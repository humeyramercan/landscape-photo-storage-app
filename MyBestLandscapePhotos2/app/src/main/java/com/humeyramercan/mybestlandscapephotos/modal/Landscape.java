package com.humeyramercan.mybestlandscapephotos.modal;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Landscape implements Serializable {
    String date;
    String note;
    String placeName;


    byte[] placeImage;

    public String getDate() { return date; }
    public String getNote() { return note; }
    public String getPlaceName() { return placeName; }
    public byte[] getPlaceImage() { return placeImage; }

    public Landscape(String date, String note, String placeName, byte[] placeImage) {
        this.date = date;
        this.note = note;
        this.placeName = placeName;
        this.placeImage = placeImage;
    }
}
