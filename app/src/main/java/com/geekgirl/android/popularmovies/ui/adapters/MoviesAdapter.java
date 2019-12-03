package com.geekgirl.android.popularmovies.ui.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekgirl.android.popularmovies.R;
import com.geekgirl.android.popularmovies.databinding.ItemMovieBinding;
import com.geekgirl.android.popularmovies.model.IMovie;

import java.util.List;

/**
 * Created by Rim Gazzah on 15/08/18
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<? extends IMovie> mMoviesList;
    private OnMovieClickListener mOnMovieClickListener;
    private Context mContext;

    public void setMoviesArrayList(final List<? extends IMovie> moviesArrayList) {
        mMoviesList = moviesArrayList;
        notifyDataSetChanged();
    }

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        mOnMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ItemMovieBinding itemMovieBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext),
                        R.layout.item_movie, parent, false);
        return new MoviesViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        IMovie movie = mMoviesList.get(position);
        holder.itemMovieBinding.setMovie(movie);
        holder.itemMovieBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMoviesList == null ? 0 : mMoviesList.size();
    }


    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemMovieBinding itemMovieBinding;

        public MoviesViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            itemMovieBinding = binding;
            itemMovieBinding.movieItemThumbnail.setOnClickListener(this::onClick);
            itemMovieBinding.movieItemLike.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            if (mOnMovieClickListener != null) {
                if (v == itemMovieBinding.movieItemThumbnail) {
                    mOnMovieClickListener.onMovieDetailClick(getAdapterPosition());
                } else if (v == itemMovieBinding.movieItemLike) {
                    mOnMovieClickListener.onMovieLikeClick(getAdapterPosition());
                }
            }
        }
    }

    public interface OnMovieClickListener {
        void onMovieDetailClick(int position);

        void onMovieLikeClick(int position);
    }
}
