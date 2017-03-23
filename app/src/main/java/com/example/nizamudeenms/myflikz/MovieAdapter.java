package com.example.nizamudeenms.myflikz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private List<String> images;
    private Context mContext;
    private static final int LENGTH = 18;

    public MovieAdapter(Context context, List<String> images) {
        mContext = context;
        this.images = images;
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
    return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String image = images.get(position);
        ImageView im = holder.thumbnail;
        Log.i(TAG,image);

//        Picasso.with(mContext)
//                .load(image).placeholder(R.mipmap.ic_launcher)
//                .into(holder.thumbnail);

        Glide.with(mContext).load(image).placeholder(R.mipmap.ic_launcher).crossFade().thumbnail(0.5f).into(im);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


}
