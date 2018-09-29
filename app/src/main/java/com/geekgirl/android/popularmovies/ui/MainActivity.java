package com.geekgirl.android.popularmovies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.data.AppDatabase;
import com.geekgirl.android.popularmovies.databinding.ActivityMainBinding;
import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.service.AppExecutors;
import com.geekgirl.android.popularmovies.service.NetworkUtils;
import com.geekgirl.android.popularmovies.ui.adapters.MoviesAdapter;
import com.geekgirl.android.popularmovies.utils.Logger;
import com.geekgirl.android.popularmovies.utils.Prefs;
import com.geekgirl.android.popularmovies.viewModel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener {

    private List<Movie> mMovieList = new ArrayList<>();
    private List<Movie> mMovieFavList = new ArrayList<>();
    private MoviesAdapter mMoviesAdapter = new MoviesAdapter();
    private MovieDetailFragment mMovieDetailFragment;
    private AppViewModel mViewModel;
    private ActivityMainBinding mBinding;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mBinding.recyclerviewMovies.setLayoutManager(mLayoutManager);
        mBinding.recyclerviewMovies.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerviewMovies.setHasFixedSize(true);
        mBinding.recyclerviewMovies.setAdapter(mMoviesAdapter);
    }

    private void initData() {
        mDb = AppDatabase.getInstance(getApplicationContext());
        mViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        mViewModel.getFavoritesMovies().observe(this, (List<Movie> movieList) -> {
            mMovieFavList = movieList;
            getMoviesList();
        });
    }


    private List<Movie> setFavoritesToList(int selectedOption, List<Movie> movieList) {
        if (selectedOption == Prefs.FAVORITES_VALUE) {
            mMovieFavList = movieList;
            return movieList;
        }
        if (mMovieFavList == null || mMovieFavList.isEmpty()) {
            return movieList;
        }
        List<Movie> resultList = new ArrayList<>();
        for (Movie movie : movieList) {
            if (mMovieFavList.contains(movie)) {
                movie.setFavorite(true);
            }
            resultList.add(movie);
        }
        return resultList;
    }

    private void getMoviesList() {
        setToolbarTitle();
        mViewModel.getFavoritesMovies().removeObservers(this);
        mViewModel.getMoviesByPopularity().removeObservers(this);
        mViewModel.getMoviesByRating().removeObservers(this);
        int selectedOption = Prefs.FAVORITES_VALUE;
        if (NetworkUtils.isNetworkConnected(this)) {
            selectedOption = Prefs.getPref(Prefs.SORT_OPTION, this, Prefs.MOST_POPULAR_VALUE);
        }
        LiveData<List<Movie>> listMutableLiveData;
        switch (selectedOption) {

            case Prefs.MOST_POPULAR_VALUE: {
                listMutableLiveData = mViewModel.getMoviesByPopularity();
            }
            break;
            case Prefs.TOP_RATED_VALUE: {
                listMutableLiveData = mViewModel.getMoviesByRating();
            }
            break;
            default: {
                listMutableLiveData = mViewModel.getFavoritesMovies();
            }
            break;
        }
        int finalSelectedOption = selectedOption;
        listMutableLiveData.observe(this, (List<Movie> movieList) -> {
            mMovieList = setFavoritesToList(finalSelectedOption, movieList);
            Logger.d("mMovieList size = " + mMovieList.size());
            Logger.d("mMovieList " + mMovieList.toString());
            mMoviesAdapter.setMoviesArrayList(mMovieList);
            showErrorMessage(mMovieList.size() == 0);
        });

    }


    public void initEvent() {
        mMoviesAdapter.setOnMovieClickListener(this);
    }


    private boolean isUpEnabled() {
        return (getSupportActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0;
    }

    @Override
    public void onBackPressed() {
        if (isUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setToolbarTitle();
            super.onBackPressed();
            invalidateOptionsMenu();
        } else {
            finish();
        }
    }

    private void setToolbarTitle() {
        int selectedOption = Prefs.getPref(Prefs.SORT_OPTION, this, Prefs.MOST_POPULAR_VALUE);

        switch (selectedOption) {
            case Prefs.MOST_POPULAR_VALUE: {
                getSupportActionBar().setTitle(getString(R.string.most_popular_option_menu) + " Movies");
            }
            break;
            case Prefs.TOP_RATED_VALUE: {
                getSupportActionBar().setTitle(getString(R.string.top_rated_option_menu) + " Movies");
            }
            break;
            default: {
                getSupportActionBar().setTitle(getString(R.string.favorites_option_menu) + " Movies");
            }
            break;
        }
    }

    private void showErrorMessage(boolean show) {
        if (show == true) {
            mBinding.recyclerviewMovies.setVisibility(View.GONE);
            mBinding.statusContainer.setVisibility(View.VISIBLE);
        } else {
            mBinding.statusContainer.setVisibility(View.GONE);
            mBinding.recyclerviewMovies.setVisibility(View.VISIBLE);
        }
    }


    private void goToMovieDetailFragment(Movie movie) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mMovieDetailFragment = MovieDetailFragment.newInstance(movie);
        fragmentTransaction.add(R.id.activity_main_container, mMovieDetailFragment, "MovieDetailFragment").addToBackStack("MovieDetailFragment");
        fragmentTransaction.commit();
    }


    @Override
    public void onMovieDetailClick(int position) {
        Movie movie = mMovieList.get(position);
        if (movie != null) {
            goToMovieDetailFragment(movie);
        }
    }

    @Override
    public void onMovieLikeClick(int position) {
        Movie clickedMovie = mMovieList.get(position);
        if (clickedMovie.isFavorite()) {
            clickedMovie.setFavorite(false);
            AppExecutors.getInstance().diskIO().execute(() -> {
                mDb.movieDAO().deleteMovie(clickedMovie);
            });

        } else {
            clickedMovie.setFavorite(true);
            AppExecutors.getInstance().diskIO().execute(() -> {
                mDb.movieDAO().insertMovie(clickedMovie);
            });
        }
        int selectedOption = Prefs.getPref(Prefs.SORT_OPTION, this, Prefs.MOST_POPULAR_VALUE);
        if (selectedOption == Prefs.FAVORITES_VALUE) {
            mMoviesAdapter.notifyDataSetChanged();
        } else {
            mMoviesAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSort = menu.findItem(R.id.menu_sort);
        MenuItem itemRefresh = menu.findItem(R.id.menu_refresh);
        if (isUpEnabled()) {
            itemSort.setVisible(false);
            itemRefresh.setVisible(false);
        } else {
            itemSort.setVisible(true);
            itemRefresh.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void refreshView(int selectedOption) {
        if (NetworkUtils.isNetworkConnected(this)) {
            switch (selectedOption) {
                case Prefs.MOST_POPULAR_VALUE: {
                    Prefs.setPref(Prefs.SORT_OPTION, Prefs.MOST_POPULAR_VALUE, this);
                }
                break;
                case Prefs.TOP_RATED_VALUE: {
                    Prefs.setPref(Prefs.SORT_OPTION, Prefs.TOP_RATED_VALUE, this);
                }
                break;
                case Prefs.FAVORITES_VALUE: {
                    Prefs.setPref(Prefs.SORT_OPTION, Prefs.FAVORITES_VALUE, this);
                }
                break;
            }
        }else {
            Toast.makeText(this, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
        }
        getMoviesList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_refresh: {
                getMoviesList();
            }
            break;
            case R.id.top_rated_option: {
                refreshView(Prefs.TOP_RATED_VALUE);

            }
            break;
            case R.id.most_popular_option: {
                refreshView(Prefs.MOST_POPULAR_VALUE);
            }
            break;

            case R.id.favorites_option: {
                refreshView(Prefs.FAVORITES_VALUE);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
