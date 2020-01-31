package com.next.sheharyar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.AtomDemoApplicationController;
import com.next.sheharyar.ChannelsActivity;
import com.next.sheharyar.LocationCountriesActivity;
import com.next.sheharyar.Model.ChannelListModel;
import com.next.sheharyar.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ChannelsListAdapter extends RecyclerView.Adapter<ChannelsListAdapter.ViewHolder> {



    private ArrayList<ChannelListModel> arrayList = new ArrayList<>();
    private ArrayList<ChannelListModel> filtered_arrayList = new ArrayList<>();
    private Context context;
    ChannelsActivity activity;
    public static final String TAG = "shani";

    public ChannelsListAdapter(ArrayList<ChannelListModel> arrayList, Context context, ChannelsActivity activity) {
        this.arrayList = arrayList;
        this.filtered_arrayList = arrayList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ChannelsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.channels_list_item, viewGroup, false);
        return new ChannelsListAdapter.ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelsListAdapter.ViewHolder holder, final int position) {


        final ChannelListModel model = arrayList.get(position);
        holder.name.setText(model.getName());

        Picasso.with(context)
                .load(model.getIcon_url())
                .resize(90, 70)
                .centerInside()
                .into(holder.icon);


        if (model.isFavourite()){
            holder.favourite.setImageResource(R.drawable.filled_heart);
        }else {
            holder.favourite.setImageResource(R.drawable.heart);
        }


        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ChannelListModel> savedChannels = new ArrayList<ChannelListModel>();
                SharedPreferences mPrefs = context.getSharedPreferences("favourite_list_channels", context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString("favourite_list", "");
                if (json.isEmpty()) {

                    Log.d(TAG,"empty ......");

                    savedChannels = new ArrayList<ChannelListModel>();
                    savedChannels.add(model);

                    SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list_channels", context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                    Gson gson_put = new Gson();
                    String json_put = gson_put.toJson(savedChannels);
                    prefsEditor.putString("favourite_list", json_put);
                    prefsEditor.commit();

                    holder.favourite.setImageResource(R.drawable.filled_heart);

                } else {

                    Log.d(TAG,"not empty ......");

                    Type type = new TypeToken<List<ChannelListModel>>() {
                    }.getType();

                    savedChannels = gson.fromJson(json, type);

                    if (model.isFavourite()){

                        Log.d(TAG,"ChannelsAdapter favourite......");
                        /// delete item from favourites

                        for (int i = 0; i <savedChannels.size() ; i++) {
                            if (savedChannels.get(i).getId().equals(model.getId())){
                                savedChannels.remove(i);
                                SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list_channels", context.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                                Gson gson_put = new Gson();
                                String json_put = gson_put.toJson(savedChannels);
                                prefsEditor.putString("favourite_list", json_put);
                                prefsEditor.commit();
                                holder.favourite.setImageResource(R.drawable.heart);
                            }
                        }

                    }else {
                        Log.d(TAG,"ChannelsAdapter not favourite......");

                        savedChannels.add(model);

                        SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list_channels", context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                        Gson gson_put = new Gson();
                        String json_put = gson_put.toJson(savedChannels);
                        prefsEditor.putString("favourite_list", json_put);
                        prefsEditor.commit();
                        holder.favourite.setImageResource(R.drawable.filled_heart);

                    }
                }

                activity.UpdateLists();
            }
        });
        
        
        


        holder.container_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String ChannelToGson = gson.toJson(model);
                SharedPreferences sharedPref = context.getSharedPreferences("connection",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("last_connection", "channel");
                editor.putString("channel_detail",ChannelToGson);
                editor.apply();

                Intent intent = new Intent();
                intent.putExtra("data", "channel");
                ((ChannelsActivity)context).setResult(RESULT_OK,intent);
                ((ChannelsActivity)context).finish();

            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView favourite, icon;
        LinearLayout container_stream;


        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            container_stream = (LinearLayout) itemView.findViewById(R.id.container_stream);
        }
    }
}

