package com.ire.blogbot;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ire.blogbot.model.News;
import com.ire.blogbot.utils.TechNetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ire on 5/23/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

//    Variable used to reference the model
    private ArrayList<News> mNews = new ArrayList<>();
    private static ClickListener clickListener;


//    Setting the adapter
    public NewsAdapter(ArrayList<News> news) {
        mNews = news;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        String imagePath = mNews.get(position).getImageUrl();
        Picasso.with(holder.mImageView.getContext()).load(imagePath).into(holder.mImageView);

        holder.mNewsTextView.setText(mNews.get(position).getNews());
        holder.mTimeStampTextView.setText(mNews.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    //    NewsHolder class that extends the ViewHolder
    public static class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mImageView;
        private TextView mNewsTextView;
        private TextView mTimeStampTextView;
        private String mDetailUrl;
//
        //   Setting the views
        public NewsHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.simple_imageView);
            mNewsTextView = (TextView) itemView.findViewById(R.id.news_tv);
            mTimeStampTextView = (TextView) itemView.findViewById(R.id.time_tv);
        }

        @Override
        public void onClick(View view){
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener listener){
        NewsAdapter.clickListener = listener;
    }

    public interface ClickListener{
        void onItemClick(int position, View v);
    }

}
