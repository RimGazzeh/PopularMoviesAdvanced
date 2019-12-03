package com.geekgirl.android.popularmovies.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.geekgirl.android.popularmovies.data.AppDatabase;
import com.geekgirl.android.popularmovies.model.ApiResponse;
import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.model.Review;
import com.geekgirl.android.popularmovies.model.Video;
import com.geekgirl.android.popularmovies.service.ServiceInitiator;
import com.geekgirl.android.popularmovies.service.Webservice;
import com.geekgirl.android.popularmovies.utils.Logger;
import com.geekgirl.android.popularmovies.utils.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rim Gazzah on 11/09/18
 */
public class AppViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private Webservice webservice;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> mostRatedMovies;
    private MutableLiveData<List<Video>> movieVideos;
    private MutableLiveData<List<Review>> movieReviews;
    private LiveData<List<Movie>> movieFavorites;


    public AppViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        webservice = ServiceInitiator.createService();
    }

    public MutableLiveData<List<Movie>> getMoviesByPopularity() {
        if (popularMovies == null) {
            popularMovies = new MutableLiveData<>();
            loadMoviesByPopularity(1);
        }

        return popularMovies;
    }

    public MutableLiveData<List<Movie>> getMoviesByRating() {
        if (mostRatedMovies == null) {
            mostRatedMovies = new MutableLiveData<>();
            loadMoviesByRating(1);
        }

        return mostRatedMovies;
    }

    public LiveData<List<Movie>> getFavoritesMovies() {
        if (movieFavorites == null) {
            movieFavorites = new MutableLiveData<>();
            loadFavoritesMovies();
        }

        return movieFavorites;
    }

    public MutableLiveData<List<Video>> getMovieVideos(long idVideo) {
        if (movieVideos == null) {
            movieVideos = new MutableLiveData<>();
            loadMovieVideos(idVideo);
        }
        return movieVideos;
    }


    public MutableLiveData<List<Review>> getMovieReviews(long idVideo) {
        if (movieReviews == null) {
            movieReviews = new MutableLiveData<>();
            loadMovieReviews(idVideo);
        }
        return movieReviews;
    }

    private void loadFavoritesMovies() {
        movieFavorites = appDatabase.movieDAO().getAllMovies();
    }

    public void refreshListMovies(int type, int page){
        switch (type) {

            case Prefs.MOST_POPULAR_VALUE: {
                loadMoviesByPopularity(page);
            }
            break;
            case Prefs.TOP_RATED_VALUE: {
                loadMoviesByRating(page);
            }
            break;
        }
    }

    private void loadMoviesByPopularity(int page) {
        Call<ApiResponse<Movie>> call = webservice.getPopularMovies(String.valueOf(page));
        call.enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> resultsMovies = response.body().getListData();
                    Logger.d("success" + resultsMovies.size());
                    List<Movie> movieList = popularMovies.getValue();
                    if (movieList == null || movieList.isEmpty()) {
                        popularMovies.setValue(resultsMovies);
                    } else {
                        movieList.addAll(resultsMovies);
                        popularMovies.setValue(movieList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                Logger.d(t.getMessage() + "cause= " + t.getCause());
                popularMovies = null;
            }
        });
    }


    private void loadMoviesByRating(int page) {
        Call<ApiResponse<Movie>> call = webservice.getMostRatedMovies(String.valueOf(page));
        call.enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> resultsMovies = response.body().getListData();
                    Logger.d(response.body().toString());
                    List<Movie> movieList = mostRatedMovies.getValue();
                    if (movieList == null || movieList.isEmpty()) {
                        mostRatedMovies.setValue(resultsMovies);
                    } else {
                        movieList.addAll(resultsMovies);
                        mostRatedMovies.setValue(movieList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                mostRatedMovies = null;
            }
        });
    }


    private void loadMovieVideos(long idVideo) {
        Call<ApiResponse<Video>> call = webservice.getMovieVideos(idVideo);
        call.enqueue(new Callback<ApiResponse<Video>>() {
            @Override
            public void onResponse(Call<ApiResponse<Video>> call, Response<ApiResponse<Video>> response) {
                if (response.isSuccessful()) {
                    List<Video> resultsVideos = response.body().getListData();
                    Logger.d(response.body().toString());
                    movieVideos.setValue(resultsVideos);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Video>> call, Throwable t) {
                movieVideos = null;
            }
        });
    }

    private void loadMovieReviews(long idVideo) {
        Call<ApiResponse<Review>> call = webservice.getMovieReviews(idVideo);
        call.enqueue(new Callback<ApiResponse<Review>>() {
            @Override
            public void onResponse(Call<ApiResponse<Review>> call, Response<ApiResponse<Review>> response) {
                if (response.isSuccessful()) {
                    List<Review> resultsReviews = response.body().getListData();
                    Logger.d(response.body().toString());
                    movieReviews.setValue(resultsReviews);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Review>> call, Throwable t) {
                movieReviews = null;
            }
        });
    }


}
