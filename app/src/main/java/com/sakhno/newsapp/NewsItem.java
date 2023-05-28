package com.sakhno.newsapp;

import java.time.LocalDateTime;

public class NewsItem {

    private int id;
    private String title;
    private String category;
    private String source;
    private String url;
    private String description;

    private LocalDateTime dateTime;

    public NewsItem() {
    }

    public NewsItem(int id, String title, String category, String source, String url, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.source = source;
        this.url = url;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "NewsItem:" +
                "\n\ttitle='" + title + '\'' +
                "\n\tcategory='" + category + '\'' +
                "\n\tsource='" + source + '\'' +
                "\n\turl='" + url + '\'' +
                "\n\t, description='" + description + '\'' +
                "\n\n";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
