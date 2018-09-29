package com.geekgirl.android.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.geekgirl.android.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Rim Gazzah on 11/09/18
 */

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movie" )
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    Movie getMovieById(String id);
}
