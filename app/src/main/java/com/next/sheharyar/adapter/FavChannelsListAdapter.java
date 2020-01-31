package com.next.sheharyar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.ChannelsActivity;
import com.next.sheharyar.LocationCountriesActivity;
import com.next.sheharyar.Model.ChannelListModel;
import com.next.sheharyar.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FavChannelsListAdapter  extends RecyclerView.Adapter<FavChannelsListAdapter.ViewHolder> {



    private ArrayList<ChannelListModel> arrayList = new ArrayList<>();
    private Context context;
    ChannelsActivity activity;

    public FavChannelsListAdapter(ArrayList<ChannelListModel> arrayList, Context context, ChannelsActivity activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FavChannelsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.channels_list_item_remove, viewGroup, false);
        return new FavChannelsListAdapter.ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavChannelsListAdapter.ViewHolder holder, final int position) {


        final ChannelListModel model = arrayList.get(position);
        holder.name.setText(model.getName());

        Picasso.with(context)
                .load(model.getIcon_url())
                .resize(90, 70)
                .centerInside()
                .into(holder.icon);


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ChannelListModel> savedChannels = new ArrayList<ChannelListModel>();
                SharedPreferences mPrefs = context.getSharedPreferences("favourite_list_channels", context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString("favourite_list", "");

                Type type = new TypeToken<List<ChannelListModel>>() {
                }.getType();

                savedChannels = gson.fromJson(json, type);

                savedChannels.remove(position);
                SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list_channels", context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                Gson gson_put = new Gson();
                String json_put = gson_put.toJson(savedChannels);
                prefsEditor.putString("favourite_list", json_put);
                prefsEditor.commit();


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
        ImageView remove, icon;
        LinearLayout container_stream;


        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            remove = (ImageView) itemView.findViewById(R.id.remove);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            container_stream = (LinearLayout) itemView.findViewById(R.id.container_stream);
        }
    }
}