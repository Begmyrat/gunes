package com.fabrika.gunes;

public class BlogObject {
    String blog_id, blog_author, blog_body, blog_date;
    long blog_like_number, blog_comment_number, blog_view_number, blog_author_avatar;

    public long getBlog_comment_number() {
        return blog_comment_number;
    }

    public void setBlog_comment_number(long blog_comment_number) {
        this.blog_comment_number = blog_comment_number;
    }

    public long getBlog_author_avatar() {
        return blog_author_avatar;
    }

    public void setBlog_author_avatar(long blog_author_avatar) {
        this.blog_author_avatar = blog_author_avatar;
    }

    public long getBlog_view_number() {
        return blog_view_number;
    }

    public void setBlog_view_number(long blog_view_number) {
        this.blog_view_number = blog_view_number;
    }

    public BlogObject(String blog_id, String blog_author, String blog_body, String blog_date, long blog_like_number) {
        this.blog_id = blog_id;
        this.blog_author = blog_author;
        this.blog_body = blog_body;
        this.blog_date = blog_date;
        this.blog_like_number = blog_like_number;
    }

    public BlogObject(){}

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getBlog_author() {
        return blog_author;
    }

    public void setBlog_author(String blog_author) {
        this.blog_author = blog_author;
    }

    public String getBlog_body() {
        return blog_body;
    }

    public void setBlog_body(String blog_body) {
        this.blog_body = blog_body;
    }

    public String getBlog_date() {
        return blog_date;
    }

    public void setBlog_date(String blog_date) {
        this.blog_date = blog_date;
    }

    public long getBlog_like_number() {
        return blog_like_number;
    }

    public void setBlog_like_number(long blog_like_number) {
        this.blog_like_number = blog_like_number;
    }
}
