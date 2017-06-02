package com.example.trung.rssreaderndk;

/**
 * Created by trung on 3/23/2017.
 */

public class NewsData {
    private String thumbUrl;
    private String title;
    private String date;

    public NewsData() {
        this.thumbUrl = null;
        this.title = null;
        this.date = null;
    }

    public NewsData(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        this.title = null;
        this.date = null;
    }

    public NewsData(String thumbUrl, String title, String date) {
        this.thumbUrl = thumbUrl;
        this.title = title;
        this.date = date;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
