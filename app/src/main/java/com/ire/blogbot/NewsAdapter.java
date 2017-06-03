package com.ire.blogbot;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ire.blogbot.model.News;

import java.util.ArrayList;

/**
 * Created by ire on 5/23/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

//    Variable used to reference the model
    private ArrayList<News> mNews = new ArrayList<>();

//    NewsHolder class that extends the ViewHolder
    public static class NewsHolder extends RecyclerView.ViewHolder{
//        private ImageView mImageView;
        private TextView mNewsTextView;
        private TextView mTimeStampTextView;

//   Constructor to set the views
        public NewsHolder(View itemView){
            super(itemView);
//            mImageView = (ImageView) itemView.findViewById(R.id.simple_imageView);
            mNewsTextView = (TextView) itemView.findViewById(R.id.news_tv);
            mTimeStampTextView = (TextView) itemView.findViewById(R.id.time_tv);
        }
    }

//    Constructor to set the adapter
    public NewsAdapter(ArrayList<News> news){
        mNews = news;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        NewsHolder newsHolder = new NewsHolder(view);
        return newsHolder;
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
//        String imagePath = mNews.get(position).getImageUrl();
        holder.mNewsTextView.setText(mNews.get(position).getNews());
        holder.mTimeStampTextView.setText(mNews.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }
}
