package com.next.sheharyar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.Model.PInfo;
import com.next.sheharyar.adapter.ApplicationListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SplitTunneling extends BaseActivity {

    TextView PfnSettingsBackText;
    ImageView PfnSettingsBackImage;
    String language;
    public static final String TAG = "shani";
    Switch onDemandSwitch;
    TextView switch_status;
    LinearLayout no_list_layout;
    RecyclerView mRecyclerView;
    ApplicationListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<PInfo> arrayList = new ArrayList<>();
    ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_split_tunneling);



        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        no_list_layout = (LinearLayout) findViewById(R.id.no_list_layout);
        onDemandSwitch = (Switch) findViewById(R.id.onDemandSwitch);
        PfnSettingsBackText = (TextView)findViewById(R.id.pfn_settings_back);
        switch_status = (TextView)findViewById(R.id.switch_status);
        PfnSettingsBackImage = (ImageView)findViewById(R.id.pfn_settings_back_image);

        progress_circular = (ProgressBar) findViewById(R.id.progress_circular);

        PfnSettingsBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        PfnSettingsBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences prefs = getSharedPreferences("OnDemand", MODE_PRIVATE);
        if (prefs != null) {

            if (prefs.getString("onDemand",null) != null  &&  prefs.getString("onDemand",null).equals("true")){
                onDemandSwitch.setChecked(true);
                ShowItem(true);
                switch_status.setText(getResources().getString(R.string.onDemand_working));
            }else {
                onDemandSwitch.setChecked(false);
                ShowItem(false);
                switch_status.setText(getResources().getString(R.string.inside_on_demand_setup));

            }

        }else {
            onDemandSwitch.setChecked(false);
            ShowItem(false);
            switch_status.setText(getResources().getString(R.string.inside_on_demand_setup));
        }


        onDemandSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                ShowItem(isChecked);

            }
        });
    }





    public void ShowItem(boolean isChecked){

        onDemandSwitch.setChecked(isChecked);
        SharedPreferences sharedPref = getSharedPreferences("OnDemand", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (isChecked){

            progress_circular.setVisibility(View.VISIBLE);

            MyAsyncTask asyncTask = new MyAsyncTask();
            asyncTask.execute();
            editor.putString("onDemand","true");

        }else {

            progress_circular.setVisibility(View.GONE);
            no_list_layout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
//            search.setVisibility(View.GONE);
            editor.putString("onDemand","false");
        }
        editor.apply();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String  successed = "success";
            arrayList = getInstalledApps(false);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SetAdapter();
                    no_list_layout.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            no_list_layout.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
//                            search.setVisibility(View.VISIBLE);
                            Log.d("shani","list get success.....");
                        }
                    },500);

                }
            });

            return successed;
        }

        protected void onPostExecute(String success) {
            Log.d("shani","onPostExecute called.......");
        }
    }

    public void ApplyFilters(String string){

        ArrayList<PInfo> searchTemList = new ArrayList<>();
        for (int i = 0; i <arrayList.size() ; i++) {
            String appName_lower = arrayList.get(i).getAppname().toLowerCase();
            String string_lower = string.toLowerCase();
            if (appName_lower.contains(string_lower)){
                searchTemList.add(arrayList.get(i));
            }
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ApplicationListAdapter(searchTemList, SplitTunneling.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void SetAdapter(){

        progress_circular.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ApplicationListAdapter(arrayList, SplitTunneling.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }

            PInfo newInfo = new PInfo();
            newInfo.setAppname( p.applicationInfo.loadLabel(getPackageManager()).toString());
            newInfo.setPname(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }




    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"current language......"+ LocaleManager.getLanguage(this));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()).getLanguage());

        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }

    }
}
