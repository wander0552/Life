package com.wander.life.presenter;

import android.content.Context;

import com.wander.life.bean.Letter;
import com.wander.life.database.DbUtils;
import com.wander.life.ui.iviews.IMailBoxView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wander on 2017/2/7.
 */

public class MailBoxPresenter extends BasePresenter<IMailBoxView> {
    private List<Letter> mList;
    private int DEFAULT_PAGE_SIZE = 30;
    private int pageNum = 0;

    public MailBoxPresenter(Context context, IMailBoxView mView) {
        super(context, mView);
    }

    public List<Letter> getList() {
        return mList;
    }

    public void loadData() {
        Observable.create(new ObservableOnSubscribe<List<Letter>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Letter>> e) throws Exception {
                e.onNext(DbUtils.listLetter(Letter.LETTER_TYPE_DRAFT, pageNum, DEFAULT_PAGE_SIZE));
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Letter>>() {
            @Override
            public void accept(List<Letter> letters) throws Exception {
                mView.onLoadSuccess(letters);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
