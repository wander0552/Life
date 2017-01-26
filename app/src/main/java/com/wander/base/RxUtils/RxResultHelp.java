package com.wander.base.RxUtils;


import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
public class RxResultHelp {
    /**
     * 可用于验证token
     *
     * @param <T>
     * @return
     */
//    public static <T> Observable.Transformer<ApiResponse<T>, T> handleResult() {
//
//        return apiResponseObservable -> apiResponseObservable.flatMap(new Func1<ApiResponse<T>, Observable<T>>() {
//            @Override
//            public Observable<T> call(ApiResponse<T> tApiResponse) {
//                if (tApiResponse.isSuccess()) {
//                    //表示成功
//                    return createData(tApiResponse.getList());
//                } else {
//                    return Observable.error(new ServerException(tApiResponse.getMsg()));
//                }
//            }
//        });
//    }

    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(t);
                e.onComplete();
            }
        });
    }

}
