package com.geekgirl.android.popularmovies.service;

import com.geekgirl.android.popularmovies.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rim Gazzah on 16/09/18
 */
public class ServiceInitiator {

    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = BuildConfig.POPULAR_MOVIES_KEY;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl httpUrl = request.url();

                    httpUrl = httpUrl.newBuilder()
                            .addQueryParameter("api_key", API_KEY)
                            .build();

                    request = request.newBuilder().url(httpUrl).build();
                    return chain.proceed(request);
                }
            }).build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static Webservice createService() {
        return retrofit.create(Webservice.class);
    }
}
