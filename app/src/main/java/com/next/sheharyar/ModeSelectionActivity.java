package com.next.sheharyar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.BaseActivity;

import com.next.sheharyar.LocaleManager.LocaleManager;

import static com.atom.sdk.android.AtomManager.VPNStatus.CONNECTED;
import static com.next.sheharyar.Model.ModelMethods.IsBannedCountry;
import static com.next.sheharyar.Model.ModelMethods.IsBannedCountryInt;

public class ModeSelectionActivity extends BaseActivity {
    TextView pfn_back;
    ImageView pfn_back_image;
    RelativeLayout internet_freedom_layout, security_layout, file_sharing_layout, hd_stream_layout , share_app_layout;
    public static final String TAG = "shani";
    String from , username , language , mode_text_Pref;
    LinearLayout file_check,security_check,freedom_check,hd_check;
    boolean nothing_selected = false;

    String connected_country, country;
    boolean connected = false  , banned_country = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mode_selection);

        pfn_back = (TextView)findViewById(R.id.pfn_back);
        pfn_back_image = (ImageView)findViewById(R.id.pfn_back_image);




        Intent intent = getIntent();
        Log.d(TAG, "reponse..."+intent.getStringExtra("response"));

        if (intent!=null){
            if (intent.getStringExtra("from")!=null){
                from = intent.getStringExtra("from");
            }
        }

        file_check = (LinearLayout) findViewById(R.id.file_check);
        security_check = (LinearLayout) findViewById(R.id.security_check);
        freedom_check = (LinearLayout) findViewById(R.id.freedom_check);
        hd_check = (LinearLayout) findViewById(R.id.hd_check);

        if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equals(CONNECTED)){
            connected = true;
            country = AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails().getCountry();
            Log.d(TAG,"connected country............"+country);

            banned_country = IsBannedCountry(country);


        }else {connected = false;}


        SharedPreferences shared_mode = getSharedPreferences("Mode", MODE_PRIVATE);
        if (shared_mode != null) {
            if (shared_mode.getString("selected_mode", null) != null){

                mode_text_Pref = shared_mode.getString("selected_mode", null);
                if (mode_text_Pref.equals("freedom")){

                    file_check.setVisibility(View.GONE);
                    security_check.setVisibility(View.GONE);
                    freedom_check.setVisibility(View.VISIBLE);
                    hd_check.setVisibility(View.GONE);

                }else if (mode_text_Pref.equals("security")){

                    file_check.setVisibility(View.GONE);
                    security_check.setVisibility(View.VISIBLE);
                    freedom_check.setVisibility(View.GONE);
                    hd_check.setVisibility(View.GONE);

                }else if (mode_text_Pref.equals("file_sharing")){

                    file_check.setVisibility(View.VISIBLE);
                    security_check.setVisibility(View.GONE);
                    freedom_check.setVisibility(View.GONE);
                    hd_check.setVisibility(View.GONE);

                }else if (mode_text_Pref.equals("hd_stream")){

                    file_check.setVisibility(View.GONE);
                    security_check.setVisibility(View.GONE);
                    freedom_check.setVisibility(View.GONE);
                    hd_check.setVisibility(View.VISIBLE);

                }else {
                    file_check.setVisibility(View.GONE);
                    security_check.setVisibility(View.GONE);
                    freedom_check.setVisibility(View.GONE);
                    hd_check.setVisibility(View.GONE);
                }
            }else {

                nothing_selected = true;
                pfn_back.setVisibility(View.GONE);
                pfn_back_image.setVisibility(View.GONE);

                file_check.setVisibility(View.GONE);
                security_check.setVisibility(View.GONE);
                freedom_check.setVisibility(View.GONE);
                hd_check.setVisibility(View.GONE);
            }
        }else {

            nothing_selected = true;
            pfn_back.setVisibility(View.GONE);
            pfn_back_image.setVisibility(View.GONE);

            file_check.setVisibility(View.GONE);
            security_check.setVisibility(View.GONE);
            freedom_check.setVisibility(View.GONE);
            hd_check.setVisibility(View.GONE);
        }

        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            username = prefs.getString("username", null);
            Log.d(TAG,"username...."+username);

            if (prefs.getString("last_connection",null)!=null) {

                if (prefs.getString("last_connection", null).equals("country")) {
                    banned_country = IsBannedCountry(prefs.getString("country", null));
                    country = prefs.getString("country", null);
                } else if (prefs.getString("last_connection", null).equals("city")) {
                    banned_country = IsBannedCountryInt(Integer.parseInt(prefs.getString("countryId", null)));
                    country = prefs.getString("cityName", null);
                }
            }else {
                banned_country = false;
            }
        }else {
            banned_country = false;
        }

        share_app_layout = (RelativeLayout)findViewById(R.id.share_app_layout);





        internet_freedom_layout = (RelativeLayout)findViewById(R.id.internet_freedom_layout);
        internet_freedom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file_check.setVisibility(View.GONE);
                security_check.setVisibility(View.GONE);
                freedom_check.setVisibility(View.VISIBLE);
                hd_check.setVisibility(View.GONE);

                if (from != null && from.equals("login")){
                    Bundle bundle = new Bundle();
                    bundle.putString("mode","freedom");

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    CountrySelectionFragment fragment = new CountrySelectionFragment();
                    fragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right ,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.commit();

                }else {
                    Intent intent = new Intent(ModeSelectionActivity.this,MainAppActivity.class);
                    intent.putExtra("mode","freedom");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                SharedPreferences mPrefs_put = getSharedPreferences("Mode", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                prefsEditor.putString("selected_mode", "freedom");
                prefsEditor.commit();

            }
        });
        security_layout = (RelativeLayout)findViewById(R.id.security_layout);
        security_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file_check.setVisibility(View.GONE);
                security_check.setVisibility(View.VISIBLE);
                freedom_check.setVisibility(View.GONE);
                hd_check.setVisibility(View.GONE);


                if (from!=null && from.equals("login")){
                    Bundle bundle = new Bundle();
                    bundle.putString("mode","security");

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    CountrySelectionFragment fragment = new CountrySelectionFragment();
                    fragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right ,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.commit();
                }else {
                    Intent intent = new Intent(ModeSelectionActivity.this,MainAppActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("mode","security");
                    startActivity(intent);
                    finish();
                }

                SharedPreferences mPrefs_put = getSharedPreferences("Mode", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                prefsEditor.putString("selected_mode", "security");
                prefsEditor.commit();

            }
        });
        file_sharing_layout = (RelativeLayout)findViewById(R.id.file_sharing_layout);
        file_sharing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (banned_country){

                    String msg = "You have selected \"" + country + "\". And File sharing is banned in this country. You can select another mode or create connection with another country.";
                    new AlertDialog.Builder(ModeSelectionActivity.this)
                            .setTitle("Not Possible")
                            .setMessage(msg)

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    dialog.dismiss();
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }else {

                    SharedPreferences mPrefs_put = getSharedPreferences("Mode", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                    prefsEditor.putString("selected_mode", "file_sharing");
                    prefsEditor.commit();

                    file_check.setVisibility(View.VISIBLE);
                    security_check.setVisibility(View.GONE);
                    freedom_check.setVisibility(View.GONE);
                    hd_check.setVisibility(View.GONE);

                    if (from!=null && from.equals("login")){
                        Bundle bundle = new Bundle();
                        bundle.putString("mode","file_sharing");

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        CountrySelectionFragment fragment = new CountrySelectionFragment();
                        fragment.setArguments(bundle);
                        transaction.addToBackStack(null);
                        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right ,
                                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        transaction.replace(R.id.frameLayout, fragment);
                        transaction.commit();
                    }else {
                        Intent intent = new Intent(ModeSelectionActivity.this,MainAppActivity.class);
                        intent.putExtra("mode","file_sharing");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }


                }
            }
        });

        hd_stream_layout = (RelativeLayout)findViewById(R.id.hd_stream_layout);
        hd_stream_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences mPrefs_put = getSharedPreferences("Mode", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                prefsEditor.putString("selected_mode", "hd_stream");
                prefsEditor.commit();

                file_check.setVisibility(View.GONE);
                security_check.setVisibility(View.GONE);
                freedom_check.setVisibility(View.GONE);
                hd_check.setVisibility(View.VISIBLE);

                if (from!=null && from.equals("login")){
                    Bundle bundle = new Bundle();
                    bundle.putString("mode","hd_stream");

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    CountrySelectionFragment fragment = new CountrySelectionFragment();
                    fragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right ,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.commit();
                }else {
                    Intent intent = new Intent(ModeSelectionActivity.this,MainAppActivity.class);
                    intent.putExtra("mode","hd_stream");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }




            }
        });


        pfn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences mPrefs_put = getSharedPreferences("Mode", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                prefsEditor.putString("selected_mode", mode_text_Pref);
                prefsEditor.commit();


              // finish();
                if (from != null && from.equals("login")){
                    Bundle bundle = new Bundle();
                    bundle.putString("mode",mode_text_Pref);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    CountrySelectionFragment fragment = new CountrySelectionFragment();
                    fragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right ,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.commit();

                }else {
                    Intent intent = new Intent(ModeSelectionActivity.this,MainAppActivity.class);
                    intent.putExtra("mode",mode_text_Pref);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }


            }
        });
        pfn_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();


                SharedPreferences mPrefs_put = getSharedPreferences("Mode", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                prefsEditor.putString("selected_mode", mode_text_Pref);
                prefsEditor.commit();

                if (from != null && from.equals("login")){
                    Bundle bundle = new Bundle();
                    bundle.putString("mode",mode_text_Pref);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    CountrySelectionFragment fragment = new CountrySelectionFragment();
                    fragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right ,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.commit();

                }else {
                    Intent intent = new Intent(ModeSelectionActivity.this,MainAppActivity.class);
                    intent.putExtra("mode",mode_text_Pref);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }



            }
        });




        share_app_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String shareBody = getResources().getString(R.string.share_text) +username;


                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(txtIntent ,"Share link!"));
            }
        });


    }


    @Override
    public void onBackPressed() {


        if (nothing_selected){

            new AlertDialog.Builder(ModeSelectionActivity.this)
                    .setTitle("Close")
                    .setMessage("Do you wanna close the application ?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            dialog.dismiss();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_delete)
                    .show();

        }else {

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
            }


        }



    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"current language......"+ LocaleManager.getLanguage(this));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()).getLanguage());

        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }

    }



}
