package com.next.sheharyar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atom.core.exceptions.AtomValidationException;
import com.atom.core.models.AccessToken;
import com.atom.core.models.AtomConfiguration;
import com.atom.sdk.android.AtomManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import com.next.sheharyar.APIs.APIs;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import cdflynn.android.library.checkview.CheckView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edituser;
    ShowHidePasswordEditText editpass;
    TextView login_id, backText, forgotPassword, done_loginForm;
    ImageView backImageButton;
    String name, uu_id,  username, email, company, address, address2, city, state, country, pref_username, pass;
    ProgressDialog progress;
    public static final String TAG = "shani";
    private static String uniqueID = null;
    String language;

    LinearLayout success_layout, login_layout;
    CheckView check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Log.d(TAG, "***********   LoginActivity  *************");


        SharedPreferences prefs = getSharedPreferences("PREF_UNIQUE_ID", MODE_PRIVATE);
        if (prefs != null) {
            uniqueID = prefs.getString("PREF_UNIQUE_ID", null);
            Log.d(TAG, "uniqueID...." + uniqueID);
        }


        success_layout = (LinearLayout) findViewById(R.id.success_layout);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        check = (CheckView) findViewById(R.id.check);

        success_layout.setVisibility(View.GONE);
        login_layout.setVisibility(View.VISIBLE);
        check.uncheck();

        edituser = (EditText) findViewById(R.id.edtuser);
        editpass = (ShowHidePasswordEditText) findViewById(R.id.edtpass);
        login_id = (TextView) findViewById(R.id.loginscreen_id);
        backText = (TextView) findViewById(R.id.back_text);
        backImageButton = (ImageView) findViewById(R.id.back_image);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        done_loginForm = (TextView) findViewById(R.id.done_loginForm);


        done_loginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edituser.getText().toString().trim().length() == 0) {
                    edituser.setError("Username is not entered");
                    edituser.requestFocus();
                }
                if (editpass.getText().toString().trim().length() == 0) {
                    editpass.setError("Password is not entered");
                    editpass.requestFocus();
                } else {

                    Log.d(TAG, "else");


                    progress = new ProgressDialog(LoginActivity.this);
                    progress.setMessage("Loading");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();


                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            OkHttpClient client = new OkHttpClient();

                            RequestBody formBody = new FormBody.Builder()
                                    .add("lg_email", edituser.getText().toString())
                                    .add("lg_password", editpass.getText().toString())
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


                                    if (response.toString().contains("<b>")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.dismiss();
                                                Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    JSONObject object = new JSONObject(response.body().string());

                                    Log.d(TAG, "after object...");

                                    if (object.getString("status").equals("0")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Toast.makeText(LoginActivity.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                                    progress.dismiss();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else if (object.getString("status").equals("1")) {

                                        name = object.getString("name");
                                        uu_id = object.getString("uu_id");
                                        uniqueID = uu_id;
                                        username = object.getString("username");
                                        email = object.getString("email");
                                        company = object.getString("company");
                                        address = object.getString("address");
                                        address2 = object.getString("address2");
                                        city = object.getString("city");
                                        state = object.getString("state");
                                        country = object.getString("country");
                                        pass = editpass.getText().toString();

                                        SharedPreferences.Editor editor_id = getSharedPreferences("PREF_UNIQUE_ID", MODE_PRIVATE).edit();
                                        editor_id.putString("PREF_UNIQUE_ID", uu_id);
                                        editor_id.apply();

                                        if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {



                                            SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                                            editor.putString("name", name);
                                            editor.putString("uu_id", uu_id);
                                            uniqueID = uu_id;
                                            editor.putString("username", username);
                                            editor.putString("email", email);
                                            editor.putString("company", company);
                                            editor.putString("address", address);
                                            editor.putString("address2", address2);
                                            editor.putString("city", city);
                                            editor.putString("state", state);
                                            editor.putString("country", country);
                                            editor.putString("pass", pass);
                                            editor.apply();

                                            if (AtomDemoApplicationController.getInstance().GetAccessToken()==null){
                                                GetAccessToken();
                                            }else {
                                                GenerateVpnAccount();
                                            }

                                        } else {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress = new ProgressDialog(LoginActivity.this);
                                                    progress.setMessage("Initializing Atom Manager");
                                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progress.setIndeterminate(true);
                                                    progress.setProgress(0);
                                                    progress.show();
                                                }
                                            });


                                            Toast.makeText(getApplicationContext(), "Initializing Atom Manager.", Toast.LENGTH_LONG).show();

                                            AtomConfiguration.Builder atomConfigurationBuilder = new AtomConfiguration.Builder("b12e0405d803ba771c46bb94be29a0a59f976b06");
                                            atomConfigurationBuilder.setVpnInterfaceName("Atom SDK Demo");
                                            AtomConfiguration atomConfiguration = atomConfigurationBuilder.build();

                                            try {
                                                AtomManager.initialize(AtomDemoApplicationController.getInstance(), atomConfiguration, new AtomManager.InitializeCallback() {
                                                    @Override
                                                    public void onInitialized(AtomManager mAtomManager) {

                                                        AtomDemoApplicationController.getInstance().SetAtomManager(mAtomManager);


                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progress.dismiss();
                                                            }
                                                        });

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                                        SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                                                        editor.putString("name", name);
                                                        editor.putString("uu_id", uu_id);
                                                        uniqueID = uu_id;
                                                        editor.putString("username", username);
                                                        editor.putString("email", email);
                                                        editor.putString("company", company);
                                                        editor.putString("address", address);
                                                        editor.putString("address2", address2);
                                                        editor.putString("city", city);
                                                        editor.putString("state", state);
                                                        editor.putString("country", country);
                                                        editor.putString("pass", pass);
                                                        editor.apply();

                                                        if (AtomDemoApplicationController.getInstance().GetAccessToken()==null){
                                                            GetAccessToken();
                                                        }else {
                                                            GenerateVpnAccount();
                                                        }

                                                    }
                                                });
                                            } catch (AtomValidationException e) {
                                                e.printStackTrace();
                                                Log.d(TAG, "exception atomManager....." + e.getMessage());
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progress.dismiss();
                                                        Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "somethings went AtomValidationException wrong.....");

                                                    }
                                                });
                                            }
                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "somethings went this wrong.....");
                                    }


                                } else {
                                    Log.d(TAG, "response...." + response);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "somethings went this else wrong.....");
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
                                        progress.dismiss();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                    }
                                });
                            }

                        }
                    });
                    thread.start();


                }

