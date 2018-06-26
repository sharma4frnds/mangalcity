package net.clamour.mangalcity.webservice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Clamour on 11/29/2017.
 */

public class ApiClient {

    public static final String BASE_URL="http://emergingncr.com/mangalcity/api/";
    public static Retrofit retrofit=null;

    public static Retrofit getApiClient(){

        if(retrofit==null){

            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient().newBuilder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .build())
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    //add this dependency compile 'com.squareup.okhttp3:okhttp:3.9.0'

}
