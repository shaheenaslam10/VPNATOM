package com.next.sheharyar.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.next.sheharyar.FaqOne;
import com.next.sheharyar.Model.FaqModel;
import com.next.sheharyar.R;

import java.util.ArrayList;

/**
 * Created by Shani on 3/14/2019.
 */

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {



    ArrayList<FaqModel> arrayList = new ArrayList<>();
    Context context;
    String res;

    public FaqAdapter(ArrayList<FaqModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.faq_list_row, viewGroup, false);
        Log.d("shani","in adpater... create view..");

        return new FaqAdapter.ViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqAdapter.ViewHolder holder, final int position) {


        final FaqModel model = arrayList.get(position);


        holder.title.setText(model.getFaq_title());


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,FaqOne.class);
                intent.putExtra("title",model.getFaq_title());
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        LinearLayout container;


        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);

            container = (LinearLayout) itemView.findViewById(R.id.container);
            title = (TextView)itemView.findViewById(R.id.faq_title);
        }
    }
}
