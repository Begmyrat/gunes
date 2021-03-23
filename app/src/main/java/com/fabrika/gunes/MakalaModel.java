package com.fabrika.gunes;

import java.util.ArrayList;

public class MakalaModel {
    int viewType;
    String makala_id, makala_title, makala_body, makala_img_url, makala_date, makala_author, makala_viewed_number, makala_category;
    String makala_comments, makala_like_number, makala_dislike_number;
    ArrayList<MakalaModel> listMakalaModel;

    public ArrayList<MakalaModel> getListMakalaModel() {
        return listMakalaModel;
    }

    public void setListMakalaModel(ArrayList<MakalaModel> listMakalaModel) {
        this.listMakalaModel = listMakalaModel;
    }

    public MakalaModel(int viewType, String makala_id, String makala_title, String makala_body, String makala_img_url, String makala_date, String makala_author, String makala_viewed_number, String makala_category, String makala_comments, String makala_like_number, String makala_dislike_number) {
        this.viewType = viewType;
        this.makala_id = makala_id;
        this.makala_title = makala_title;
        this.makala_body = makala_body;
        this.makala_img_url = makala_img_url;
        this.makala_date = makala_date;
        this.makala_author = makala_author;
        this.makala_viewed_number = makala_viewed_number;
        this.makala_category = makala_category;
        this.makala_comments = makala_comments;
        this.makala_like_number = makala_like_number;
        this.makala_dislike_number = makala_dislike_number;
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

    public String getMakala_title() {
        return makala_title;
    }

    public void setMakala_title(String makala_title) {
        this.makala_title = makala_title;
    }

    public String getMakala_body() {
        return makala_body;
    }

    public void setMakala_body(String makala_body) {
        this.makala_body = makala_body;
    }

    public String getMakala_img_url() {
        return makala_img_url;
    }

    public void setMakala_img_url(String makala_img_url) {
        this.makala_img_url = makala_img_url;
    }

    public String getMakala_date() {
        return makala_date;
    }

    public void setMakala_date(String makala_date) {
        this.makala_date = makala_date;
    }

    public String getMakala_author() {
        return makala_author;
    }

    public void setMakala_author(String makala_author) {
        this.makala_author = makala_author;
    }

    public String getMakala_viewed_number() {
        return makala_viewed_number;
    }

    public void setMakala_viewed_number(String makala_viewed_number) {
        this.makala_viewed_number = makala_viewed_number;
    }

    public String getMakala_category() {
        return makala_category;
    }

    public void setMakala_category(String makala_category) {
        this.makala_category = makala_category;
    }

    public String getMakala_comments() {
        return makala_comments;
    }

    public void setMakala_comments(String makala_comments) {
        this.makala_comments = makala_comments;
    }

    public String getMakala_like_number() {
        return makala_like_number;
    }

    public void setMakala_like_number(String makala_like_number) {
        this.makala_like_number = makala_like_number;
    }

    public String getMakala_dislike_number() {
        return makala_dislike_number;
    }

    public void setMakala_dislike_number(String makala_dislike_number) {
        this.makala_dislike_number = makala_dislike_number;
    }

    public MakalaModel(int viewType, String makala_id, String makala_title, String makala_body, String makala_img_url, String makala_date, String makala_author) {
        this.viewType = viewType;
        this.makala_id = makala_id;
        this.makala_title = makala_title;
        this.makala_body = makala_body;
        this.makala_img_url = makala_img_url;
        this.makala_date = makala_date;
        this.makala_author = makala_author;
    }

    public MakalaModel(){}
}
