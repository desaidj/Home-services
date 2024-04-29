package com.example.prototype_1.ServiceMilegiOne.utils;

import com.google.firebase.Timestamp;

public  class Orders {

    private String job;
    private Timestamp messageTime;
    private  boolean  is_complete;
    public Orders(){

    }

    public  Orders(boolean is_complete, String job , Timestamp messageTime ){
        this.job = job;
        this.is_complete = is_complete;
        this.messageTime = messageTime;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isIs_complete() {
        return is_complete;
    }

    public void setIs_complete(boolean is_complete) {
        this.is_complete = is_complete;
    }
}
