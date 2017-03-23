package com.example.nizamudeenms.myflikz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "http://image.tmdb.org/t/p/";
    private ArrayList<String> images;
    private ProgressDialog pDialog;
    private MovieAdapter mAdapter;
    private String urlFromJson = null;
    private RecyclerView recyclerView;

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


        images = new ArrayList<>();
        mAdapter = new MovieAdapter(getApplicationContext(), images);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        fetchImages();
        Log.i(TAG, "Inside");



    }

    private void fetchImages() {
        final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String GET_POPULAR = "popular";
        final String GET_TOP = "top_rated";

//        pDialog.setMessage("Downloading json...");
//        pDialog.show();

        final String url = FORECAST_BASE_URL + GET_POPULAR + "?api_key="+BuildConfig.TMDB_KEY;


        JsonObjectRequest req =  new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                images.clear();
                    try {
                        JSONArray contacts = response.getJSONArray("results");
                        for (int j = 0; j < contacts.length(); j++) {
                            JSONObject c = contacts.getJSONObject(j);
                            String path = c.getString("poster_path");
                            Log.i(TAG, path);
                            images.add(endpoint+"w185/"+path);
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }

                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
//                images.add(pic);
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}