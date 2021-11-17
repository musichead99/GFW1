package com.example.capstonedesign.retrofit;

import java.util.concurrent.TimeUnit;
import android.content.Context;

import com.example.capstonedesign.PreferenceManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Context appContext = null;
    private static RetrofitClient instance = null;
    private static initMyApi initMyApi;

    //사용하고 있는 서버 BASE 주소
    private static String baseUrl = "http://125.6.37.125:5000";

    private RetrofitClient() {
        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .build();
        /*OkHttpClient client = null;
        if(appContext != null){
            String token = PreferenceManager.getString(appContext,"token");
            if(token != null){
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(30,TimeUnit.SECONDS)
                        .writeTimeout(15,TimeUnit.SECONDS)
                        .addInterceptor(new Interceptor() {
                            // interceptor를 추가해서 만약에 token이 Preference data로 저장되어 있으면
                            // Header를 추가.
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                OkHttpClient client = new OkHttpClient.Builder()
                                        .addInterceptor(interceptor)
                                        .connectTimeout(1, TimeUnit.MINUTES)
                                        .readTimeout(30,TimeUnit.SECONDS)
                                        .writeTimeout(15,TimeUnit.SECONDS)
                                        .addInterceptor(new Interceptor() {
                                                            // interceptor를 추가해서 만약에 token이 Preference data로 저장되어 있으면
                                                            // Header를 추가.
                                                            @Override
                                                            public Response intercept(Chain chain) throws IOException {
                                                                if (appContext != null) {
                                                                    String token = PreferenceManager.getString(appContext, "token");
                                                                    if (token != null) {
                                                                        Request newRequest = chain.request().newBuilder()
                                                                                .addHeader("Authorization", "Bearer " + token)
                                                                                .build();
                                                                        return chain.proceed(newRequest);
                                                                    }
                                                                }
                                                                return null;
                                                            }
                                                        })
                                                        .build();
                                return null;
                           }
                                        else{
                                            client = OkhttpClientBuilding(interceptor);
                                        }
                                    }else{
                                        client = OkhttpClientBuilding(interceptor);
                                    }
                                                    }
                                                    else return null;
                                                }
                                            })
                .build();*/

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) //로그 기능 추가
                .build();

        initMyApi = retrofit.create(initMyApi.class);
    }
    private RetrofitClient(Context appContext) {
        this.appContext = appContext;
        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = null;

        if(appContext != null){
            String token = PreferenceManager.getString(appContext,"token");
            if(token != null){
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(30,TimeUnit.SECONDS)
                        .writeTimeout(15,TimeUnit.SECONDS)
                        .addInterceptor(new Interceptor() {
                            // interceptor를 추가해서 만약에 token이 Preference data로 저장되어 있으면
                            // Header를 추가.
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request newRequest = chain.request().newBuilder()
                                        .addHeader("Authorization","Bearer "+token)
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        })
                        .build();
            }
            else{
                client = OkhttpClientBuilding(interceptor);
            }
        }else{
            client = OkhttpClientBuilding(interceptor);
        }

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) //로그 기능 추가
                .build();

        initMyApi = retrofit.create(initMyApi.class);
    }

    public RetrofitClient setContext(Context appContext){
        this.appContext = appContext;
        return instance;
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public static RetrofitClient getNewInstance(Context appContext){
        instance = new RetrofitClient(appContext);
        return instance;
    }

    public static initMyApi getRetrofitInterface() {
        return initMyApi;
    }
}
