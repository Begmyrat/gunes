package com.fabrika.gunes;

public class InformationObject {
    String information_author;
    String information_body;

    public String getInformation_id() {
        return information_id;
    }

    public void setInformation_id(String information_id) {
        this.information_id = information_id;
    }

    String information_id;
    long information_number;

    public InformationObject(String information_author, String information_body) {
        this.information_author = information_author;
        this.information_body = information_body;
        this.information_number = information_number;
    }

    public InformationObject(){}

    public String getInformation_author() {
        return information_author;
    }

    public void setInformation_author(String information_author) {
        this.information_author = information_author;
    }

    public String getInformation_body() {
        return information_body;
    }

    public void setInformation_body(String information_body) {
        this.information_body = information_body;
    }

    public long getInformation_number() {
        return information_number;
    }

    public void setInformation_number(long information_number) {
        this.information_number = information_number;
    }
}
