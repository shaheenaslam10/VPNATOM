package com.next.sheharyar;

import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.LocaleManager;

import java.util.Locale;

public class InternetKillSwitchActivity extends AppCompatActivity {


    ImageView pfn_settings_back_image;
    TextView pfn_settings_back;

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
        setContentView(R.layout.activity_internet_kill_switch);



        pfn_settings_back_image = (ImageView)findViewById(R.id.pfn_settings_back_image);
        pfn_settings_back = (TextView)findViewById(R.id.pfn_settings_back);





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

    }
*/
}
