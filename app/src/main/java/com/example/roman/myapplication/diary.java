package com.example.roman.myapplication;

import java.io.Serializable;
import java.util.UUID;
import MyDate.Date;

/**
 * Created by Roman on 2016/9/20.
 */
public class diary implements Serializable{

    private static final long serialVersionUID = 1L;

    private UUID mID;
    private Date mDate;
    private String mContent;

    public diary() {
        this(UUID.randomUUID());
    }

    public diary(UUID id){
        mID = id;
        mDate = new Date();
        mContent = null;
    }

    public UUID getID() {
        return mID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(int year,int month,int day) {
        mDate.setYear(year);
        mDate.setMonth(month);
        mDate.setDay(day);
    }

    public String getContent(){
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }

    public boolean isEmpty(){
        return (mContent==null);
    }
}