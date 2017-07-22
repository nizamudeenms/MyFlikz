package com.example.nizamudeenms.myflikz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nizamudeenms on 05/07/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>  {
  ArrayList<Review> reviewArrayList;
    Context context;

    public ReviewAdapter(ArrayList<Review> reviewsList) {
        this.reviewArrayList = reviewsList;
    }


    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_review,parent,false);
        return new ReviewHolder(view,reviewArrayList);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviewArrayList.get(position);
        holder.reviewerName.setText(review.getReviewerName());
        holder.reviewContent.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView reviewerName;
        public TextView reviewContent;
        ArrayList<Review> reviewArrayList;

        public ReviewHolder(View itemView, ArrayList<Review> reviewArrayList) {
            super(itemView);
            this.reviewArrayList = reviewArrayList;
            reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name_textview);
            reviewContent = (TextView) itemView.findViewById(R.id.review_textview);
        }

        @Override
        public void onClick(View v) {
            Toast toast = new Toast(context);
            toast.setText("Cliked");
            toast.show();
        }
    }
}
