package com.example.tugassensor;

import com.example.tugassensor.ModelResults;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
NIM : 10120045
Nama : Fifit Siti Nurfitriyati Basyari
Kelas : IF-2
 */

public class ModelResultNearby {
    @SerializedName("results")
    private List<ModelResults> modelResults;

    public List<ModelResults> getModelResults() {
        return modelResults;
    }

    public void setModelResults(List<ModelResults> modelResults) {
        this.modelResults = modelResults;
    }
}
