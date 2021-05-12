package com.fabrika.gunes;

import java.util.ArrayList;

public class MakalaModel {
    int viewType;
    String makala_id, article_title, article_body, article_img_url, article_date, article_author, article_category;
    String makala_comments;
    long article_view_number, article_like_number, article_dislike_number, article_number;
    ArrayList<MakalaModel> listMakalaModel;
    boolean article_valid = false;

    public ArrayList<MakalaModel> getListMakalaModel() {
        return listMakalaModel;
    }

    public boolean isArticle_valid() {
        return article_valid;
    }

    public long getArticle_number() {
        return article_number;
    }

    public void setArticle_number(long article_number) {
        this.article_number = article_number;
    }

    public void setArticle_valid(boolean article_valid) {
        this.article_valid = article_valid;
    }

    public void setListMakalaModel(ArrayList<MakalaModel> listMakalaModel) {
        this.listMakalaModel = listMakalaModel;
    }

    public MakalaModel(int viewType, String makala_id, String article_title, String article_body, String article_img_url, String article_date, String article_author, long article_view_number, String article_category, String makala_comments, long article_like_number, long article_dislike_number) {
        this.viewType = viewType;
        this.makala_id = makala_id;
        this.article_title = article_title;
        this.article_body = article_body;
        this.article_img_url = article_img_url;
        this.article_date = article_date;
        this.article_author = article_author;
        this.article_view_number = article_view_number;
        this.article_category = article_category;
        this.makala_comments = makala_comments;
        this.article_like_number = article_like_number;
        this.article_dislike_number = article_dislike_number;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getMakala_id() {
        return makala_id;
    }

    public void setMakala_id(String makala_id) {
        this.makala_id = makala_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_body() {
        return article_body;
    }

    public void setArticle_body(String article_body) {
        this.article_body = article_body;
    }

    public String getArticle_img_url() {
        return article_img_url;
    }

    public void setArticle_img_url(String article_img_url) {
        this.article_img_url = article_img_url;
    }

    public String getArticle_date() {
        return article_date;
    }

    public void setArticle_date(String article_date) {
        this.article_date = article_date;
    }

    public String getArticle_author() {
        return article_author;
    }

    public void setArticle_author(String article_author) {
        this.article_author = article_author;
    }

    public long getArticle_view_number() {
        return article_view_number;
    }

    public void setArticle_view_number(long article_view_number) {
        this.article_view_number = article_view_number;
    }

    public String getArticle_category() {
        return article_category;
    }

    public void setArticle_category(String article_category) {
        this.article_category = article_category;
    }

    public String getMakala_comments() {
        return makala_comments;
    }

    public void setMakala_comments(String makala_comments) {
        this.makala_comments = makala_comments;
    }

    public long getArticle_like_number() {
        return article_like_number;
    }

    public void setArticle_like_number(long article_like_number) {
        this.article_like_number = article_like_number;
    }

    public long getArticle_dislike_number() {
        return article_dislike_number;
    }

    public void setArticle_dislike_number(long article_dislike_number) {
        this.article_dislike_number = article_dislike_number;
    }

    public MakalaModel(int viewType, String makala_id, String article_title, String article_body, String article_img_url, String article_date, String article_author) {
        this.viewType = viewType;
        this.makala_id = makala_id;
        this.article_title = article_title;
        this.article_body = article_body;
        this.article_img_url = article_img_url;
        this.article_date = article_date;
        this.article_author = article_author;
    }

    public MakalaModel(){}
}