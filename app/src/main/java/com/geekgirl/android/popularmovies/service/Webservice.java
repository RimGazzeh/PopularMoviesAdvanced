package com.geekgirl.android.popularmovies.service;

import com.geekgirl.android.popularmovies.model.ApiResponse;
import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.model.Review;
import com.geekgirl.android.popularmovies.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rim Gazzah on 13/09/18
 */
public interface Webservice {

    @GET("popular")
    Call<ApiResponse<Movie>> getPopularMovies(@Query("page") String page);

    @GET("top_rated")
    Call<ApiResponse<Movie>> getMostRatedMovies(@Query("page") String page);

    @GET("{id}/videos")
    Call<ApiResponse<Video>> getMovieVideos(@Path("id") long movieId);

    @GET("{id}/reviews")
    Call<ApiResponse<Review>> getMovieReviews(@Path("id") long movieId);
}
