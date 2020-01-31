package com.next.sheharyar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.atom.core.models.Country;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.atom.sdk.android.AtomManager;
import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.logger.LogFragment;
import com.next.sheharyar.logger.LogWrapper;
import com.next.sheharyar.logger.MessageOnlyLogFilter;

public class LocationCountriesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LogFragment logFragment;
    public LogWrapper logWrapper;
    SearchView editsearch;
    ListView countryList; //listview
    TextView app_bottom_Connect_text, hd_stream_header;
    ImageView app_bottom_Connect, navigation_right_arrow;
    public static final String TAG = "shani";
    Button button_cities,button_countries;
    String mode;
    String mode_text , username;
    String mode_text_Pref;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_countries);


        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getStringExtra("mode");
        }

        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            username = prefs.getString("username", null);
        }



        if (mode!=null){

            if (mode.equals("freedom")){

                mode_text = "Internet Freedom";

            }else if (mode.equals("security")){

                mode_text = "Security/Privacy";

            }else if (mode.equals("file_sharing")){

                mode_text = "File Sharing";

            }else if (mode.equals("hd_stream")){

                mode_text = "HD Stream";

            }
        }

        SharedPreferences shared_mode = getSharedPreferences("Mode", MODE_PRIVATE);
        if (shared_mode != null) {
            if (shared_mode.getString("selected_mode", null) != null){

                mode_text_Pref = shared_mode.getString("selected_mode", null);
                if (mode_text_Pref.equals("freedom")){

                    mode_text = "Internet Freedom";

                }else if (mode_text_Pref.equals("security")){

                    mode_text = "Security/Privacy";

                }else if (mode_text_Pref.equals("file_sharing")){

                    mode_text = "File Sharing";

                }else if (mode_text_Pref.equals("hd_stream")){

                    mode_text = "HD Stream";

                }
            }
        }


        button_countries = (Button) findViewById(R.id.button_countries);
        button_cities = (Button)findViewById(R.id.button_cities);
        app_bottom_Connect_text = (TextView)findViewById(R.id.app_bottom_quickConnect_Text);
        app_bottom_Connect = (ImageView)findViewById(R.id.app_bottom_quickConnect);

        app_bottom_Connect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent app_bottom_connect = new Intent(getApplicationContext(), MainAppActivity.class);
                app_bottom_connect.putExtra("mode",mode);
                startActivity(app_bottom_connect);
            }
        });

        app_bottom_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent app_bottom_onnect_image = new Intent(getApplicationContext(), MainAppActivity.class);
                app_bottom_onnect_image.putExtra("mode",mode);
                startActivity(app_bottom_onnect_image);
            }
        });

        button_cities.setBackground(ContextCompat.getDrawable(LocationCountriesActivity.this, R.drawable.transparentbutton));
        button_countries.setBackgroundColor(getResources().getColor(R.color.track_color));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CountriesFragment fragment = new CountriesFragment();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();


        button_cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_countries.setBackground(ContextCompat.getDrawable(LocationCountriesActivity.this, R.drawable.transparentbutton));
                button_cities.setBackgroundColor(getResources().getColor(R.color.track_color));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                CitiesFragment fragment = new CitiesFragment();
                transaction.replace(R.id.frame, fragment);
                transaction.commit();
            }
        });
        button_countries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_cities.setBackground(ContextCompat.getDrawable(LocationCountriesActivity.this, R.drawable.transparentbutton));
                button_countries.setBackgroundColor(getResources().getColor(R.color.track_color));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                CountriesFragment fragment = new CountriesFragment();
                transaction.replace(R.id.frame, fragment);
                transaction.commit();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.White));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        hd_stream_header = (TextView)headerview.findViewById(R.id.hd_stream_header);
        hd_stream_header.setText(mode_text);
        hd_stream_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckConnectionAndNavigate();
            }
        });
        navigation_right_arrow = (ImageView) headerview.findViewById(R.id.nav_header_forward_icon);
        navigation_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckConnectionAndNavigate();
            }
        });

        Menu menu = navigationView.getMenu();
        if (mode!=null){

            if (mode.equals("freedom")){

                menu.findItem(R.id.nav_p2p).setVisible(false);

            }else if (mode.equals("security")){

                menu.findItem(R.id.nav_p2p).setVisible(false);

            }else if (mode.equals("file_sharing")){

                menu.findItem(R.id.nav_p2p).setVisible(false);

            }else if (mode.equals("hd_stream")){

                menu.findItem(R.id.nav_p2p).setVisible(false);

            }
        }



    }


    public void CheckConnectionAndNavigate(){

        if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {

            new AlertDialog.Builder(LocationCountriesActivity.this)
                    .setTitle("Connected to PFN")
                    .setMessage("You have to disconnect to change mood. Do you want to disconnect?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation

                            AtomDemoApplicationController.getInstance().getAtomManager().disconnect(getApplicationContext());


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent mod_Text = new Intent(getApplicationContext(), ModeSelectionActivity.class);
                                    startActivity(mod_Text);
                                }
                            },2000);



                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Continue with delete operation
                    dialog.dismiss();
                }
            })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else {
            Intent mod_Text = new Intent(getApplicationContext(), ModeSelectionActivity.class);
            startActivity(mod_Text);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_settings) {

            Intent nav_settings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(nav_settings);

        } else if (id == R.id.nav_faq) {

            Intent nav_faq = new Intent(getApplicationContext(), FaqActivity.class);
            startActivity(nav_faq);

        }else if (id == R.id.nav_p2p) {

            Intent nav_faq = new Intent(getApplicationContext(), P2PActivity.class);
            startActivity(nav_faq);

        }else if (id == R.id.share) {

           /* Spanned link_ready = null;
            String link = "https://user.theprivatefamilynetwork.com/registrations/mobile/"+username;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                link_ready = Html.fromHtml("<a href='"+link+"'>"+link+"</a>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                link_ready = Html.fromHtml("<a href='"+link+"'>"+link+"</a>");
            }*/

            String shareBody = getResources().getString(R.string.share_text) +username;


            Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
            txtIntent .setType("text/plain");
            txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(txtIntent ,"Share link!"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initializeLogging() {
        // Wraps Android's native log framework.
        try {
            logWrapper = new LogWrapper();
            // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
            com.next.sheharyar.logger.Log.setLogNode(logWrapper);


            // Filter strips out everything except the message text.
            MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
            logWrapper.setNext(msgFilter);

            // On screen logging via a fragment with a TextView.

            logFragment = (LogFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.log_fragment);
            msgFilter.setNext(logFragment.getLogView());

            ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
            output.setDisplayedChild(1);

        } catch (Exception e) {

        }
    }

    public void countryClick(Country country) {

        Log.d(TAG,"selected....."+country.getName());
        Log.d(TAG,"selected....."+country.getProtocols());

        SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
        editor.putString("country", String.valueOf(country.getName()));
        editor.apply();

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
            LocaleManager.setNewLocale(this, LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }

    }

}
