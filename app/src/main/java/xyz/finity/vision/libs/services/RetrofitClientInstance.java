package xyz.finity.vision.libs.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class RetrofitClientInstance {
    private static Retrofit retrofit;

//    private static final String BASE_URL = "http://10.42.0.1/vision/";
//    private static final String BASE_URL = "http://10.0.2.2/vision/";
//    private static final String BASE_URL = "http://192.168.42.50/vision/";
    private static final String BASE_URL = "http://192.168.43.134/vision/";
//    private static final String BASE_URL = "http://35.198.233.167/vision/";
//    private static final String BASE_URL = "https://balilingo.ooo/vision/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
