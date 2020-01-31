package com.next.sheharyar;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.LocaleManager;

import java.util.Locale;

public class SecureWifiActivity extends AppCompatActivity {

    TextView pfn_settings_back , switch_status;
    ImageView pfn_settings_back_image;
    Switch secure_wifi_switch , connect_on_non_secure , connect_on_cellular;
    ScrollView scrollView;
    RelativeLayout off_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad  = LocaleManager.getLanguage(this); // change your language here
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_secure_wifi);




        pfn_settings_back_image = (ImageView)findViewById(R.id.pfn_settings_back_image);
        pfn_settings_back = (TextView)findViewById(R.id.pfn_settings_back);
        switch_status = (TextView)findViewById(R.id.switch_status);
        secure_wifi_switch = (Switch)findViewById(R.id.secure_wifi_switch);
        connect_on_non_secure = (Switch)findViewById(R.id.connect_on_non_secure);
        connect_on_cellular = (Switch)findViewById(R.id.connect_on_cellular);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        off_layout = (RelativeLayout) findViewById(R.id.off_layout);


        pfn_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pfn_settings_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("SecureWifi", MODE_PRIVATE);
        if (prefs != null) {
            if (prefs.getString("secureWifi",null) != null  &&  prefs.getString("secureWifi",null).equals("true")){
                secure_wifi_switch.setChecked(true);
                switch_status.setText(getResources().getString(R.string.onDemand_working));
                CheckInner(prefs);
            }else {
                secure_wifi_switch.setChecked(false);
                switch_status.setText(getResources().getString(R.string.inside_on_demand_setup));
                connect_on_cellular.setChecked(false);
                connect_on_non_secure.setChecked(false);
                scrollView.setVisibility(View.GONE);
                off_layout.setVisibility(View.VISIBLE);
                CheckInner(prefs);
            }
        }else {

            secure_wifi_switch.setChecked(false);
            switch_status.setText(getResources().getString(R.string.inside_on_demand_setup));
            connect_on_cellular.setChecked(false);
            connect_on_non_secure.setChecked(false);
            scrollView.setVisibility(View.GONE);
            off_layout.setVisibility(View.VISIBLE);
        }


        secure_wifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    switch_status.setText("ON");
                    off_layout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = getSharedPreferences("SecureWifi", MODE_PRIVATE).edit();
                    editor.putString("secureWifi","true");
                    editor.apply();

                }else {
                    switch_status.setText("OFF");
                    scrollView.setVisibility(View.GONE);
                    off_layout.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = getSharedPreferences("SecureWifi", MODE_PRIVATE).edit();
                    editor.putString("secureWifi","false");
                    editor.apply();
                }
            }
        });

        connect_on_cellular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("SecureWifi", MODE_PRIVATE).edit();
                    editor.putString("secureWifi_cellular","true");
                    editor.apply();

                }else {
                    SharedPreferences.Editor editor = getSharedPreferences("SecureWifi", MODE_PRIVATE).edit();
                    editor.putString("secureWifi_cellular","false");
                    editor.apply();
                }
            }
        });

        connect_on_non_secure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("SecureWifi", MODE_PRIVATE).edit();
                    editor.putString("secureWifi_non_secure","true");
                    editor.apply();

                }else {
                    SharedPreferences.Editor editor = getSharedPreferences("SecureWifi", MODE_PRIVATE).edit();
                    editor.putString("secureWifi_non_secure","false");
                    editor.apply();
                }
            }
        });

    }


    public void CheckInner(SharedPreferences prefs){

        if (prefs.getString("secureWifi_cellular",null) != null   &&  prefs.getString("secureWifi_cellular",null).equals("true")){
            connect_on_cellular.setChecked(true);
        }else {
            connect_on_cellular.setChecked(false);
        }
        if (prefs.getString("secureWifi_non_secure",null) != null   &&  prefs.getString("secureWifi_non_secure",null).equals("true")){
            connect_on_non_secure.setChecked(true);
        }else {
            connect_on_non_secure.setChecked(false);
        }
    }

    public static final String TAG = "shani";
    /*String language;
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

    }*/
}
