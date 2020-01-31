package com.next.sheharyar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class UnableToConnect extends AppCompatActivity {

    TextView PfnSettingsBackText;
    ImageView PfnSettingsBackImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_unable_to_connect);

        PfnSettingsBackText = (TextView)findViewById(R.id.pfn_settings_back);
        PfnSettingsBackImage = (ImageView)findViewById(R.id.pfn_settings_back_image);

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

    }
}
