package com.wander.life.ui.iviews;

import com.wander.life.bean.Letter;

import java.util.List;

/**
 * Created by wander on 2017/2/7.
 */

public interface IMailBoxView extends IView {
    void onLoadSuccess(List<Letter> letters);
}
