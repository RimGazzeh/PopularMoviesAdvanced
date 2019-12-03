package com.geekgirl.android.popularmovies.service

import com.geekgirl.android.popularmovies.model.ApiResponse
import com.geekgirl.android.popularmovies.model.Movie
import com.geekgirl.android.popularmovies.model.Review
import com.geekgirl.android.popularmovies.model.Video
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Rim Gazzah on 2019-12-03.
 **/
interface ApiService {

    @GET(POPULAR_URL)
    fun getPopularMovies(@Query(PAGE_QUERY) page: String):Observable<ApiResponse<Movie>>

    @GET(TOP_RATED_URL)
    fun getMostRatedMovies(@Query(PAGE_QUERY) page: String):Observable<ApiResponse<Movie>>


    @GET("{$ID_QUERY}/$VIDEO_URL")
    fun getMovieVideos(@Path(ID_QUERY) movieId: Long):Observable<ApiResponse<Video>>

    @GET("{$ID_QUERY}/$REVIEW_URL")
    fun getMovieReviews(@Path(ID_QUERY) movieId: Long):Observable<ApiResponse<Review>>


}