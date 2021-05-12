package com.fabrika.gunes;

import org.w3c.dom.Comment;

public class BlogModel {

    int view_type;
    BlogObject blogObject;
    CommentObject commentObject;

    public boolean isDetail() {
        return isDetail;
    }

    public void setDetail(boolean detail) {
        isDetail = detail;
    }

    boolean isDetail = false;

    public BlogModel(int view_type, BlogObject blogObject, CommentObject commentObject) {
        this.view_type = view_type;
        this.blogObject = blogObject;
        this.commentObject = commentObject;
    }

    public BlogModel(){}

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public BlogObject getBlogObject() {
        return blogObject;
    }

    public void setBlogObject(BlogObject blogObject) {
        this.blogObject = blogObject;
    }

    public CommentObject getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(CommentObject commentObject) {
        this.commentObject = commentObject;
    }
}
