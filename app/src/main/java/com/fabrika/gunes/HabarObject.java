package com.fabrika.gunes;

public class HabarObject {

    String news_body, news_author, news_category, news_date, news_img_url, news_title;
    int news_view_number;
    Boolean isBookmarked = false;
    String id;
    long news_number, news_id;

    public long getNews_id() {
        return news_id;
    }

    public void setNews_id(long news_id) {
        this.news_id = news_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBookmarked() {
        return isBookmarked;
    }

    public long getNews_number() {
        return news_number;
    }

    public void setNews_number(long news_number) {
        this.news_number = news_number;
    }

    public void setBookmarked(Boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public HabarObject(String news_body, String news_author, String news_category, String news_date, String news_img_url, String news_title, int news_view_number) {
        this.news_body = news_body;
        this.news_author = news_author;
        this.news_category = news_category;
        this.news_date = news_date;
        this.news_img_url = news_img_url;
        this.news_title = news_title;
        this.news_view_number = news_view_number;
    }

    public HabarObject(){}

    public String getNews_body() {
        return news_body;
    }

    public void setNews_body(String news_body) {
        this.news_body = news_body;
    }

    public String getNews_author() {
        return news_author;
    }

    public void setNews_author(String news_author) {
        this.news_author = news_author;
    }

    public String getNews_category() {
        return news_category;
    }

    public void setNews_category(String news_category) {
        this.news_category = news_category;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_img_url() {
        return news_img_url;
    }

    public void setNews_img_url(String news_img_url) {
        this.news_img_url = news_img_url;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public int getNews_view_number() {
        return news_view_number;
    }

    public void setNews_view_number(int news_view_number) {
        this.news_view_number = news_view_number;
    }
}
