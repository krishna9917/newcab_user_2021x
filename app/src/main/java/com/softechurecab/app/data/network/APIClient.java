package com.softechurecab.app.data.network;

import androidx.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.softechurecab.app.BuildConfig;
import com.softechurecab.app.MvpApplication;
import com.softechurecab.app.data.SharedHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

public class APIClient {

    private static Retrofit retrofit = null;

    public static ApiInterface getAPIClient() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(getHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
    public static ApiInterface getAPIClient2() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(getHttpClient2())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }

    private static OkHttpClient getHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient()
                .newBuilder()
                //.cache(new Cache(MvpApplication.getInstance().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(new AddHeaderInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
    } private static OkHttpClient getHttpClient2() {


        return new OkHttpClient()
                .newBuilder()
                //.cache(new Cache(MvpApplication.getInstance().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .connectTimeout(10, TimeUnit.MINUTES)
                .addNetworkInterceptor(new AddHeaderInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)

                .build();
    }

    private static class AddHeaderInterceptor implements Interceptor
    {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("X-Requested-With", "XMLHttpRequest");
            builder.addHeader("Authorization", SharedHelper.getKey(MvpApplication.getInstance(), "access_token", ""));
            builder.addHeader("api-key", "cefb8f2a-6e6d-4f5f-936e-a2accc5bb8df");
            return chain.proceed(builder.build());
        }
    }
}
