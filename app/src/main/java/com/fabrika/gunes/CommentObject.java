package com.fabrika.gunes;

public class CommentObject {

    String comment_author, comment_body, comment_id, comment_date;
    long comment_like_number, comment_author_avatar;

    public CommentObject(String comment_author, String comment_body, String comment_id, String comment_date, long comment_like_number) {
        this.comment_author = comment_author;
        this.comment_body = comment_body;
        this.comment_id = comment_id;
        this.comment_date = comment_date;
        this.comment_like_number = comment_like_number;
    }

    public long getComment_author_avatar() {
        return comment_author_avatar;
    }

    public void setComment_author_avatar(long comment_author_avatar) {
        this.comment_author_avatar = comment_author_avatar;
    }

    public CommentObject(){}

    public String getComment_author() {
        return comment_author;
    }

    public void setComment_author(String comment_author) {
        this.comment_author = comment_author;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public long getComment_like_number() {
        return comment_like_number;
    }

    public void setComment_like_number(long comment_like_number) {
        this.comment_like_number = comment_like_number;
    }
}
