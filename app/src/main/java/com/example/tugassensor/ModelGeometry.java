package com.example.tugassensor;

import com.google.gson.annotations.SerializedName;

/*
NIM : 10120045
Nama : Fifit Siti Nurfitriyati Basyari
Kelas : IF-2
 */

public class ModelGeometry {
    @SerializedName("location")
    private ModelLocation modelLocation;

    public ModelLocation getModelLocation() {
        return modelLocation;
    }

    public void setModelLocation(ModelLocation modelLocation) {
        this.modelLocation = modelLocation;
    }
}
