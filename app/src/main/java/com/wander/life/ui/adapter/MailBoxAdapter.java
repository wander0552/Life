package com.wander.life.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wander.life.R;
import com.wander.life.bean.Letter;

import java.util.List;

/**
 * Created by wander on 2017/2/7.
 */

public class MailBoxAdapter extends RecyclerView.Adapter<MailBoxAdapter.MailViewHolder> {
    private Context mContext;
    private List<Letter> mList;

    @Override
    public MailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater.from(mContext).inflate(R.layout.item_mail_all, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(MailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MailViewHolder extends RecyclerView.ViewHolder {
        private TextView createTime;
        private TextView des;
        private ImageView stampImage;
        private ImageView receiveImage;

        public MailViewHolder(View itemView) {
            super(itemView);
        }
    }
}
