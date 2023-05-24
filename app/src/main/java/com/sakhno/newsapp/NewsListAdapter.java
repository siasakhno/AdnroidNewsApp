package com.sakhno.newsapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sakhno.newsapp.databinding.NewsItemBinding;
import com.sakhno.newsapp.db.DatabaseHelper;
import com.sakhno.newsapp.list.OnRecyclerViewItemClickListener;
import com.sakhno.newsapp.list.OnRemoveButtonClickListener;


import java.util.Collections;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    private List<NewsItem> mNewsList = Collections.emptyList();
    private DatabaseHelper databaseHelper;
    private OnRecyclerViewItemClickListener itemClickListener;
    private OnRemoveButtonClickListener removeButtonClickListener;


    class NewsViewHolder extends RecyclerView.ViewHolder {
       private NewsItemBinding newsItemBinding;
       private NewsItem newsItem;

        public NewsViewHolder(NewsItemBinding binding) {
            super(binding.getRoot());
            newsItemBinding = binding;
        }

        public void bind(NewsItem newsItem) {
            this.newsItem = newsItem;
            newsItemBinding.newsTitle.setText(newsItem.getTitle());
            newsItemBinding.category.setText(newsItem.getCategory());
            newsItemBinding.source.setText(newsItem.getSource());
            newsItemBinding.removeButton.setContentDescription(String.valueOf(newsItem.getId()));

            Glide.with(newsItemBinding.newsImage.getContext())
                    .load(newsItem.getUrl())
                    //.placeholder(R.mipmap.poster_tmp)
                    //.error(R.drawable.ic_baseline_error_outline_24)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(newsItemBinding.image);
        }

        public NewsItem getNewsItem() {
            return newsItem;
        }
    }

    @NonNull
    @Override
    public NewsListAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NewsItemBinding newsItemBinding = NewsItemBinding.inflate(inflater, parent, false);
        return new NewsViewHolder(newsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsListAdapter.NewsViewHolder holder, final int position) {
        holder.bind(mNewsList.get(position));

        holder.newsItemBinding.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeButtonClickListener != null) {
                    removeButtonClickListener.onRemoveBtnClick(v);
                }
            }
        });

        holder.newsItemBinding.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.getNewsItem());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


    public void setNewsList(List<NewsItem> news) {
        this.mNewsList = news;
        Log.e(NewsListAdapter.class.getCanonicalName(), this.mNewsList.toString());
    }

    public List<NewsItem> getNewsList() {
        return mNewsList;
    }

    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener mClickListener) {
        this.itemClickListener = mClickListener;
    }

    public void setRemoveButtonClickListener(OnRemoveButtonClickListener removeButtonClickListener) {
        this.removeButtonClickListener = removeButtonClickListener;
    }
}
