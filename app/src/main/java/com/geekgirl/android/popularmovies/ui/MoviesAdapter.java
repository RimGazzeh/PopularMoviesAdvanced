package com.geekgirl.android.popularmovies.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rim Gazzah on 15/08/18
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> mMoviesArrayList;
    private Context mContext;
    private OnMovieClickListener mOnMovieClickListener;

    public MoviesAdapter(Context context, List<Movie> movieArrayList, OnMovieClickListener onMovieClickListener) {
        mMoviesArrayList = movieArrayList;
        mContext = context;
        mOnMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Movie movie = mMoviesArrayList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        if (!TextUtils.isEmpty(movie.getPosterPath())) {
            Picasso.with(mContext).load(movie.getPosterPath()).into(holder.movieThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mMoviesArrayList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView movieThumbnail;
        private TextView movieTitle;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            movieThumbnail = itemView.findViewById(R.id.movie_item_thumbnail);
            movieTitle = itemView.findViewById(R.id.movie_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnMovieClickListener != null) {
                mOnMovieClickListener.onMovieClick(getAdapterPosition());
            }
        }
    }

    public interface OnMovieClickListener {
        void onMovieClick(int position);
    }
}
