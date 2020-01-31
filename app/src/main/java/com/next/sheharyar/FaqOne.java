package com.next.sheharyar;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.next.sheharyar.APIs.APIs;
import com.next.sheharyar.LocaleManager.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaqOne extends BaseActivity {

    TextView FaqBack , ques , ans;
    ImageView FaqBackImage;
    public static final String TAG = "shani";
    RelativeLayout progress_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_faq_one);



        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        ans = (TextView)findViewById(R.id.ans);
        FaqBack = (TextView)findViewById(R.id.faq_main_back);
        ques = (TextView)findViewById(R.id.ques);
        FaqBackImage = (ImageView)findViewById(R.id.faq_back_main_image);

        FaqBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FaqBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent!=null){
            ques.setText(intent.getStringExtra("title"));
            ans.setText(intent.getStringExtra("desc"));

            FAQMethodAns(intent.getStringExtra("title"));
        }
    }



    public void FAQMethodAns(String title){


        ans.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("faq_title", title)
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
                            ans.setVisibility(View.VISIBLE);
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
                                        Toast.makeText(FaqOne.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (object.getString("status").equals("1")) {


                            JSONArray description_array = new JSONArray(object.getString("description"));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ans.setText(String.valueOf(description_array.get(0)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d(TAG,"setTexterror"+e.getMessage());
                                    }
                                }
                            });


                        }


                    } else {
                        Log.d(TAG, "response...." + response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FaqOne.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                            ans.setVisibility(View.VISIBLE);
                            progress_layout.setVisibility(View.GONE);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();


    }
}
