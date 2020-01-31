package com.next.sheharyar;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.Model.FaqModel;
import com.next.sheharyar.adapter.FaqAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaqActivity extends BaseActivity {


    TextView PfnBackText;
    ImageView PfnBackImage;
    public static final String TAG = "shani";
    RelativeLayout progress_layout;
    RecyclerView recyclerView;
    ArrayList<FaqModel> arrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FaqAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_faq);

        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        PfnBackText = (TextView)findViewById(R.id.pfn_main_back);
        PfnBackImage = (ImageView)findViewById(R.id.pfn_back_main_image);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        PfnBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        PfnBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        FAQMethod();
    }



    public void FAQMethod(){


        recyclerView.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("faq", "1")
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress_layout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        });

                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getString("status").equals("0")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(FaqActivity.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (object.getString("status").equals("1")) {


                            JSONArray title_array = new JSONArray(object.getString("title"));


                            for (int i = 0; i <title_array.length() ; i++) {


                                FaqModel model = new FaqModel(title_array.getString(i));
                                arrayList.add(model);
                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SetAdapter(arrayList);

                                }
                            });

                        }


                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FaqActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    // Do something with the response.
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });
        thread.start();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void SetAdapter(ArrayList<FaqModel> arrayList){

        progress_layout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter = new FaqAdapter(arrayList,FaqActivity.this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(FaqActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    String language;
    @Override
    protected void onResume() {
        super.onResume();


        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }

    }

}