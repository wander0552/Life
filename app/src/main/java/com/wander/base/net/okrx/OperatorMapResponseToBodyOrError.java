package com.wander.base.net.okrx;


import com.lzy.okgo.model.Response;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 */
final class OperatorMapResponseToBodyOrError<T> implements ObservableOperator<T, Response<T>> {

    private static final OperatorMapResponseToBodyOrError<Object> INSTANCE = new OperatorMapResponseToBodyOrError<>();

    @SuppressWarnings("unchecked") // Safe because of erasure.
    static <R> OperatorMapResponseToBodyOrError<R> instance() {
        return (OperatorMapResponseToBodyOrError<R>) INSTANCE;
    }

    @Override
    public Observer<? super Response<T>> apply(Observer<? super T> observer) throws Exception {
        return new Observer<Response<T>>() {
            @Override
            public void onSubscribe(Disposable d) {
                observer.onSubscribe(d);
            }

            @Override
            public void onNext(Response<T> response) {
                if (response.isSuccessful()) {
                    observer.onNext(response.body());
                } else {
                    observer.onError(new HttpException(response));
                }
            }

            @Override
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        };
    }
}
