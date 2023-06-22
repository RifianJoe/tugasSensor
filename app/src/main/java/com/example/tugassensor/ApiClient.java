package com.example.tugassensor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
NIM : 10120045
Nama : Fifit Siti Nurfitriyati Basyari
Kelas : IF-2
 */

public class ApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
