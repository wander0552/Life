package com.wander.life.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wander.life.R;
import com.wander.life.bean.WorldInfo;
import com.wander.life.ui.activity.WorldDetailActivity;
import com.wander.life.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by wander on 2017/1/29.
 */

public class WorldCardAdapter extends RecyclerView.Adapter<WorldCardAdapter.WorldCardViewHolder> {
    private Context context;
    private List<WorldInfo> mList;

    public WorldCardAdapter(Context context, List<WorldInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public WorldCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_world_card, parent, false);
        return new WorldCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorldCardViewHolder holder, int position) {
        WorldInfo worldInfo = mList.get(position);
        ImageLoader.getInstance().displayImage(worldInfo.getPic1(), holder.item_pic, ImageLoaderUtils.getDefaultImage());

        holder.item_title.setText(worldInfo.getTitle());
        holder.item_des.setText(worldInfo.getDes());
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "id" + worldInfo.getwId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, WorldDetailActivity.class);
            intent.putExtra("url", worldInfo.getPic1());
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.item_pic, context.getString(R.string.item_world_image)).toBundle();
            ActivityCompat.startActivity(context, intent, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class WorldCardViewHolder extends RecyclerView.ViewHolder {
        TextView item_title, item_des;
        ImageView item_pic;

        public WorldCardViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.world_card_title);
            item_des = (TextView) itemView.findViewById(R.id.world_card_des);
            item_pic = (ImageView) itemView.findViewById(R.id.world_card_img);
        }
    }
}
