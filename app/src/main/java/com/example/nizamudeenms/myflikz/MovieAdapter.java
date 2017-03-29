package com.example.nizamudeenms.myflikz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by nizamudeenms on 17/03/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movies;
    private Context mContext;
    private static final int LENGTH = 18;

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        this.movies = movies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.gallery_thumbnail;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

//        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
//        MyViewHolder viewHolder = new MyViewHolder(view);


        View itemView = LayoutInflater.from(mContext).inflate(R.layout.gallery_thumbnail,parent,false);
    return new MyViewHolder(itemView,mContext,movies);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movies.get(position);
        ImageView im = holder.thumbnail;
//        Log.i(TAG,image.toString());

//        Picasso.with(mContext)
//                .load(image).placeholder(R.mipmap.ic_launcher)
//                .into(holder.thumbnail);

        Glide.with(mContext).load(movie.getPOSTER_PATH()).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(im);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        List<Movie> movies ;
        Context context;

        public MyViewHolder(View view, Context mContext, List<Movie> movies) {
            super(view);
            this.movies = movies;
            this.context = mContext;
            view.setOnClickListener(this);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie currentMovie = this.movies.get(position);

            Intent intent = new Intent(this.context,DetailActivity.class);
            intent.putExtra("poster_url",currentMovie.getPOSTER_PATH());
            intent.putExtra("backdrop_url",currentMovie.getBACKDROP_PATH());
            intent.putExtra("id",currentMovie.getID());
            intent.putExtra("overview",currentMovie.getOVERVIEW());
            intent.putExtra("release_date",currentMovie.getRELEASE_DATE());
            intent.putExtra("results",currentMovie.getRESULTS());
            intent.putExtra("title",currentMovie.getTITLE());
            intent.putExtra("vote_average",currentMovie.getVOTE_AVERAGE());
            this.context.startActivity(intent);

//            v.getContext().startActivity(new Intent(v.getContext(), DetailActivity.class));

        }
    }




}
