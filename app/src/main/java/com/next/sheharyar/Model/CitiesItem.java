package com.next.sheharyar.Model;

import org.json.JSONArray;

/**
 * Created by sheharyar on 11/25/2018.
 */

public class CitiesItem {
    private int mId;
    private String mCityName;;
    private String mCountryId;
    private String[] data_center_array;
    private boolean favourite;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getmCountryId() {
        return mCountryId;
    }

    public void setmCountryId(String mCountryId) {
        this.mCountryId = mCountryId;
    }

    public String[] getData_center_array() {
        return data_center_array;
    }

    public void setData_center_array(String[] data_center_array) {
        this.data_center_array = data_center_array;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public CitiesItem(int id, String name, String country_id)
    {
        mId = id;
        mCityName = name;
        mCountryId = country_id;
    }

    public CitiesItem(int mId, String mCityName, String mCountryId, String[] data_center_array, boolean favourite) {
        this.mId = mId;
        this.mCityName = mCityName;
        this.mCountryId = mCountryId;
        this.favourite = favourite;
    }
}
