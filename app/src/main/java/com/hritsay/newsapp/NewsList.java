package com.hritsay.newsapp;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsList {
    @SerializedName("articles")
    private List<NewsItem> newsList;

    public NewsList(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    public List<NewsItem> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @Override
    public String toString() {
        return "NewsList{" +
                "newsList=" + newsList +
                '}';
    }
}
