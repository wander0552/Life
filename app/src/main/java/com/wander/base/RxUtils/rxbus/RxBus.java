package com.wander.base.RxUtils.rxbus;


import android.support.annotation.NonNull;


import com.wander.base.log.WLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by wander on 2017/4/26.
 * 已解决leaks问题
 */
public class RxBus {
    private static final String TAG = "RxBus";
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

//    public void post(@NonNull Object obj) {
//        post(TAG_DEFAULT, obj);
//    }

    /**
     * 发布事件
     *
     * @param code
     * @param obj
     */
    public void post(@NonNull int code, @NonNull Object obj) {
        bus.onNext(new Msg(code, obj));
    }

    /**
     * 发布事件
     * 只处理code
     *
     * @param code
     */
    public void post(@NonNull int code) {
        bus.onNext(new Msg(code, new MessageEvent(code, "")));
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
     *
     * @return
     */
    public <T> Observable tObservable(final int code, final Class<T> eventType) {
        return bus.ofType(Msg.class)//判断接收事件类型
                .filter(new Predicate<Msg>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull Msg msg) throws Exception {
                        return msg.code == code;
                    }
                })
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
     * 寻找父类的方法并且限制五层
     *
     * @param subscriber
     */
    public synchronized void register(@NonNull final Object subscriber) {
        //获取订阅者方法并且用Observable装载
        //使非public方法可以被invoke,并且关闭安全检查提升反射效率
        //方法必须被Subscribe注解
        int i = 0;
        for (Class obj = subscriber.getClass(); obj != null && i < 5; obj = obj.getSuperclass(), i++) {
            for (Method method : obj.getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(Subscribe.class)) {
                    addSubscription(method, subscriber);
                }
            }
        }

    }

    /**
     * 添加订阅
     *
     * @param m          方法
     * @param subscriber 订阅者
     */
    private void addSubscription(final Method m, final Object subscriber) {
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
                .subscribe(new Consumer() {
                               @Override
                               public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                                   try {
                                       m.invoke(subscriber, o);
                                   } catch (IllegalAccessException e) {
                                       e.printStackTrace();
                                       unRegister(subscriber);
                                   } catch (InvocationTargetException e) {
                                       e.printStackTrace();
                                       unRegister(subscriber);
                                   }
                               }
                           },
                        new Consumer() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Object e) throws Exception {
                                System.out.println("this object is not invoke");
                            }
                        });
        putSubscriptionsData(subscriber, disposable);
    }

    /**
     * 添加订阅者到map空间来unRegister
     *
     * @param subscriber 订阅者
     * @param disposable 订阅者 Subscription
     */
    private void putSubscriptionsData(Object subscriber, Disposable disposable) {
        addDisposable(subscriptions, subscriber, disposable);
        WLog.d(TAG, "register:" + subscriber + "");
    }

    private void addDisposable(Map<Object, CompositeDisposable> subscriptions, Object subscriber, Disposable disposable) {
        CompositeDisposable subs = subscriptions.get(subscriber);
        if (subs == null) {
            subs = new CompositeDisposable();
            subscriptions.put(subscriber, subs);
        }
        subs.add(disposable);
    }

    /**
     * 解除订阅者
     *
     * @param subscriber 订阅者
     */
    public void unRegister(final Object subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Object to unregister must not be null.");
        }
        CompositeDisposable compositeDisposable = subscriptions.get(subscriber);
        if (compositeDisposable != null) {
            if (!compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }
            subscriptions.remove(subscriber);
            WLog.d(TAG, "unRegister:" + subscriber + "");
        }
    }

}
