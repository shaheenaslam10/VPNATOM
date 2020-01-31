package com.next.sheharyar;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atom.core.models.AccessToken;
import com.next.sheharyar.APIs.APIs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        GetAccessToken();
    }

    public void AddHandler(){

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(SplashScreen.this, WelcomeActivity.class);
                startActivity(splash);
                finish();
            }
        }, 3000);
    }


    private void GetAccessToken() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(SplashScreen.this);
        HashMap<String, String> params = new HashMap<>();
        params.put("grantType", "secret");
        params.put("secretKey", getResources().getString(R.string.atom_secret_key));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, APIs.GET_ACCESS_TOKEN, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("shani","response ..... "+response);
                // Some code
                /*{"header":{"code":1,"message":"Access token created successfully!","response_code":1},"body":{"accessToken":"94e683059c3855ffe30d1b9bd03e71c2d070be2986bbf365505c4cce266d2a66","refreshToken":"9b297246947e4301bcaa7267dc95f5d62b64d559d2ee8c8d30d2701d60162bb0","expiry":3600,"resellerId":"443","resellerUid":"res_5d4d5945a6207"}}*/

                AddHandler();
                try {

                    JSONObject header = response.getJSONObject("header");
                    if (header.getString("message").equals("Access token created successfully!")) {

                        JSONObject body = response.getJSONObject("body");
                        String accessToken = body.getString("accessToken");
                        String refreshToken = body.getString("refreshToken");
                        int expiry = body.getInt("expiry");
                        String resellerId = body.getString("resellerId");
                        String resellerUid = body.getString("resellerUid");

                        AccessToken token = new AccessToken();

                        token.setAccessToken(accessToken);
                        token.setRefreshToken(refreshToken);
                        token.setExpiry(expiry);
                        token.setResellerId(resellerId);
                        token.setResellerUid(resellerUid);

                        AtomDemoApplicationController.getInstance().SetAccessToken(token);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle errors
                AddHandler();
            }
        });
        mRequestQueue.add(request);




    }



}
