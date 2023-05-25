package com.sakhno.newsapp.list;

import android.view.View;

import com.sakhno.newsapp.NewsItem;

public interface OnRecyclerViewItemClickListener {
    void onItemClick(NewsItem item, View view);
}
