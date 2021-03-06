package com.example.nizamudeenms.myflikz;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

public class VideoActivity extends AppCompatActivity {
    YouTubePlayerFragment youTubePlayerFragment;
    RecyclerView recyclerviewVideo;
    Vector<Video> youtubeVideos = new Vector<Video>();
    String MOVIE_ID = "";
    VideoAdapter mVideoAdapter;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        MOVIE_ID = getIntent().getStringExtra("id");


        recyclerviewVideo = (RecyclerView) findViewById(R.id.recycler_view_video);
        recyclerviewVideo.setHasFixedSize(true);
        recyclerviewVideo.setItemAnimator(new DefaultItemAnimator());
        recyclerviewVideo.setLayoutManager(new LinearLayoutManager(this));


        mVideoAdapter = new VideoAdapter(youtubeVideos);
        recyclerviewVideo.setAdapter(mVideoAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        VideoFetchTask videoFetchTask = new VideoFetchTask();
        videoFetchTask.execute();
    }


    public class VideoFetchTask extends AsyncTask<Void, Void, Void> {

        Context mContext;
        ArrayList<Video> videosList = new ArrayList<Video>() {
        };
        String FINAL_URL = "";

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... params) {
            final String VIDEO_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String FIRST_VIDEO_URL = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/";
            final String SECOND_VIDEO_URL = "\" frameborder=\"0\" allowfullscreen></iframe>";
            String url = VIDEO_BASE_URL + MOVIE_ID + "/videos?api_key=" + BuildConfig.TMDB_KEY;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray responseBundle = response.getJSONArray("results");
                                for (int j = 0; j < responseBundle.length(); j++) {
                                    JSONObject videoResponse = responseBundle.getJSONObject(j);
                                    Video video = new Video();
                                    video.setId(videoResponse.getString("id"));
                                    video.setIso_639_1(videoResponse.getString("iso_639_1"));
                                    video.setIso_3166_1(videoResponse.getString("iso_3166_1"));
                                    video.setKey(videoResponse.getString("key"));
                                    video.setName(videoResponse.getString("name"));
                                    video.setSite(videoResponse.getString("site"));
                                    video.setSize(videoResponse.getString("size"));
                                    video.setType(videoResponse.getString("type"));
                                    videosList.add(video);
                                    FINAL_URL = FIRST_VIDEO_URL + videoResponse.getString("key") + SECOND_VIDEO_URL;
                                    youtubeVideos.add(new Video(FINAL_URL));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mVideoAdapter.notifyDataSetChanged();
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }


}
