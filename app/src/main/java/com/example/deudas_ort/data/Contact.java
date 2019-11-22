package com.example.deudas_ort.data;

import android.graphics.Bitmap;

public class Contact {
    String name;
    Bitmap bitMap;
    String phone;
    String email;
    String photoThumbnailUri;

    public Bitmap getBitMap() {
        return bitMap;
    }
    public String getPhone() {
        return phone;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhotoThumbnailUri() {
        return photoThumbnailUri;
    }
    public void setBitMap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhotoThumbnailUri(String photoThumbnailUri) {
        this.photoThumbnailUri = photoThumbnailUri;
    }
}
