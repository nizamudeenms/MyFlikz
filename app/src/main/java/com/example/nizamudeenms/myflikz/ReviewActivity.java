package com.example.nizamudeenms.myflikz;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nizamudeenms on 05/07/17.
 */

public class ReviewActivity extends AppCompatActivity {
    ArrayList<Review> reviewsList = new ArrayList<Review>();
    String MOVIE_ID = "";
    ReviewAdapter mReviewAdapter;
    RecyclerView recyclerViewReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        MOVIE_ID = getIntent().getStringExtra("id");

        recyclerViewReview = (RecyclerView) findViewById(R.id.recycler_view_review);
        recyclerViewReview.setHasFixedSize(true);
        recyclerViewReview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));

        ReviewFetchTask  reviewFetchTask = new ReviewFetchTask();
        reviewFetchTask.execute();

        mReviewAdapter = new ReviewAdapter(reviewsList);
        recyclerViewReview.setAdapter(mReviewAdapter);


    }

    public class ReviewFetchTask extends AsyncTask<Void, Void, Void>{
        Context mContext;
        String FINAL_REVIEW_URL="";

        @Override
        protected Void doInBackground(Void... params) {
            final String REVIEW_BASE_URL = "https://api.themoviedb.org/3/movie/";

            FINAL_REVIEW_URL = REVIEW_BASE_URL + MOVIE_ID +"/reviews?api_key=" + BuildConfig.TMDB_KEY;

            Log.i("URL",FINAL_REVIEW_URL);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FINAL_REVIEW_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray responseBundle = response.getJSONArray("results");
                        for (int j = 0; j < responseBundle.length(); j++) {
                            JSONObject reviewResponse = responseBundle.getJSONObject(j);
                            Review review = new Review();
                            review.setReviewId(reviewResponse.getString("id"));
                            review.setReviewerName(reviewResponse.getString("author"));
                            review.setReviewContent(reviewResponse.getString("content"));
                            review.setReviewUrl(reviewResponse.getString("url"));
                            reviewsList.add(review);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mReviewAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = null;
                    if (volleyError instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                    }

                    Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
                    toast.show();
                    volleyError.printStackTrace();
                }
            });
            MovieController.getInstance().addToRequestQueue(jsonObjectRequest);
            return null;
        }
    }
}
