package com.next.sheharyar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
 * Created by Shani on 3/26/2019.
 */

public class FavouriteCitiesAdapter extends RecyclerView.Adapter<FavouriteCitiesAdapter.CitiesViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<CitiesItem> mCitiesList;
    private ArrayList<CitiesItem> citiesListFiltered;
    private FavouriteCitiesAdapter.OnItemClickListener mListener;
    private FavouriteCitiesAdapter.CitiesAdapterListener listener;
    private Cities[] cities;
    private CitiesFragment fragment;
    public static final String TAG = "shani";


    public FavouriteCitiesAdapter(Context mContext, ArrayList<CitiesItem> mCitiesList, CitiesFragment fragment) {
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
                if (charString.isEmpty())
                {
                    citiesListFiltered = mCitiesList;
                } else {
                    ArrayList<CitiesItem> filteredList = new ArrayList<>();
                    for (CitiesItem row : mCitiesList){
                        if (row.getmCityName().toLowerCase().contains(charString.toLowerCase()))
                        {
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
    public void setOnItemClickListener(FavouriteCitiesAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public FavouriteCitiesAdapter.CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.favourite_cities_item, parent, false);

        return new FavouriteCitiesAdapter.CitiesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavouriteCitiesAdapter.CitiesViewHolder holder, int position) {

        CitiesItem citiesItem = citiesListFiltered.get(position);

        String creatorName = citiesItem.getmCityName();

        holder.mTextViewCreator.setText(creatorName);


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<CitiesItem> savedCities = new ArrayList<CitiesItem>();
                SharedPreferences mPrefs = mContext.getSharedPreferences("favourite_list_cities", mContext.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString("favourite_list", "");

                Type type = new TypeToken<List<CitiesItem>>() {
                }.getType();

                savedCities = gson.fromJson(json, type);

                savedCities.remove(position);
                SharedPreferences mPrefs_put = mContext.getSharedPreferences("favourite_list_cities", mContext.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                Gson gson_put = new Gson();
                String json_put = gson_put.toJson(savedCities);
                prefsEditor.putString("favourite_list", json_put);
                prefsEditor.commit();


                fragment.UpdateLists(position, "remove");
            }
        });

    }

    @Override
    public int getItemCount() {
        return citiesListFiltered.size();
    }

    public class CitiesViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewCreator;
        ImageView remove;


        public CitiesViewHolder(View itemView) {
            super(itemView);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            remove = itemView.findViewById(R.id.remove);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (mListener != null) {*/
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                           // mListener.onItemClick(position);


                            SharedPreferences.Editor editor = mContext.getSharedPreferences("connection", MODE_PRIVATE).edit();
                            editor.putString("countryId", mCitiesList.get(position).getmCountryId());
                            editor.putString("cityId", String.valueOf(mCitiesList.get(position).getmId()));
                            editor.putString("cityName", mCitiesList.get(position).getmCityName());
                            editor.apply();


                            Intent intent = new Intent();
                            intent.putExtra("countryId", mCitiesList.get(position).getmCountryId());
                            intent.putExtra("cityId", String.valueOf(mCitiesList.get(position).getmId()));
                            intent.putExtra("cityName", mCitiesList.get(position).getmCityName());
                            intent.putExtra("access_token", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                            intent.putExtra("data_center_list", mCitiesList.get(position).getData_center_array());
                            ((LocationCountriesActivity)mContext).setResult(RESULT_OK,intent);
                            ((LocationCountriesActivity)mContext).finish();

                        }else {
                            Log.d(TAG,"in else part 1....");
                        }
                    /*}else {
                        Log.d(TAG,"in else part 2....");
                    }*/
                }
            });
        }

    }
    public interface CitiesAdapterListener {
        void onCitiesSelected(CitiesItem cities);
    }
}