//                    Intent afterlogin = new Intent(getApplicationContext(), MainAppActivity.class);
//                    startActivity(afterlogin);
            }
        });

        login_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edituser.getText().toString().trim().length() == 0) {
                    edituser.setError("Username is not entered");
                    edituser.requestFocus();
                }
                if (editpass.getText().toString().trim().length() == 0) {
                    editpass.setError("Password is not entered");
                    editpass.requestFocus();
                }


//                else
//                {
//                    Intent afterlogin = new Intent(getApplicationContext(), xyz.class);
//                    startActivity(afterlogin);
//                }
            }
        });

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backText = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(backText);
            }
        });

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backImage = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(backImage);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_pass = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(forgot_pass);
            }
        });
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void GetAccessToken() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        HashMap<String, String> params = new HashMap<>();
        params.put("grantType", "secret");
        params.put("secretKey", getResources().getString(R.string.atom_secret_key));

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, APIs.GET_ACCESS_TOKEN, new JSONObject(params), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("shani","response ..... "+response);
                // Some code
                /*{"header":{"code":1,"message":"Access token created successfully!","response_code":1},"body":{"accessToken":"94e683059c3855ffe30d1b9bd03e71c2d070be2986bbf365505c4cce266d2a66","refreshToken":"9b297246947e4301bcaa7267dc95f5d62b64d559d2ee8c8d30d2701d60162bb0","expiry":3600,"resellerId":"443","resellerUid":"res_5d4d5945a6207"}}*/

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

                        GenerateVpnAccount();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("shani","Error GetAccessToken "+e.getMessage());
                    Toast.makeText(LoginActivity.this, " Error occurred.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle errors
                Log.d("shani","Error GetAccessToken VolleyError "+error.getMessage());
                Toast.makeText(LoginActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);



    }



    public void GenerateVpnAccount() {

        Log.d(TAG,"uniqueID..........."+uniqueID);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("uuid", uniqueID)
                .add("period", "180")
                .build();
        Request request = new Request.Builder()
                .url(APIs.GENERATE_VPN_ACCOUNT)
                .header("X-AccessToken", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken())
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
                        progress.dismiss();
                    }
                });

                // {"header":{"code":1,"message":"VPN account has been generated","response_code":1},"body":{"vpnUsername":"partner2720s7971417","vpnPassword":"p7h2dtgr"}}

                JSONObject object = new JSONObject(response.body().string());

                Log.d(TAG,"response ......... "+object);

                if (object.getJSONObject("header").getString("message").equals("VPN account has been generated")) {


                    Log.d(TAG, "message......." + object.getJSONObject("header").getString("message"));
                    Log.d(TAG, "message......." + object.getJSONObject("body").getString("vpnUsername"));

                    String vpn_username = object.getJSONObject("body").getString("vpnUsername");
                    String vpn_password = object.getJSONObject("body").getString("vpnPassword");

                    AtomDemoApplicationController.getInstance().getAtomManager().setUUID(uniqueID);

                    SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                    editor.putString("vpn_username", vpn_username);
                    editor.putString("vpn_password", vpn_password);
                    editor.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            login_layout.setVisibility(View.GONE);
                            success_layout.setVisibility(View.VISIBLE);

                            Handler handler = new Handler();
                            Handler handler1 = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    check.check();
                                }
                            }, 500);
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), ModeSelectionActivity.class);
                                    intent.putExtra("from", "login");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                }
                            }, 3000);

                        }
                    });


                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "somethings went wrong account generate....");
                        }
                    });

                }

                Log.d(TAG, "after object...");


            } else {
                Log.d(TAG, "response...." + response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "somethings went wrong account generate after....");
                    }
                });
            }
            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "exception...." + e.getMessage());

            //CLEARTEXT communication to api.atom.purevpn.com not permitted by network security policy
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "current language......" + LocaleManager.getLanguage(this));
        Log.d(TAG, "current locale......" + LocaleManager.getLocale(this.getResources()));
        Log.d(TAG, "current locale......" + LocaleManager.getLocale(this.getResources()).getLanguage());

        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)) {


            SharedPreferences shared_mode = getSharedPreferences("connection", MODE_PRIVATE);
            if (shared_mode != null) {
                if (shared_mode.getString("language", null) != null) {
                    LocaleManager.setNewLocale(this, shared_mode.getString("language", null));
                    startActivity(getIntent());
                    finish();
                }
            }

        }


    }
}
