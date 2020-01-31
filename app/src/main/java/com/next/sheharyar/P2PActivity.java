package com.next.sheharyar;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;

public class P2PActivity extends BaseActivity {

    TextView pfn_p2p_text;
    ImageView pfn_p2p_image;
    public static final String TAG = "shani";
    Switch p2p_protection_switch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_p2_p);

        p2p_protection_switch = (Switch)findViewById(R.id.p2p_protection_switch);


        pfn_p2p_text = (TextView)findViewById(R.id.pfn_p2p_back_text);
        pfn_p2p_image = (ImageView)findViewById(R.id.pfn_p2p_back_image);

        pfn_p2p_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pfn_p2p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences p2p = getSharedPreferences("P2P", MODE_PRIVATE);
        if (p2p != null) {
            if (p2p.getString("p2p_protection",null)!=null  && p2p.getString("p2p_protection",null).equals("true")){
                p2p_protection_switch.setChecked(true);
            }else {
                p2p_protection_switch.setChecked(false);
            }
        }else {
            p2p_protection_switch.setChecked(false);
        }

        p2p_protection_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = getSharedPreferences("P2P", MODE_PRIVATE).edit();
                if (isChecked){
                    editor.putString("p2p_protection","true");
                }else {
                    editor.putString("p2p_protection","false");
                }
                editor.apply();
            }
        });


    }
    String language;
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
