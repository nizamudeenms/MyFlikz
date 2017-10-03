package com.example.nizamudeenms.myflikz;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private SQLiteDatabase mMovieDb;

    final String GET_POPULAR = "popular";
    final String GET_TOP = "top_rated";
    public String sortBy = GET_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        mMovieDb = movieDbHelper.getWritableDatabase();

        Cursor cPopularMovies = getPopularMovies();
        Cursor cTopMovies = getTopMovies();

        if (sortBy.equals(GET_POPULAR)) {
            System.out.println("inside popular movies");
            mAdapter = new MovieAdapter(getApplicationContext(), cPopularMovies);
            recyclerView.setAdapter(mAdapter);
        }else if(sortBy.equals(GET_TOP)){
            System.out.println("inside top movies");
            mAdapter = new MovieAdapter(getApplicationContext(), cTopMovies);
            recyclerView.setAdapter(mAdapter);
        }



        Log.i(TAG, "Adapter Set");
        FetchMoviesTask fetchMovies = new FetchMoviesTask();
        fetchMovies.execute();


    }

    private Cursor getPopularMovies() {
        return mMovieDb.query(
                MovieContract.MovieEntry.POPULAR_MOVIE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor getTopMovies() {
        return mMovieDb.query(
                MovieContract.MovieEntry.TOP_MOVIE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

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
//                            Log.d(TAG, response.toString());
                            movies.clear();
                            try {
                                JSONArray responseBundle = response.getJSONArray("results");
                                for (int j = 0; j < responseBundle.length(); j++) {
                                    JSONObject c = responseBundle.getJSONObject(j);
                                    String posterPath = c.getString("poster_path");
                                    String backdropPath = c.getString("backdrop_path");
//                                Log.i(TAG, posterPath);
                                    Log.i(TAG, c.getString("original_title"));
                                    Log.i(TAG, c.getString("vote_average"));
                                    Log.i(TAG, c.getString("release_date"));

                                    Movie movie = new Movie();
                                    movie.setPOSTER_PATH(endpoint + "w185/" + posterPath);
                                    movie.setBACKDROP_PATH(endpoint + "w500/" + backdropPath);
                                    movie.setID(c.getString("id"));
                                    movie.setOVERVIEW(c.getString("overview"));
                                    movie.setRELEASE_DATE(c.getString("release_date"));
                                    movie.setTITLE(c.getString("original_title"));
                                    movie.setVOTE_AVERAGE(c.getString("vote_average"));
                                    movies.add(movie);

                                    if (sortBy.equals(GET_POPULAR)) {
                                        ContentValues cv = new ContentValues();
                                        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, endpoint + "w185/" + posterPath);
                                        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, endpoint + "w500/" + backdropPath);
                                        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, c.getString("original_title"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, c.getString("id"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, c.getString("overview"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, c.getString("release_date"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, c.getString("vote_average"));
                                        mMovieDb.insert(MovieContract.MovieEntry.POPULAR_MOVIE_TABLE, null, cv);
                                        System.out.println("Value inserted in Popular DB ");
                                    } else {
                                        ContentValues cv = new ContentValues();
                                        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, endpoint + "w185/" + posterPath);
                                        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, endpoint + "w500/" + backdropPath);
                                        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, c.getString("original_title"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, c.getString("id"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, c.getString("overview"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, c.getString("release_date"));
                                        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, c.getString("vote_average"));
                                        mMovieDb.insert(MovieContract.MovieEntry.TOP_MOVIE_TABLE, null, cv);
                                        System.out.println("Value inserted in Top DB ");
                                    }


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
            return null;
        }
    }


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
            FetchMoviesTask fetchMovies = new FetchMoviesTask();
            fetchMovies.execute();
            Cursor cPopularMovies = getPopularMovies();
            mAdapter = new MovieAdapter(getApplicationContext(), cPopularMovies);
            recyclerView.setAdapter(mAdapter);
            Context context = this;
            Toast toast = Toast.makeText(context, getString(R.string.popular_sel_mes), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
        } else if (menuSelected == R.id.topRated) {
            sortBy = GET_TOP;
            FetchMoviesTask fetchMovies = new FetchMoviesTask();
            fetchMovies.execute();
            Cursor cTopMovies = getTopMovies();
            mAdapter = new MovieAdapter(getApplicationContext(), cTopMovies);
            recyclerView.setAdapter(mAdapter);
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