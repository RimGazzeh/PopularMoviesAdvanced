package com.geekgirl.android.popularmovies.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.databinding.FragmentMovieDetailBinding;
import com.geekgirl.android.popularmovies.model.Movie;
import com.geekgirl.android.popularmovies.model.Review;
import com.geekgirl.android.popularmovies.model.Video;
import com.geekgirl.android.popularmovies.ui.adapters.ReviewAdapter;
import com.geekgirl.android.popularmovies.ui.adapters.VideoAdapter;
import com.geekgirl.android.popularmovies.viewModel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rim Gazzah on 21/08/18
 */
public class MovieDetailFragment extends Fragment implements ReviewAdapter.OnReviewClickListener, VideoAdapter.OnVideoClickListener {

    private static final String MOVIE_EXTRA = "movie_extra";
    Movie mMovie;
    ActionBar mActionBar;
    private AppViewModel mViewModel;
    private FragmentMovieDetailBinding mFragmentMovieDetailBinding;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private List<Video> mVideoList = new ArrayList<>();
    private List<Review> mReviewList = new ArrayList<>();


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
        mFragmentMovieDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false);
        getActivity().invalidateOptionsMenu();
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("");
        initView();
        initData();
        initEvent();
        mFragmentMovieDetailBinding.setMovie(mMovie);
        return mFragmentMovieDetailBinding.getRoot();
    }

    private void initView() {
        mFragmentMovieDetailBinding.movieListVideos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mFragmentMovieDetailBinding.movieListVideos.setHasFixedSize(true);
        mFragmentMovieDetailBinding.movieListVideos.setNestedScrollingEnabled(false);
        mVideoAdapter = new VideoAdapter();
        mFragmentMovieDetailBinding.movieListVideos.setAdapter(mVideoAdapter);

        mFragmentMovieDetailBinding.movieListReviews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mFragmentMovieDetailBinding.movieListReviews.setHasFixedSize(true);
        mFragmentMovieDetailBinding.movieListReviews.setNestedScrollingEnabled(false);
        mReviewAdapter = new ReviewAdapter();
        mFragmentMovieDetailBinding.movieListReviews.setAdapter(mReviewAdapter);
    }

    private void initData() {
        mViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(MOVIE_EXTRA);
        }

        MutableLiveData<List<Video>> videoLiveData = mViewModel.getMovieVideos(mMovie.getId());
        videoLiveData.observe(this, (List<Video> videoList) -> {
            mVideoList = videoList;
            mFragmentMovieDetailBinding.videosTitle.setVisibility(mVideoList.size() > 0 ? View.VISIBLE : View.GONE);
            mVideoAdapter.setVideosArrayList(mVideoList);
        });

        MutableLiveData<List<Review>> reviewLiveData = mViewModel.getMovieReviews(mMovie.getId());
        reviewLiveData.observe(this, (List<Review> reviewList) -> {
            mReviewList = reviewList;
            mFragmentMovieDetailBinding.reviewsTitle.setVisibility(mReviewList.size() > 0 ? View.VISIBLE : View.GONE);
            mReviewAdapter.setReviewList(mReviewList);
        });

    }

    public void initEvent() {
        mReviewAdapter.setOnReviewClickListener(this);
        mVideoAdapter.setOnVideoClickListener(this);
    }


    @Override
    public void onReviewOpenClick(int position) {
        mReviewAdapter.notifyItemChanged(position);
    }

    @Override
    public void onVideoClick(String videoUrl) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + videoUrl));

        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + videoUrl));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
