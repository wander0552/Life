package com.wander.base.RxUtils.rxbus;


import android.support.annotation.NonNull;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @version 1.4
 * bus重新改为线程安全对象
 *
 * Created by Android on 2016/6/6.
 */
public class RxBus {
    private static RxBus instance;

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }
    //TAG默认值
    public static final int TAG_DEFAULT = 0;
    public static final int TAG_ERROR = -1;
    //发布者
    private final Subject<Object> bus;

    //存放订阅者信息
    private Map<Object, CompositeDisposable> subscriptions = new HashMap<>();

    /**
     * PublishSubject 创建一个可以在订阅之后把数据传输给订阅者Subject
     * SerializedSubject 序列化Subject为线程安全的Subject RxJava2 暂无
     */
    public RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public void post(@NonNull Object obj) {
        post(TAG_DEFAULT, obj);
    }

    /**
     * 发布事件
     * @param code
     * @param obj
     */
    public void post(@NonNull int code, @NonNull Object obj) {
        bus.onNext(new Msg(code, obj));
    }

//    public Observable<Object> tObservable() {
//        return tObservable(Object.class);
//    }
//
//    public <T> Observable<T> tObservable(Class<T> eventType) {
//        return tObservable(TAG_DEFAULT, eventType);
//    }

    /**
     * 订阅事件
     * @return
     */
    public <T> Observable tObservable(int code, final Class<T> eventType) {
        return bus.ofType(Msg.class)//判断接收事件类型
                .filter(msg -> msg.code==code)
                .map(new Function<Msg, Object>() {
                    @Override
                    public Object apply(Msg msg) throws Exception {
                        return msg.object;
                    }
                })
                .cast(eventType);
    }

    /**
     * 订阅者注册
     * @param subscriber
     */
    public void register(@NonNull Object subscriber) {
        Flowable.just(subscriber)
                .filter(s -> s != null)//判断订阅者不为空
                .filter(s -> subscriptions.get(subscriber)==null) //判断订阅者没有在序列中
                .map(s -> s.getClass())
                .flatMap(s -> Flowable.fromArray(s.getDeclaredMethods()))//获取订阅者方法并且用Observable装载
                .map(m -> {m.setAccessible(true);return m;})//使非public方法可以被invoke,并且关闭安全检查提升反射效率
                .filter(m -> m.isAnnotationPresent(Subscribe.class))//方法必须被Subscribe注解
                .subscribe(m -> {
                    addSubscription(m,subscriber);
                });
    }

    /**
     * 添加订阅
     * @param m 方法
     * @param subscriber 订阅者
     */
    private void addSubscription(Method m, Object subscriber){
        //获取方法内参数
        Class[] parameterType = m.getParameterTypes();
        //只获取第一个方法参数，否则默认为Object
        Class cla = Object.class;
        if (parameterType.length > 1) {
            cla = parameterType[0];
        }
        //获取注解
        Subscribe sub = m.getAnnotation(Subscribe.class);
        //订阅事件
        Disposable disposable = tObservable(sub.tag(), cla)
                .observeOn(EventThread.getScheduler(sub.thread()))
                .subscribe(o -> {
                            try {
                                m.invoke(subscriber, o);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        },
                        e -> System.out.println("this object is not invoke"));
        putSubscriptionsData(subscriber,disposable);
    }

    /**
     * 添加订阅者到map空间来unRegister
     * @param subscriber 订阅者
     * @param disposable 订阅者 Subscription
     */
    private void putSubscriptionsData(Object subscriber, Disposable disposable){
        CompositeDisposable subs = subscriptions.get(subscriber);
        if (subs == null) {
            subs = new CompositeDisposable();
        }
        subs.add(disposable);
        subscriptions.put(subscriber, subs);
    }

    /**
     * 解除订阅者
     * @param subscriber 订阅者
     */
    public void unRegister(Object subscriber) {
        Flowable.just(subscriber)
                .filter(s -> s!=null)
                .map(s -> subscriptions.get(s))
                .filter(subs -> subs!=null)
                .subscribeWith(new Subscriber<CompositeDisposable>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(CompositeDisposable compositeDisposable) {
                        compositeDisposable.dispose();
                        subscriptions.remove(subscriber);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
