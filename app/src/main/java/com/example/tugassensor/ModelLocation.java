package com.example.tugassensor;

import com.google.gson.annotations.SerializedName;

/*
NIM : 10120045
Nama : Fifit Siti Nurfitriyati Basyari
Kelas : IF-2
 */

public class ModelLocation {
    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
