package com.wander.life.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wander.life.R;
import com.wander.life.bean.Letter;
import com.wander.life.ui.activity.WriteActivity;
import com.wander.life.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wander on 2017/2/7.
 */

public class MailBoxAdapter extends RecyclerView.Adapter<MailBoxAdapter.MailViewHolder> {
    private Context mContext;
    private List<Letter> mList;
    String pic2 ="http://s3.sinaimg.cn/orignal/48f5e36c5108f6e5af9d2";

    public MailBoxAdapter(Context mContext, List<Letter> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mail_all, parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MailViewHolder holder, int position) {
        final Letter letter = mList.get(position);
        holder.tv_createTime.setText(DateFormat.format("yyyy-MM-dd", letter.getCreateTime()));
        String content = letter.getContent();
        if (!TextUtils.isEmpty(content)) {
            holder.tv_des.setText(content.substring(0, content.length() > 50 ? 50 : content.length()));}
        holder.tv_sender.setText("小白菜");
        holder.tv_address.setText(R.string.app_name);
        ImageLoader.getInstance().displayImage(pic2,holder.img_stampImage, ImageLoaderUtils.getDefaultImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WriteActivity.class);
                intent.putExtra("letter",letter);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void addData(List<Letter> letters) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(letters);
        notifyDataSetChanged();
    }


    class MailViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_createTime;
        private TextView tv_des;
        private ImageView img_stampImage;
        private ImageView img_receiveImage;
        private TextView tv_sender;
        private TextView tv_address;

        public MailViewHolder(View itemView) {
            super(itemView);
            tv_createTime = ((TextView) itemView.findViewById(R.id.mail_create_time));
            tv_des = (TextView) itemView.findViewById(R.id.mail_des);
            tv_sender = (TextView) itemView.findViewById(R.id.mail_sender);
            img_stampImage = (ImageView) itemView.findViewById(R.id.mail_stamp);
            tv_address = (TextView) itemView.findViewById(R.id.mail_address);

        }
    }
}
