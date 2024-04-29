package com.example.prototype_1.ServiceMilegiOne.utils;

public class User {
    private String first_name ,last_name , mob_no,email, address;

    public User (){

    }

    public User(String fname ,String lname , String mob,String email){

        this.first_name = fname;
        this.last_name = lname;
        this.email = email;
        this.mob_no = mob;
        this.address = null;
    }

    public String getEmail() {
        return email;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

}
