package com.example.dairy.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Content {
    private  String title;
    private  String description;
    private  String createdDate;
    private String editedDate;
    private String uid;

    public Content() {
    }

    public Content(String title, String description, String createdDate, String editedDate,String uid) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.editedDate = editedDate;
        this.uid=uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate() {
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm::s");
        Date date =new Date();
        createdDate=dateFormat.format(date).toString();
        this.createdDate = createdDate;
    }

    public String getEditedDate() {
        return editedDate;
    }

    public void setEditedDate() {
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm::s");
        Date date =new Date();
        editedDate=dateFormat.format(date).toString();
        this.editedDate = editedDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
