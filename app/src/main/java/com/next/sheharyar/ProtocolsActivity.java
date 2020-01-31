package com.next.sheharyar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.logger.Log;

public class ProtocolsActivity extends BaseActivity {

    TextView PfnSettingsBackText;
    ImageView PfnSettingsBackImage , p1_info, p2_info;

    RelativeLayout UnableConnect , connect_guide;
    private RadioGroup radioGroup;
    private RadioButton radioButton , radioBtn_auto , radioBtn_tcp , radioBtn_udp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_protocols);

        PfnSettingsBackText = (TextView)findViewById(R.id.pfn_settings_back);
        PfnSettingsBackImage = (ImageView)findViewById(R.id.pfn_settings_back_image);

        p1_info = (ImageView)findViewById(R.id.p1_info);
        p2_info = (ImageView)findViewById(R.id.p2_info);

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

        UnableConnect = (RelativeLayout)findViewById(R.id.unable_to_connectLayout);
        connect_guide = (RelativeLayout)findViewById(R.id.connect_guide);
        UnableConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),UnableToConnect.class ));

            }
        });


        connect_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/user-guide/"));
                startActivity(browserIntent);
            }
        });



        radioGroup = (RadioGroup) findViewById(R.id.radio);
        radioBtn_auto = (RadioButton) findViewById(R.id.radioAuto);
        radioBtn_tcp = (RadioButton) findViewById(R.id.radioTCP);
        radioBtn_udp = (RadioButton) findViewById(R.id.radioUDP);

        SharedPreferences prefs = getSharedPreferences("Protocols", MODE_PRIVATE);
        if (prefs != null) {

            if (prefs.getString("selected",null) != null  &&  prefs.getString("selected",null).equals("auto")){
                radioBtn_auto.setChecked(true);
            }else if (prefs.getString("selected",null) != null  &&  prefs.getString("selected",null).equals("tcp")){
                radioBtn_tcp.setChecked(true);
            }else if (prefs.getString("selected",null) != null  &&  prefs.getString("selected",null).equals("udp")){
                radioBtn_udp.setChecked(true);
            }else
                radioBtn_auto.setChecked(true);

        }else {
            radioBtn_auto.setChecked(false);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                Log.d(TAG,"id ..... "+radioButton.getId());
                Log.d(TAG,"selected id ..... "+selectedId);

                if (radioButton.getId() == R.id.radioAuto){
                    SharedPreferences.Editor editor = getSharedPreferences("Protocols", MODE_PRIVATE).edit();
                    editor.putString("selected","auto");
                    editor.apply();
                }else if (radioButton.getId() == R.id.radioTCP){
                    SharedPreferences.Editor editor = getSharedPreferences("Protocols", MODE_PRIVATE).edit();
                    editor.putString("selected","tcp");
                    editor.apply();
                }else if (radioButton.getId() == R.id.radioUDP){
                    SharedPreferences.Editor editor = getSharedPreferences("Protocols", MODE_PRIVATE).edit();
                    editor.putString("selected","udp");
                    editor.apply();
                }

            }
        });






        p1_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ProtocolsActivity.this)
                        .setTitle("Information")
                        .setMessage("TCP(Transmission Control Protol) is Connection-Oriented protocol. TCP tracks all data sent, requiring acknowledgment for each octet (generally). Because of acknowledgments, TCP is considered a reliable data transfer protocol. It ensures that no data is sent to the upper layer application that is out of order, duplicated, or has missing pieces. It can even manage transmissions to attempt to reduce congestion")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });
        p2_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ProtocolsActivity.this)
                        .setTitle("Information")
                        .setMessage("UDP(User Datagram Protocol is a very lightweight protocol. The primary uses for UDP include service advertisements, such as routing protocol updates and server availability, one-to-many multicast applications, and streaming applications, such as voice and video, where a lost datagram is far less important than an out-of-order datagram.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });
    }


    public static final String TAG = "shani";
    String language;
    @Override
    protected void onResume() {
        super.onResume();

        android.util.Log.d(TAG,"current language......"+ LocaleManager.getLanguage(this));
        android.util.Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()));
        android.util.Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()).getLanguage());

        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }

    }
}
