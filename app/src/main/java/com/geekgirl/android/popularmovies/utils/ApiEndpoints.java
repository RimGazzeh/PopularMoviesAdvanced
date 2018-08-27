package com.geekgirl.android.popularmovies.utils;

import com.geekgirl.android.popularmovies.BuildConfig;

/**
 * Created by Rim Gazzah on 15/08/18
 */
public class ApiEndpoints {
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = BuildConfig.POPULAR_MOVIES_KEY;

    public static final String GET_MOVIES_POPULAR = BASE_URL+"popular";
    public static final String GET_MOVIES_TOP_RATED = BASE_URL+"top_rated";
}
