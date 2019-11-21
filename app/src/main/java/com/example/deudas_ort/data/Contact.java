package com.example.deudas_ort.data;

import android.graphics.Bitmap;

public class Contact {
    String name;
    Bitmap bitMap;
    String phone;
    Boolean checkedBox = false;
    String email;

    public Bitmap getBitMap() {
        return bitMap;
    }
    public String getPhone() {
        return phone;
    }
    public Boolean getCheckedBox() {
        return checkedBox;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public void setBitMap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setCheckedBox(Boolean checkedBox) {
        this.checkedBox = checkedBox;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
