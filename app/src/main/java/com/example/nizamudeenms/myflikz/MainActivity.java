package com.example.nizamudeenms.myflikz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "http://image.tmdb.org/t/p/";
    private ArrayList<Movie> movies;
    private ProgressDialog pDialog;
    private MovieAdapter mAdapter;
    private String urlFromJson = null;
    private RecyclerView recyclerView;

    final String GET_POPULAR = "popular";
    final String GET_TOP = "top_rated";
    public String sortBy = GET_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
//
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);


//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mAdapter = new MovieAdapter(getApplicationContext(), urlFromJson);
//        Log.i(TAG ,mAdapter.toString());


        movies = new ArrayList<>();
        mAdapter = new MovieAdapter(getApplicationContext(), movies);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
//        fetchImages();
        Log.i(TAG, "Inside");
        FetchMoviesTask fetchMovies = new FetchMoviesTask();
        fetchMovies.execute();

    }


    public class FetchMoviesTask extends AsyncTask<Void, Void ,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/movie/";

            String url = FORECAST_BASE_URL + GET_POPULAR + "?api_key=" + BuildConfig.TMDB_KEY;

//        pDialog.setMessage("Downloading json...");
//        pDialog.show();

            if (sortBy.equals(GET_POPULAR)) {
                url = FORECAST_BASE_URL + GET_POPULAR + "?api_key=" + BuildConfig.TMDB_KEY;
            } else if (sortBy.equals(GET_TOP)) {
                url = FORECAST_BASE_URL + GET_TOP + "?api_key=" + BuildConfig.TMDB_KEY;
            } else {
                url = FORECAST_BASE_URL + GET_POPULAR + "?api_key=" + BuildConfig.TMDB_KEY;
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            movies.clear();
                            try {
                                JSONArray responseBundle = response.getJSONArray("results");
                                for (int j = 0; j < responseBundle.length(); j++) {
                                    JSONObject c = responseBundle.getJSONObject(j);
                                    String posterPath = c.getString("poster_path");
                                    String backdropPath = c.getString("backdrop_path");
//                                Log.i(TAG, posterPath);
                                    Log.i(TAG,c.getString("original_title"));
                                    Log.i(TAG,c.getString("vote_average"));
                                    Log.i(TAG,c.getString("release_date"));

                                    Movie movie = new Movie();
                                    movie.setPOSTER_PATH(endpoint + "w185/" + posterPath);
                                    movie.setBACKDROP_PATH(endpoint + "w500/"+backdropPath);
                                    movie.setID(c.getString("id"));
                                    movie.setOVERVIEW(c.getString("overview"));
                                    movie.setRELEASE_DATE(c.getString("release_date"));
                                    movie.setTITLE(c.getString("original_title"));
                                    movie.setVOTE_AVERAGE(c.getString("vote_average"));
                                    movies.add(movie);
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }

                            mAdapter.notifyDataSetChanged();
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

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                    volleyError.printStackTrace();
                }
            });
            MovieController.getInstance().addToRequestQueue(req);
//        Log.i(TAG, url);
//        try{
//            JSONObject jo  = new JSONObject(url);
//            JSONArray weatherArray = jo.getJSONArray("result");
//
//            for(int i = 0; i < weatherArray.length(); i++) {
//                String pic = null;
//                JSONObject pics = weatherArray.getJSONObject(i);
//                pic = pics.getString("poster_path");
//                Log.i(TAG,pic);
//                movies.add(pic);
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
            return null;
        }
    }


//    private void fetchImages() {
//
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuSelected = item.getItemId();

        if (menuSelected == R.id.popular) {
            sortBy = GET_POPULAR;
//            fetchImages();
            FetchMoviesTask fetchMovies = new FetchMoviesTask();
            fetchMovies.execute();
            Context context = this;
            Toast toast = Toast.makeText(context, getString(R.string.popular_sel_mes), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
        } else if (menuSelected == R.id.topRated) {
            sortBy = GET_TOP;
//            fetchImages();
            FetchMoviesTask fetchMovies = new FetchMoviesTask();
            fetchMovies.execute();
            Context context = this;
            Toast toast = Toast.makeText(context, getString(R.string.top_rated_sel_msg), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
        }
        return true;
    }


//    @Override
//    public void onClick(String weatherForDay) {
//        Context context = this;
//        Intent intent = new Intent(this,DetailActivity.class);
//        Toast.makeText(context, " Activity Launched", Toast.LENGTH_SHORT).show();
//        startActivity(intent);
//    }
}