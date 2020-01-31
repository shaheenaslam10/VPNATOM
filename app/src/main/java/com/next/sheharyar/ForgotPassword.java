package com.next.sheharyar;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.next.sheharyar.LocaleManager.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.next.sheharyar.APIs.APIs;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPassword extends BaseActivity {

    TextView backToLoginPage, doneTextAppBar;
    ImageView backToLoginImage;
    AlertDialog alertDialog;
    EditText edtuser;
    public static final String TAG = "shani";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        backToLoginImage = (ImageView)findViewById(R.id.back_login_image);
        backToLoginPage = (TextView)findViewById(R.id.back__login_text);
        edtuser = (EditText) findViewById(R.id.edtuser);
        doneTextAppBar = (TextView)findViewById(R.id.done_text);
        doneTextAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtuser.getText()==null ||  edtuser.getText().toString().length()<1){
                    Toast.makeText(ForgotPassword.this, "Please enter email.", Toast.LENGTH_SHORT).show();
                    edtuser.setError("Please enter email.");
                }/*else if (!com.next.sheharyar.APIs.isValidEmail(edtuser.getText().toString())){
                    Toast.makeText(ForgotPassword.this, "Please enter correct email.", Toast.LENGTH_SHORT).show();
                    edtuser.setError("Enter correct email.");
                }*/else {

                    ForgotMethod();
                }

            }
        });
        backToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        backToLoginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void buildDialog(int animationSource, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Animation Dialog");
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
    
    
    
    public void ForgotMethod(){



            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormBody.Builder()
                            .add("fg_email", edtuser.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url(APIs.GENERAL_URL)
                            .post(formBody)
                            .build();

                    Log.d(TAG,"before call");


                    try {
                        Response response = client.newCall(request).execute();



                        if (response.isSuccessful()) {

                            Log.d(TAG,"response....");
                           // Log.d(TAG,"response...."+response.body().string());

                                                            /*{
                                                             "status": 0,
                                                             "status_message": "Please Enter Correct correct Username"
                                                         }*/


                            JSONObject object = new JSONObject(response.body().string());

                            if (object.getString("status").equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(ForgotPassword.this, object.getString("status_message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else if (object.getString("status").equals("1")){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        buildDialog(R.style.dialog_animations, "Please check your email.  If it doesn’t arrive please check your spam folder before contacting support");
                                    }
                                });

                            }




                        } else {
                            Log.d(TAG,"response...."+response);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ForgotPassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        // Do something with the response.
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG,"exception...."+e.getMessage());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
        
        
        
        
    }
}
