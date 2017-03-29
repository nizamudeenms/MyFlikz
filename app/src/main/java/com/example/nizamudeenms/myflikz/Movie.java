package com.example.nizamudeenms.myflikz;

/**
 * Created by nizamudeenms on 26/03/17.
 */

public class Movie {

   private String RESULTS = "results";
   private String OVERVIEW = "overview";
   private String RELEASE_DATE = "release_date";
   private String POSTER_PATH = "poster_path";
   private String TITLE = "original_title";
   private String VOTE_AVERAGE = "vote_average";
   private String BACKDROP_PATH = "backdrop_path";
   private String ID = "id";

    public Movie() {

    }


    public String getRESULTS() {
        return RESULTS;
    }

    public void setRESULTS(String RESULTS) {
        this.RESULTS = RESULTS;
    }

    public String getOVERVIEW() {
        return OVERVIEW;
    }

    public void setOVERVIEW(String OVERVIEW) {
        this.OVERVIEW = OVERVIEW;
    }

    public String getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public void setRELEASE_DATE(String RELEASE_DATE) {
        this.RELEASE_DATE = RELEASE_DATE;
    }

    public String getPOSTER_PATH() {
        return POSTER_PATH;
    }

    public void setPOSTER_PATH(String POSTER_PATH) {
        this.POSTER_PATH = POSTER_PATH;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getVOTE_AVERAGE() {
        return VOTE_AVERAGE;
    }

    public void setVOTE_AVERAGE(String VOTE_AVERAGE) {
        this.VOTE_AVERAGE = VOTE_AVERAGE;
    }

    public String getBACKDROP_PATH() {
        return BACKDROP_PATH;
    }

    public void setBACKDROP_PATH(String BACKDROP_PATH) {
        this.BACKDROP_PATH = BACKDROP_PATH;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}