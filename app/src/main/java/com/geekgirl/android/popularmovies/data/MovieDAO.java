package com.geekgirl.android.popularmovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
