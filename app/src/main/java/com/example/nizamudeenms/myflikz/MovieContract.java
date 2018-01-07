package com.example.nizamudeenms.myflikz;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nizamudeenms on 08/07/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.nizamudeenms.myflikz";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String POPULAR_MOVIE_TABLE = "popular_movies_table";
        public static final String TOP_MOVIE_TABLE = "top_movies_table";
        public static final String FAV_MOVIE_TABLE = "fav_movies_table";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_FAVORITE = "favorite";

    }


}
