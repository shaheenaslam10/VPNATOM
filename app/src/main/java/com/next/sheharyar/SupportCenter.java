package com.next.sheharyar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class SupportCenter extends AppCompatActivity {

    TextView PfnSettingsBackText;
    ImageView PfnSettingsBackImage;

    RelativeLayout supportCenter;
    Button submit;
    EditText msg , email;
    String pr_username, pr_name;
    public static final String TAG = "shani";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_support_center);


        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            pr_username = prefs.getString("username", null);
            pr_name = prefs.getString("name", null);
        }



        submit = (Button) findViewById(R.id.submit);
        msg = (EditText)findViewById(R.id.msg);
        email = (EditText)findViewById(R.id.email);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (email.getText()==null  ||  email.getText().toString().length()< 0){

                    Toast.makeText(SupportCenter.this, "Please enter email.", Toast.LENGTH_SHORT).show();

                }else if (msg.getText()==null  ||  msg.getText().toString().length()<0){

                    Toast.makeText(SupportCenter.this, "Please enter description.", Toast.LENGTH_SHORT).show();

                } else  {
                    SupportCenterMethod();
                }
            }
        });
    }


    private void buildDialog(int animationSource, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage(type);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        dialog.show();

    }




    public void SupportCenterMethod(){




        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("sp_email", email.getText().toString())
                        .add("sp_msg", msg.getText().toString())
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


                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getString("status").equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(SupportCenter.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (object.getString("status").equals("1")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    buildDialog(R.style.dialog_animations, "Your message Submitted Successfully. You will be contacted by our team via provided email.");
                                }
                            });




                        }


                    } else {
                        Log.d(TAG, "response...." + response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SupportCenter.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

        Log.d(TAG,"support current language......"+ LocaleManager.getLanguage(this));
        Log.d(TAG,"support current locale......"+ LocaleManager.getLocale(this.getResources()));
        Log.d(TAG,"support current locale......"+ LocaleManager.getLocale(this.getResources()).getLanguage());

       /* String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }*/
    }

}
