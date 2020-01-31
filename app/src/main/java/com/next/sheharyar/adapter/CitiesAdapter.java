package com.next.sheharyar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.AtomDemoApplicationController;
import com.next.sheharyar.Cities;
import com.next.sheharyar.CitiesFragment;
import com.next.sheharyar.LocationCountriesActivity;
import com.next.sheharyar.Model.CitiesItem;
import com.next.sheharyar.R;
import com.next.sheharyar.logger.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sheharyar on 11/25/2018.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<CitiesItem> mCitiesList;
        private ArrayList<CitiesItem> citiesListFiltered;
    private CitiesAdapter.OnItemClickListener mListener;
    private CitiesAdapterListener listener;
    private Cities[] cities;
    private CitiesFragment fragment;
    public static final String TAG = "shani";

    public CitiesAdapter(Context mContext, ArrayList<CitiesItem> mCitiesList, CitiesFragment fragment) {
        this.mContext = mContext;
        this.mCitiesList = mCitiesList;
        this.fragment = fragment;
        listener = listener;
        citiesListFiltered = mCitiesList;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    citiesListFiltered = mCitiesList;
                } else {

                    ArrayList<CitiesItem> filteredList = new ArrayList<>();
                    for (CitiesItem row : mCitiesList){
                        if (row.getmCityName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    citiesListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = citiesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                citiesListFiltered = (ArrayList<CitiesItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CitiesAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public CitiesAdapter.CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.cities_item, parent, false);

        return new CitiesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CitiesAdapter.CitiesViewHolder holder, int position) {

         CitiesItem citiesItem = citiesListFiltered.get(position);

        String creatorName = citiesItem.getmCityName();

        holder.mTextViewCreator.setText(creatorName);

        if (citiesItem.isFavourite()){
            holder.favourite.setImageResource(R.drawable.filled_heart);
        }else {
            holder.favourite.setImageResource(R.drawable.heart);
        }

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ArrayList<CitiesItem> savedCities = new ArrayList<CitiesItem>();
                SharedPreferences mPrefs = mContext.getSharedPreferences("favourite_list_cities", mContext.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString("favourite_list", "");
                if (json.isEmpty()) {

                    savedCities = new ArrayList<CitiesItem>();
                    savedCities.add(citiesItem);

                    SharedPreferences mPrefs_put = mContext.getSharedPreferences("favourite_list_cities", mContext.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                    Gson gson_put = new Gson();
                    String json_put = gson_put.toJson(savedCities);
                    prefsEditor.putString("favourite_list", json_put);
                    prefsEditor.commit();

                    holder.favourite.setImageResource(R.drawable.filled_heart);

                } else {

                    Type type = new TypeToken<List<CitiesItem>>() {
                    }.getType();

                    savedCities = gson.fromJson(json, type);

                    if (citiesItem.isFavourite()){

                        /// delete item from favourites

                        for (int i = 0; i <savedCities.size() ; i++) {
                            if (savedCities.get(i).getmId() == citiesItem.getmId()){
                                savedCities.remove(i);
                                SharedPreferences mPrefs_put = mContext.getSharedPreferences("favourite_list_cities", mContext.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                                Gson gson_put = new Gson();
                                String json_put = gson_put.toJson(savedCities);
                                prefsEditor.putString("favourite_list", json_put);
                                prefsEditor.commit();
                                holder.favourite.setImageResource(R.drawable.heart);
                            }
                        }

                        fragment.UpdateLists(position , "remove");


                    }else {

                        savedCities.add(citiesItem);

                        SharedPreferences mPrefs_put = mContext.getSharedPreferences("favourite_list_cities", mContext.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                        Gson gson_put = new Gson();
                        String json_put = gson_put.toJson(savedCities);
                        prefsEditor.putString("favourite_list", json_put);
                        prefsEditor.commit();
                        holder.favourite.setImageResource(R.drawable.filled_heart);

                        fragment.UpdateLists(position , "add");

                    }

                }




            }
        });

        holder.container_city_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(TAG,"called...........");


                SharedPreferences.Editor editor = mContext.getSharedPreferences("connection", MODE_PRIVATE).edit();
                editor.putString("countryId", citiesItem.getmCountryId());
                editor.putString("cityId", String.valueOf(citiesItem.getmId()));
                editor.putString("cityName", citiesItem.getmCityName());
                editor.putString("access_token", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                editor.putString("last_connection", "city");
                editor.apply();

                Intent intent = new Intent();
                intent.putExtra("countryId", citiesItem.getmCountryId());
                intent.putExtra("cityId", String.valueOf(citiesItem.getmId()));
                intent.putExtra("cityName", citiesItem.getmCityName());
                intent.putExtra("access_token", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                intent.putExtra("data_center_list", citiesItem.getData_center_array());
                ((LocationCountriesActivity)mContext).setResult(RESULT_OK,intent);
                ((LocationCountriesActivity)mContext).finish();



            }
        });

    }

    @Override
    public int getItemCount() {
        return citiesListFiltered.size();
    }

    public class CitiesViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewCreator;
        ImageView favourite;
        LinearLayout container_city_list;


        public CitiesViewHolder(View itemView) {
            super(itemView);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            favourite = itemView.findViewById(R.id.favourite);
            container_city_list = itemView.findViewById(R.id.container_city_list);



           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                          //  mListener.onItemClick(position);




                        }else {
                            Log.d(TAG,"elseeeeeeeeeee1");
                        }
                  *//*  }else {
                        Log.d(TAG,"elseeeeeeeeeee2");
                    }*//*
                }
            });*/
        }

    }
    public interface CitiesAdapterListener {
        void onCitiesSelected(CitiesItem cities);
    }
}
