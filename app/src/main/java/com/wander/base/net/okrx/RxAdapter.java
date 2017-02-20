package com.wander.base.net.okrx;


import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.adapter.CallAdapter;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Cancellable;
import io.reactivex.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 修改兼容rxjava2
 * 版    本：1.0
 * 创建日期：16/9/28
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class RxAdapter<T> implements CallAdapter<Observable<T>> {

    public static <T> RxAdapter<T> create() {
        return RxAdapter.ConvertHolder.convert;
    }

    private static class ConvertHolder {

        private static RxAdapter convert = new RxAdapter();
    }

    @Override
    public <R> Observable<T> adapt(Call<R> call) {
        return Observable.create(new CallOnSubscribe<>((Call<T>) call)) //强转,本质，T 与 R 是同一个泛型
                .subscribeOn(Schedulers.io())   //IO线程订阅网络请求
//                .map(new Func1<Response<T>, T>() {
//                    @Override
//                    public T call(Response<T> tResponse) {
//                        return tResponse.body();
//                    }
//                });
                //感觉用上面的map操作也可以完成,但是Retrofit是这么实现的,目前并不清楚具体好处在哪
                .lift(OperatorMapResponseToBodyOrError.<T>instance());
    }

    private static final class CallOnSubscribe<T> implements ObservableOnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void subscribe(ObservableEmitter<Response<T>> e) throws Exception {
            Call<T> call = originalCall.clone();
            RequestArbiter requestArbiter = new RequestArbiter(call, e);
            e.setDisposable(requestArbiter);
            e.setCancellable(requestArbiter);
        }
    }

    private static final class RequestArbiter<T> implements Cancellable, Disposable {
        private final Call<T> call;
        private final ObservableEmitter<Response<T>> subscriber;

        RequestArbiter(Call<T> call, ObservableEmitter<Response<T>> subscriber) {
            this.call = call;
            this.subscriber = subscriber;
            request();
        }

        /**
         * 生产事件,将同步请求转化为Rx的事件
         */
        public void request() {
            try {
                Response<T> response = call.execute();
                if (!subscriber.isDisposed()) {
                    subscriber.onNext(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isDisposed()) {
                    subscriber.onError(t);
                }
                return;
            }
            if (!subscriber.isDisposed()) {
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public void dispose() {
            call.cancel();
        }

        @Override
        public boolean isDisposed() {
            return call.isCanceled();
        }
    }
}