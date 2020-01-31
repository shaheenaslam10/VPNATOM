package com.next.sheharyar;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.next.sheharyar.APIs.APIs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AboutDetailActivity extends AppCompatActivity {


    TextView title , detail_text , pfn_settings_back;
    public static final String TAG = "shani";
    String about_type , param;
    RelativeLayout progress_layout;
    ImageView pfn_settings_back_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_detail);


        title = (TextView)findViewById(R.id.title);
        detail_text = (TextView)findViewById(R.id.detail_text);
        pfn_settings_back = (TextView)findViewById(R.id.pfn_settings_back);
        pfn_settings_back_image = (ImageView) findViewById(R.id.pfn_settings_back_image);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);


        Intent intent = getIntent();
        if (intent!=null){

            about_type = intent.getStringExtra("about_type");
            if (about_type.equals("TermsofService")){
                title.setText("Terms Of Services");
                param = "police_gernal";
            }else if (about_type.equals("PrivacyPolicy")){
                title.setText("Privacy Policy");
                param = "terms";
            }else if (about_type.equals("PfnOpportunity")){
                title.setText("PFN Opportunity");
                param = "terms_opportunity";
            }

        }

        pfn_settings_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pfn_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progress_layout.setVisibility(View.VISIBLE);
        GetData();


    }





    public void GetData(){


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                Log.d(TAG,"param...."+param);

                RequestBody formBody = new FormBody.Builder()
                        .add(param, "1")
                        .build();


                Request request = new Request.Builder()
                        .url(APIs.about)
                        .post(formBody)
                        .build();

                Log.d(TAG, "before call");


                try {
                    Response response = client.newCall(request).execute();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_layout.setVisibility(View.GONE);
                        }
                    });

                    if (response.isSuccessful()) {

                        Log.d(TAG, "response...." + response);




                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getString("status").equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(AboutDetailActivity.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (object.getString("status").equals("1")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {

                                        detail_text.setText(object.getString("content"));


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });




                        }


                    } else {
                        Log.d(TAG, "response...." + response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AboutDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    // Do something with the response.
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "exception...." + e.getMessage());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_layout.setVisibility(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_layout.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

            }
        });
        thread.start();


    }

}
