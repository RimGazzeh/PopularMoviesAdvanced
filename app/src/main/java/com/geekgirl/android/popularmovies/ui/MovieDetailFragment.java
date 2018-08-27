package com.geekgirl.android.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rim Gazzah on 21/08/18
 */
public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_EXTRA = "movie_extra";
    @BindView(R.id.movie_thumbnail)
    ImageView mThumbnail;
    @BindView(R.id.movie_synopsis)
    TextView mMovieSynopsis;
    @BindView(R.id.movie_ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.movie_rating)
            TextView mRating;
    @BindView(R.id.movie_release_date)
            TextView mReleaseDate;
    @BindView(R.id.movie_title)
            TextView mTitle;
    Movie mMovies;
    ActionBar mActionBar;


    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_EXTRA, movie);
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        getActivity().invalidateOptionsMenu();
        mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        initData();
        initView();
        return rootView;
    }

    private void initData(){
        if (getArguments() != null) {
            mMovies = getArguments().getParcelable(MOVIE_EXTRA);
        }
    }

    private void initView(){
        if(!TextUtils.isEmpty(mMovies.getPosterPath())){
            Picasso.with(getActivity()).load(mMovies.getPosterPath()).into(mThumbnail);
        }
        mActionBar.setTitle("");
        mTitle.setText(mMovies.getTitle());
        mReleaseDate.setText(mMovies.getReleaseDate());
        mMovieSynopsis.setText(mMovies.getOverview());
        mRatingBar.setRating(mMovies.getVoteAverage().floatValue());
        mRating.setText(String.valueOf(mMovies.getVoteAverage()));
    }
}
