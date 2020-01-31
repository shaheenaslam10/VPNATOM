/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 */

package com.next.sheharyar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atom.core.models.Country;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.Model.CountryImageMatcher;
import com.next.sheharyar.R;
import com.next.sheharyar.logger.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CountryAdapter extends ArrayAdapter<Country> {


    private Country[] countries;
    private boolean[] booleans_list;
    private List<Country> countriesList;
    private Context context;
    public static final String TAG = "shani";


    public CountryAdapter(Context context, int textViewResourceId, Country[] countries , boolean[] booleans_list) {
        super(context, textViewResourceId, countries);
        this.countries = countries;
        this.booleans_list = booleans_list;
        this.context = context;
    }



    @Override
    public int getCount() {
        if (countries != null) {
            return countries.length;
        }
        return 0;
    }

    @Override
    public Country getItem(int position) {
        return countries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView , favourite;
        TextView txtTitle;
        TextView latency;
    }
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView =  myInflater.inflate(R.layout.listitem, null);
        holder = new ViewHolder();
//        holder.txtTitle.setTextColor(Color.WHITE);
        holder.txtTitle = (TextView)convertView.findViewById(R.id.country_text);
        holder.latency = (TextView)convertView.findViewById(R.id.latency);
        holder.imageView = (ImageView)convertView.findViewById(R.id.country_image);
        holder.favourite = (ImageView)convertView.findViewById(R.id.favourite);
        holder.txtTitle.setText(countries[position].getName());

        holder.latency.setText(countries[position].getLatency());


        if (booleans_list[position]){
            holder.favourite.setImageResource(R.drawable.filled_heart);
        }else {
            holder.favourite.setImageResource(R.drawable.heart);
        }

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Country> savedCountry = new ArrayList<Country>();
                SharedPreferences mPrefs = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString("favourite_list", "");
                if (json.isEmpty()) {

                    savedCountry = new ArrayList<Country>();
                    savedCountry.add(countries[position]);

                    SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                    Gson gson_put = new Gson();
                    String json_put = gson_put.toJson(savedCountry);
                    prefsEditor.putString("favourite_list", json_put);
                    prefsEditor.commit();

                    holder.favourite.setImageResource(R.drawable.filled_heart);

                } else {
                    Type type = new TypeToken<List<Country>>() {
                    }.getType();

                    savedCountry = gson.fromJson(json, type);

                    boolean matched = false;
                    for (int i = 0; i <savedCountry.size() ; i++) {
                        if (savedCountry.get(i).getCountry()==countries[position].getCountry()){
                            matched = true;
                        }
                    }
                    if (matched){
                        Toast.makeText(context, "Already added to favourites.", Toast.LENGTH_SHORT).show();
                        /// delete item from favourites

                        for (int i = 0; i <savedCountry.size() ; i++) {
                            if (savedCountry.get(i).getCountry()==countries[position].getCountry()){
                                savedCountry.remove(i);
                                SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                                Gson gson_put = new Gson();
                                String json_put = gson_put.toJson(savedCountry);
                                prefsEditor.putString("favourite_list", json_put);
                                prefsEditor.commit();
                                holder.favourite.setImageResource(R.drawable.heart);
                            }
                        }

                    }else {

                        savedCountry.add(countries[position]);

                        Log.d(TAG,"array........"+savedCountry.toString());

                        SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                        Gson gson_put = new Gson();
                        String json_put = gson_put.toJson(savedCountry);
                        prefsEditor.putString("favourite_list", json_put);
                        prefsEditor.commit();
                        holder.favourite.setImageResource(R.drawable.filled_heart);

                    }



                }


            }
        });

        if (CountryImageMatcher.GetImageId(countries[position].getName()) != -1){
            holder.imageView.setImageDrawable(context.getResources().getDrawable(CountryImageMatcher.GetImageId(countries[position].getName())));
        }/*else {
            holder.imageView.setImageResource(context.getResources().getDrawable(R.drawable.));
        }*/


       /* if (countries[position].get() == 254 )
        {
            holder.imageView.setImageResource(R.drawable.us);
        } else if (countries[position].getId() == 253)
        {
            holder.imageView.setImageResource(R.drawable.uk);
        } else if (countries[position].getId() == 14)
        {
            holder.imageView.setImageResource(R.drawable.australia);
        } else if (countries[position].getId() == 91)
        {
            holder.imageView.setImageResource(R.drawable.germany);
        } else if (countries[position].getId() == 63)
        {
            holder.imageView.setImageResource(R.drawable.cyprus);
        } else if (countries[position].getId() == 43)
        {
            holder.imageView.setImageResource(R.drawable.canada);
        }
        else if (countries[position].getId() == 175)
        {
            holder.imageView.setImageResource(R.drawable.netherlands);
        }
        else if (countries[position].getId() == 109)
        {
            holder.imageView.setImageResource(R.drawable.hongkong);
        }
        else if (countries[position].getId() == 233)
        {
            holder.imageView.setImageResource(R.drawable.sweden);
        }
        else if (countries[position].getId() == 122)
        {
            holder.imageView.setImageResource(R.drawable.japan);
        }
        else if (countries[position].getId() == 144)
        {
            holder.imageView.setImageResource(R.drawable.luxembourg);
        }
        else if (countries[position].getId() == 113)
        {
            holder.imageView.setImageResource(R.drawable.india);
        }
        else if (countries[position].getId() == 15)
        {
            holder.imageView.setImageResource(R.drawable.austria);
        }
        else if (countries[position].getId() == 65)
        {
            holder.imageView.setImageResource(R.drawable.denmark);
        }
        else if (countries[position].getId() == 185)
        {
            holder.imageView.setImageResource(R.drawable.norway);
        }
        else if (countries[position].getId() == 64)
        {
            holder.imageView.setImageResource(R.drawable.czech);
        }
        else if (countries[position].getId() == 226)
        {
            holder.imageView.setImageResource(R.drawable.spain);
        }
        else if (countries[position].getId() == 159)
        {
            holder.imageView.setImageResource(R.drawable.mexico);
        }
        else if (countries[position].getId() == 246)
        {
            holder.imageView.setImageResource(R.drawable.turkey);
        }
        else if (countries[position].getId() == 75)
        {
            holder.imageView.setImageResource(R.drawable.estonia);
        }
        else if (countries[position].getId() == 129)
        {
            holder.imageView.setImageResource(R.drawable.kenya);
        }
        else if (countries[position].getId() == 224)
        {
            holder.imageView.setImageResource(R.drawable.southafrica);
        }
        else if (countries[position].getId() == 236)
        {
            holder.imageView.setImageResource(R.drawable.taiwan);
        }
        else if (countries[position].getId() == 16)
        {
            holder.imageView.setImageResource(R.drawable.azerbaijan);
        }
        else if (countries[position].getId() == 35)
        {
            holder.imageView.setImageResource(R.drawable.britishvirgin);
        }
        else if (countries[position].getId() == 198)
        {
            holder.imageView.setImageResource(R.drawable.portugal);
        }
        else if (countries[position].getId() == 199)
        {
            holder.imageView.setImageResource(R.drawable.puertorico);
        }
        else if (countries[position].getId() == 238)
        {
            holder.imageView.setImageResource(R.drawable.tanzania);
        }
        else if (countries[position].getId() == 252)
        {
            holder.imageView.setImageResource(R.drawable.uae);
        }
        else if (countries[position].getId() == 303)
        {
            holder.imageView.setImageResource(R.drawable.isleofman);
        }*/
        return convertView;

    }
}