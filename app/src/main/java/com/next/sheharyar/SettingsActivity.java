package com.next.sheharyar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.logger.Log;

public class SettingsActivity extends BaseActivity {
    TextView pfn_settings_back, about;
    ImageView pfn_back_image, about_image;
    RelativeLayout aboutlayout, profileLayout, protocolLayout, changeLanguageLayout, supportCenter, onDemandVpn, logout , multi_port , auto_connect, internet_kill , secure_wifi , network_type;
    public static final String TAG = "shani";
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        pfn_back_image = (ImageView) findViewById(R.id.pfn_settings_back_image);
        pfn_settings_back = (TextView)findViewById(R.id.pfn_settings_back);

        pfn_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pfn_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        about = (TextView)findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(about);
            }
        });

        about_image = (ImageView)findViewById(R.id.about_image);
        about_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutImage = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutImage);
            }
        });

        aboutlayout = (RelativeLayout)findViewById(R.id.about_layout);
        aboutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutLayout = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutLayout);
            }
        });

        profileLayout = (RelativeLayout)findViewById(R.id.profile);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileLayout = new Intent(getApplicationContext(), ProfileSettings.class);
                startActivity(profileLayout);
            }
        });

        protocolLayout = (RelativeLayout)findViewById(R.id.protocol);
        protocolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent protocolLayout = new Intent(getApplicationContext(), ProtocolsActivity.class);
                startActivity(protocolLayout);
            }
        });

        multi_port = (RelativeLayout)findViewById(R.id.multi_port);
        multi_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent protocolLayout = new Intent(getApplicationContext(), MultiPortActivity.class);
                startActivity(protocolLayout);
            }
        });

        internet_kill = (RelativeLayout)findViewById(R.id.internet_kill);
        internet_kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent protocolLayout = new Intent(getApplicationContext(), InternetKillSwitchActivity.class);
                startActivity(protocolLayout);
            }
        });

        secure_wifi = (RelativeLayout)findViewById(R.id.secure_wifi);
        secure_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent protocolLayout = new Intent(getApplicationContext(), SecureWifiActivity.class);
                startActivity(protocolLayout);
            }
        });

        network_type = (RelativeLayout)findViewById(R.id.network_type);
        network_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent protocolLayout = new Intent(getApplicationContext(), NetworkTypeActivity.class);
                startActivity(protocolLayout);
            }
        });

        changeLanguageLayout = (RelativeLayout)findViewById(R.id.changeLanguageLayout);
        changeLanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeLanguage = new Intent(getApplicationContext(), Changelanguage.class);
                startActivity(changeLanguage);
            }
        });

        supportCenter = (RelativeLayout)findViewById(R.id.supportLayout);
        supportCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), SupportCenter.class));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-support/"));
                startActivity(browserIntent);
            }
        });

        onDemandVpn = (RelativeLayout)findViewById(R.id.ondemandvpnlayout);
        onDemandVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SplitTunneling.class));
            }
        });

        auto_connect = (RelativeLayout)findViewById(R.id.auto_connect);
        auto_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AutoConnectActivity.class));
            }
        });

        logout = (RelativeLayout)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_description)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
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
            startActivity(getIntent());
            finish();
        }

    }
}
