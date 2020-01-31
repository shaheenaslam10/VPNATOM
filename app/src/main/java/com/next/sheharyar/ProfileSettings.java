package com.next.sheharyar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.next.sheharyar.LocaleManager.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.next.sheharyar.APIs.APIs;
import com.next.sheharyar.LocaleManager.LocaleManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileSettings extends BaseActivity {

    TextView SettingsBackText, update;
    ImageView SettingsBackImage;
    EditText user_name, f_name, l_name, address, company, email;
    public static final String TAG = "shani";
    String pr_email, pr_username, pr_name;
    ScrollView scrollView1;
    RelativeLayout progress_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_settings);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        user_name = (EditText) findViewById(R.id.user_name);
        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        company = (EditText) findViewById(R.id.company);


        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            pr_username = prefs.getString("username", null);
            pr_email = prefs.getString("email", null);
            pr_name = prefs.getString("name", null);

            Log.d(TAG, "username......" + pr_username);
            Log.d(TAG, "email......" + pr_email);
        }


        SettingsBackText = (TextView) findViewById(R.id.settings_main_back);
        update = (TextView) findViewById(R.id.update);
        SettingsBackImage = (ImageView) findViewById(R.id.settings_back_main_image);

        SettingsBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        SettingsBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateProfile();
            }
        });


        GetProfile();


    }

    public void GetProfile() {


        scrollView1.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("list_p_email", pr_username)
                        .build();
                Request request = new Request.Builder()
                        .url(APIs.GENERAL_URL)
                        .post(formBody)
                        .build();

                Log.d(TAG, "before call");


                try {
                    Response response = client.newCall(request).execute();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progress_layout.setVisibility(View.GONE);
                            scrollView1.setVisibility(View.VISIBLE);

                        }
                    });

                    if (response.isSuccessful()) {

                        Log.d(TAG, "response...." + response);

                                /*{

                                    "status": 1,
                                    "status_message": "Profile Data Display Successfully.",
                                    "fname": "Shaheen",
                                     "lname": "Aslam",
                                      "email": "shaheenaslam10@gmail.com",
                                     "address": "house# 567, D1-Block , china scheme baghbanpura lahore",
                                      "company": ""
                                                           }
                                  }*/
                        //{"status":1,"status_message":"Profile Data Display Successfully.","fname":"rizwan ","lname":"arshad","email":"adeelarshad099@gmail.com","address":"lahore","company":"logic2c","username":"rizwan000","dedCityName":"txt","json_string":"text"}

                        JSONObject object = new JSONObject(response.body().string());

                        Log.d(TAG, "response object......." + object);

                        if (object.getString("status").equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(ProfileSettings.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
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

                                        company.setText(object.getString("company"));
                                        user_name.setText(object.getString("username"));
                                        email.setText(object.getString("email"));
                                        l_name.setText(object.getString("lname"));
                                        f_name.setText(object.getString("fname"));
                                        address.setText(object.getString("address"));
                                        user_name.setText(pr_username);

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
                                Toast.makeText(ProfileSettings.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    // Do something with the response.
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "exception...." + e.getMessage());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();


    }

    public void UpdateProfile() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("up_profile_username", pr_username)
                        .add("up_profile_fname", f_name.getText().toString())
                        .add("up_profile_lname", l_name.getText().toString())
                        .add("up_profile_company", company.getText().toString())
                        .add("up_profile_address", address.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(APIs.GENERAL_URL)
                        .post(formBody)
                        .build();

                Log.d(TAG, "before call");


                try {
                    Response response = client.newCall(request).execute();


                    if (response.isSuccessful()) {

                        Log.d(TAG, "response...." + response);

                                /*{
                                    "status": 1,
                                    "status_message": "Profile Data Display Successfully.",
                                    "fname": "Shaheen",
                                     "lname": "Aslam",
                                      "email": "shaheenaslam10@gmail.com",
                                     "address": "house# 567, D1-Block , china scheme baghbanpura lahore",BBHHNHBBB
                                      "company": ""
                                                           }
                                  }*/

                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getString("status").equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(ProfileSettings.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (object.getString("status").equals("1")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ProfileSettings.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();

                                    try {

                                        company.setText(object.getString("company"));
                                        l_name.setText(object.getString("lname"));
                                        f_name.setText(object.getString("fname"));
                                        address.setText(object.getString("address"));

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
                                Toast.makeText(ProfileSettings.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    // Do something with the response.
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "exception...." + e.getMessage());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();


    }

    String language;

    @Override
    protected void onResume() {
        super.onResume();
        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)) {
            LocaleManager.setNewLocale(this, LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }

    }
}
