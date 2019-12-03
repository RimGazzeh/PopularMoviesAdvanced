package com.geekgirl.android.popularmovies.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekgirl.android.popularmovies.data.AppDatabase
import com.geekgirl.android.popularmovies.model.Movie
import com.geekgirl.android.popularmovies.model.Review
import com.geekgirl.android.popularmovies.model.Video
import com.geekgirl.android.popularmovies.service.ApiService
import com.geekgirl.android.popularmovies.service.AppExecutors
import com.geekgirl.android.popularmovies.service.ServiceInitiator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Rim Gazzah on 2019-12-03.
 **/
class MoviesviewModel(application: Application) : BaseViewModel(application) {

    private val mAppDatabase: AppDatabase
    private val mApiService: ApiService
    private var mPopularMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var mMostRatedMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var mMovieFavorites: LiveData<List<Movie>> = MutableLiveData()
    private var mMoviesErrorState: MutableLiveData<Throwable?> = MutableLiveData()
    private var mMovieVideos: MutableLiveData<List<Video>> = MutableLiveData()
    private var mMovieReviews: MutableLiveData<List<Review>> = MutableLiveData()

    init {
        mAppDatabase = AppDatabase.getInstance(application)
        mApiService = ServiceInitiator.createService()
        mMovieFavorites = mAppDatabase.movieDAO().getAllMovies()
    }

    fun loadMoviesByPopularity(page: String) {
        addDisposable(
                mApiService.getPopularMovies(page).subscribeOn(Schedulers.from(AppExecutors.getInstance().diskIO()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mPopularMovies.postValue(it.listData)
                        }, {
                            mMoviesErrorState.postValue(it)
                        })
        )
    }


    fun loadMoviesByRating(page: String) {
        addDisposable(
                mApiService.getMostRatedMovies(page).subscribeOn(Schedulers.from(AppExecutors.getInstance().diskIO()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMostRatedMovies.postValue(it.listData)
                        }, {
                            mMoviesErrorState.postValue(it)
                        })
        )
    }


    fun loadMoviesVideo(idVideo: Long) {
        addDisposable(
                mApiService.getMovieVideos(idVideo).subscribeOn(Schedulers.from(AppExecutors.getInstance().diskIO()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMovieVideos.postValue(it.listData)
                        }, {})
        )
    }

    fun loadMoviesReviews(idMovie: Long) {
        addDisposable(
                mApiService.getMovieReviews(idMovie).subscribeOn(Schedulers.from(AppExecutors.getInstance().diskIO()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mMovieReviews.postValue(it.listData)
                        }, {})
        )
    }
}