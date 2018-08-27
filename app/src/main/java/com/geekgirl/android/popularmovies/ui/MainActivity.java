package com.geekgirl.android.popularmovies.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.utils.NetworkUtils;
import com.geekgirl.android.popularmovies.utils.Prefs;
import com.geekgirl.android.popularmovies.utils.UITools;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MoviesAdapter.OnMovieClickListener {

    private static final String QUERY_URL_EXTRA = "query_url_extra";

    private static final int MOVIES_QUERY_LOADER = 25;

    @BindView(R.id.activity_main_movies_rv)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.activity_main_no_network_container)
    RelativeLayout mNoInternetConnectionMsg;
    @BindView(R.id.activity_main_status_container)
    RelativeLayout mStatusContainer;
    @BindView(R.id.activity_main_loader)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.activity_main_error_message)
    TextView mErrorMessage;
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private MoviesAdapter mMoviesAdapter;
    private MovieDetailFragment mMovieDetailFragment;
    private URL mQueryUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        refreshUI();
    }

    private void initView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(mLayoutManager);
        mMoviesRecyclerView.addItemDecoration(new UITools.GridSpacingItemDecoration(2, UITools.dpToPx(10, this), true));
        mMoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesAdapter = new MoviesAdapter(this, mMovieList, this);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
    }

    public void refreshUI() {
        if (NetworkUtils.isNetworkConnected(this)) {
            mNoInternetConnectionMsg.setVisibility(View.GONE);
            mMoviesRecyclerView.setVisibility(View.VISIBLE);
            makeMoviesQuery();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mMoviesRecyclerView.setVisibility(View.GONE);
            mNoInternetConnectionMsg.setVisibility(View.VISIBLE);
        }
    }


    private void makeMoviesQuery() {
        mQueryUrl = NetworkUtils.getUrlQuery(this);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_URL_EXTRA, mQueryUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> queryLoader = loaderManager.getLoader(MOVIES_QUERY_LOADER);
        if (queryLoader == null) {
            loaderManager.initLoader(MOVIES_QUERY_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_QUERY_LOADER, queryBundle, this);
        }
    }

    private boolean isUpEnabled() {
        return (getSupportActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0;
    }

    @Override
    public void onBackPressed() {
        if (isUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.app_name));
            super.onBackPressed();
            invalidateOptionsMenu();
        } else {
            finish();
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        showLoadingIndicator();
        return new MoviesAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

        mLoadingIndicator.setVisibility(View.GONE);

        if (null == data) {
            showErrorMessage();
        } else {
            mStatusContainer.setVisibility(View.GONE);
            mMovieList.clear();
            mMovieList.addAll(data);
            mMoviesAdapter.notifyDataSetChanged();
        }
    }

    private void showErrorMessage() {
        mStatusContainer.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        mStatusContainer.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    private void goToMovieDetailFragment(Movie movie) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mMovieDetailFragment = MovieDetailFragment.newInstance(movie);
        fragmentTransaction.add(R.id.activity_main_container, mMovieDetailFragment, "MovieDetailFragment").addToBackStack("MovieDetailFragment");
        fragmentTransaction.commit();
    }


    @Override
    public void onMovieClick(int position) {
        Movie movie = mMovieList.get(position);
        if (movie != null) {
            goToMovieDetailFragment(movie);
        }
    }

    public static class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

        Bundle args;
        List<Movie> movieArrayList;


        public MoviesAsyncTaskLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
        }

        @Override
        protected void onStartLoading() {
            if (args == null) {
                return;
            }

            if (movieArrayList != null) {
                deliverResult(movieArrayList);
            } else {
                forceLoad();
            }

        }

        @Nullable
        @Override
        public List<Movie> loadInBackground() {

            String queryUrlString = args.getString(QUERY_URL_EXTRA);

            if (queryUrlString == null || TextUtils.isEmpty(queryUrlString)) {
                return null;
            }

            try {
                URL movieQueryUrl = new URL(queryUrlString);
                String movieQueryResult = NetworkUtils.getResponseFromHttpUrl(movieQueryUrl);
                return NetworkUtils.parseMoviesData(movieQueryResult);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public void deliverResult(List<Movie> data) {
            movieArrayList = data;
            super.deliverResult(data);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_refresh: {
                refreshUI();
            }
            break;
            case R.id.top_rated_option: {
                Prefs.setPref(Prefs.SORT_OPTION, Prefs.TOP_RATED_VALUE, this);
                makeMoviesQuery();
            }
            break;
            case R.id.most_popular_option: {
                Prefs.setPref(Prefs.SORT_OPTION, Prefs.MOST_POPULAR_VALUE, this);
                makeMoviesQuery();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
