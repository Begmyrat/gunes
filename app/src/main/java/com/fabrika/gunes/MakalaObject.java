package com.fabrika.gunes;

import java.util.ArrayList;

public class MakalaObject {
    String id;
    String title;
    String body;
    String writer;
    String date;
    String rowTitle;
    String category;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    String img_url;
    ArrayList<MakalaObject> subMakala;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRowTitle() {
        return rowTitle;
    }

    public void setRowTitle(String rowTitle) {
        this.rowTitle = rowTitle;
    }

    public ArrayList<MakalaObject> getSubMakala() {
        return subMakala;
    }

    public void setSubMakala(ArrayList<MakalaObject> subMakala) {
        this.subMakala = subMakala;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MakalaObject() {
        subMakala = new ArrayList<>();
    }

    public MakalaObject(String id, String title, String body, String category, String writer, String date, String rowTitle, ArrayList<MakalaObject> subMakala) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.writer = writer;
        this.date = date;
        this.rowTitle = rowTitle;
        this.subMakala = subMakala;
        this.category = category;
    }
}
