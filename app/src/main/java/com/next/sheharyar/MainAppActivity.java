package com.next.sheharyar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import com.atom.core.exceptions.AtomException;
import com.atom.core.exceptions.AtomValidationException;
import com.atom.core.models.Channel;
import com.atom.core.models.Country;
import com.atom.core.models.Protocol;
import com.atom.sdk.android.data.callbacks.CollectionCallback;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atom.sdk.android.AtomManager;
import com.atom.sdk.android.ConnectionDetails;
import com.atom.sdk.android.VPNProperties;
import com.atom.sdk.android.VPNStateListener;
import com.google.gson.Gson;
import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.Model.ChannelListModel;
import com.next.sheharyar.Model.PInfo;
import com.next.sheharyar.SqlieDataBase.DatabaseHelper;
import com.next.sheharyar.logger.LogFragment;
import com.next.sheharyar.logger.LogWrapper;
import com.next.sheharyar.logger.MessageOnlyLogFilter;
import com.skyfishjy.library.RippleBackground;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.atom.sdk.android.AtomManager.VPNStatus.CONNECTING;

public class MainAppActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, VPNStateListener, View.OnClickListener {

    public RippleBackground rippleBackground;
    LogFragment logFragment;
    public LogWrapper logWrapper;
    public static final String TAG = "shani";
    String mode_text;
    private com.android.volley.RequestQueue mRequestQueue;
    TextView hd_stream_header_text, hd_stream_settings, hd_stream_faq, file_sharing_p2p;
    List<Protocol> protocolList;
    List<Country> countriesList;
    int connection_type;
    TextView navigation_header_filesharing;
    ImageView navigation_right_arrow;
    String sP_country;
    Country c_Country;
    ImageView connect;
    TextView s_country, status, mod_selection_text;
    static TextView channel_open_channel;
    Protocol c_Protocol_udp, c_Protocol_tcp;
    String language;
    String mode = null, username, vpn_username;
    String mode_text_Pref;
    EditText dedicated_host;
    DrawerLayout drawer;
    Toolbar toolbar, bottom_toolbar;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String uniqueID = null;
    DatabaseHelper databaseHelper;
    String channel_url = null;
    Context that = this;
    LinearLayout location_container, connect_container, mode_click, channel_container;

    public MainAppActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        Log.d(TAG, "***********   MainApp  *************");

        AtomManager.addVPNStateListener(this);
        new Handler().postDelayed(() -> AtomDemoApplicationController.getInstance().getAtomManager().bindIKEVStateService(getApplicationContext()), 500);


        that = this;

        initializeLogging();
//        buttonConnectCall();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottom_toolbar = (Toolbar) findViewById(R.id.bottom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        initializeLogging();

        databaseHelper = new DatabaseHelper(MainAppActivity.this);


        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.containsKey("connection_type")) {
                    connection_type = extras.getInt("connection_type");
                }
            }
        }


        mod_selection_text = (TextView) findViewById(R.id.mod_selection_text);

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getStringExtra("mode");
        }

        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            sP_country = prefs.getString("country", null);
            username = prefs.getString("username", null);
            vpn_username = prefs.getString("vpn_username", null);
        }

        dedicated_host = (EditText) findViewById(R.id.dedicated_host);


        if (mode != null) {

            if (mode.equals("freedom")) {

                mode_text = "Internet Freedom";

            } else if (mode.equals("security")) {

                mode_text = "Security/Privacy";

            } else if (mode.equals("file_sharing")) {

                mode_text = "File Sharing";

            } else if (mode.equals("hd_stream")) {

                mode_text = "HD Stream";

            } else if (mode.equals("dedicated_id")) {

                mode_text = "Dedicated IP";

            }
        }

        SharedPreferences shared_mode = getSharedPreferences("Mode", MODE_PRIVATE);
        if (shared_mode != null) {
            if (shared_mode.getString("selected_mode", null) != null) {

                mode_text_Pref = shared_mode.getString("selected_mode", null);
                if (mode_text_Pref.equals("freedom")) {

                    mode_text = "Internet Freedom";

                } else if (mode_text_Pref.equals("security")) {

                    mode_text = "Security/Privacy";

                } else if (mode_text_Pref.equals("file_sharing")) {

                    mode_text = "File Sharing";

                } else if (mode_text_Pref.equals("hd_stream")) {

                    mode_text = "HD Stream";

                } else if (mode_text_Pref.equals("dedicated_id")) {

                    mode_text = "Dedicated IP";
                    dedicated_host.setVisibility(View.VISIBLE);

                }
            }
        }


        status = (TextView) findViewById(R.id.status);

        channel_container = (LinearLayout) findViewById(R.id.channel_container);
        location_container = (LinearLayout) findViewById(R.id.location_container);
        connect_container = (LinearLayout) findViewById(R.id.connect_container);
        mode_click = (LinearLayout) findViewById(R.id.mode_click);


        channel_open_channel = (TextView) findViewById(R.id.channel_open_channel);
        s_country = (TextView) findViewById(R.id.s_country);
        hd_stream_header_text = (TextView) findViewById(R.id.hd_stream_header);
        hd_stream_settings = (TextView) findViewById(R.id.nav_settings);
        hd_stream_faq = (TextView) findViewById(R.id.nav_faq);

        file_sharing_p2p = (TextView) findViewById(R.id.nav_p2p);

        channel_open_channel.setVisibility(View.GONE);

        mRequestQueue = Volley.newRequestQueue(MainAppActivity.this);


        channel_container.setOnClickListener(this);
        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.White));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        View headerview = navigationView.getHeaderView(0);
        navigation_header_filesharing = (TextView) headerview.findViewById(R.id.hd_stream_header);
        navigation_header_filesharing.setText(mode_text);
        mod_selection_text.setText(mode_text);
        navigation_header_filesharing.setOnClickListener(new View.OnClickListener() {
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

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        if (mode != null) {

            if (mode.equals("freedom")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("security")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("file_sharing")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("hd_stream")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("dedicated_id")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            }
        }


        rippleBackground = (RippleBackground) findViewById(R.id.content);
        final Handler handler = new Handler();

        connect = (ImageView) findViewById(R.id.center_button);
        rippleBackground.startRippleAnimation();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                button.performClick();
            }
        }, 3000);

        Log.d("shani", "country........................" + AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails().getCountry());
        Log.d("shani", "channel........................" + AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails().getChannel());
        Log.d("shani", "city........................" + AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails().getCity());

        if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {
            connect.setImageResource(R.drawable.hdstream_disconnect2);
            s_country.setText(AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails().getCountry());
            status.setText(getResources().getString(R.string.connected_to));

            UpdateStartingStatus();

        } else if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTING)) {
            connect.setImageResource(R.drawable.hd_stream_connecting);
            s_country.setText(AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails().getCountry());
            status.setText(getResources().getString(R.string.connected_to));
        }

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connect.setImageResource(R.drawable.hd_stream_connecting);
//                changeImageText(getApplicationContext(), button);
                if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {

                    if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {
                        AtomDemoApplicationController.getInstance().getAtomManager().disconnect(getApplicationContext());
                        status.setText("");
                        s_country.setText("");
                        connect.setImageResource(R.drawable.hdstream_quickconnect2);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        location_container.setOnClickListener(MainAppActivity.this);
                        mode_click.setOnClickListener(MainAppActivity.this);
                        channel_open_channel.setVisibility(View.GONE);

                    } else if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(CONNECTING)) {
                        AtomDemoApplicationController.getInstance().getAtomManager().cancel(getApplicationContext());
                        status.setText("");
                        s_country.setText("");
                        connect.setImageResource(R.drawable.hdstream_quickconnect2);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        location_container.setOnClickListener(MainAppActivity.this);
                        mode_click.setOnClickListener(MainAppActivity.this);
                        channel_open_channel.setVisibility(View.GONE);
                    } else {
                        // Put username and password here
                        try {

                            if (mode_text_Pref.equals("dedicated_id")) {

                                if (dedicated_host.getText() == null || dedicated_host.getText().toString().length() < 1) {

                                    Toast.makeText(MainAppActivity.this, "Please enter Host...", Toast.LENGTH_SHORT).show();

                                } else {

//                                    VPNProperties.Builder vpnPropertiesBuilder = new VPNProperties.Builder(dedicated_host.getText().toString(),c_Protocol_tcp);
                                    VPNProperties.Builder vpnPropertiesBuilder = new VPNProperties.Builder("us-ded-11.purevpn.net", c_Protocol_tcp);

                                    s_country.setText("");


                                    vpnPropertiesBuilder.withSecondaryProtocol(c_Protocol_udp);


                                    SharedPreferences prefs = getSharedPreferences("OnDemand", MODE_PRIVATE);
                                    if (prefs != null) {
                                        if (prefs.getString("onDemand", null) != null && prefs.getString("onDemand", null).equals("true")) {
                                            ArrayList<PInfo> list = databaseHelper.getAllAPPs();
                                            String[] app_list = new String[list.size()];
                                            boolean contains = false;
                                            Log.d("shani", "list size.........." + list.size());
                                            for (int i = 0; i < list.size(); i++) {
                                                app_list[i] = list.get(i).getPname();
                                                Log.d("shani", "app_list ..... " + app_list[i]);
                                                contains = true;
                                            }
                                            if (contains) {
                                                vpnPropertiesBuilder.withSplitTunneling(app_list);
                                            }
                                        }
                                    }

                                    Log.d(TAG, "before connection vpnPropertiesBuilder...." + vpnPropertiesBuilder);
                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                    location_container.setOnClickListener(null);
                                    mode_click.setOnClickListener(null);

                                    AtomDemoApplicationController.getInstance().getAtomManager().connect(getApplicationContext(), vpnPropertiesBuilder.build());

                                }

                            } else {


                                SharedPreferences prefs_last = getSharedPreferences("connection", MODE_PRIVATE);

                                if (prefs_last != null && prefs_last.getString("last_connection", null) != null && prefs_last.getString("last_connection", null).equals("city")) {

                                    s_country.setText(prefs_last.getString("cityName", null));

                                    CityConnection(Integer.parseInt(prefs_last.getString("countryId", null)), Integer.parseInt(prefs_last.getString("cityId", null)), prefs_last.getString("access_token", null), null);


                                } else if (prefs_last != null && prefs_last.getString("last_connection", null) != null && prefs_last.getString("last_connection", null).equals("country")){


                                    VPNProperties.Builder vpnPropertiesBuilder = new VPNProperties.Builder(c_Country, c_Protocol_tcp);

                                    s_country.setText(c_Country.getName());
                                    status.setText("Connecting");

                                    vpnPropertiesBuilder.withSecondaryProtocol(c_Protocol_udp);

                                    SharedPreferences prefs = getSharedPreferences("OnDemand", MODE_PRIVATE);
                                    if (prefs != null) {
                                        if (prefs.getString("onDemand", null) != null && prefs.getString("onDemand", null).equals("true")) {
                                            ArrayList<PInfo> list = databaseHelper.getAllAPPs();
                                            String[] app_list = new String[list.size()];
                                            boolean contains = false;
                                            Log.d("shani", "list size.........." + list.size());
                                            for (int i = 0; i < list.size(); i++) {
                                                app_list[i] = list.get(i).getPname();
                                                Log.d("shani", "app_list ..... " + app_list[i]);
                                                contains = true;
                                            }
                                            if (contains) {
                                                vpnPropertiesBuilder.withSplitTunneling(app_list);
                                            }
                                        }
                                    }
                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                    location_container.setOnClickListener(null);
                                    mode_click.setOnClickListener(null);

                                    AtomDemoApplicationController.getInstance().getAtomManager().connect(getApplicationContext(), vpnPropertiesBuilder.build());
                                }else if (prefs_last != null && prefs_last.getString("last_connection", null) != null && prefs_last.getString("last_connection", null).equals("channel")){
                                    ChannelConnection();
                                }
                            }

                        } catch (AtomValidationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        // get Protocols from Atom SDK
        if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {
            AtomDemoApplicationController.getInstance().getAtomManager().getProtocols(new CollectionCallback<Protocol>() {

                @Override
                public void onSuccess(List<Protocol> protocols) {
                    protocolList = protocols;
                    for (int i = 0; i < protocolList.size(); i++) {


                        if (protocolList.get(i).getName().equals("TCP")) {
                            c_Protocol_tcp = protocolList.get(i);
                        }
                        if (protocolList.get(i).getName().equals("UDP")) {
                            c_Protocol_udp = protocolList.get(i);
                        }

                    }

                }

                @SuppressLint("LogNotTimber")
                @Override
                public void onError(AtomException atomException) {
                    Log.d(TAG, atomException.getMessage() + " : " + atomException.getCode());

                }

                @SuppressLint("LogNotTimber")
                @Override
                public void onNetworkError(AtomException atomException) {
                    Log.d(TAG, atomException.getMessage() + " : " + atomException.getCode());
                }
            });
        }

        // get Countries from Atom SDK
        if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {

            AtomDemoApplicationController.getInstance().getAtomManager().getCountries(new CollectionCallback<Country>() {
                @Override
                public void onSuccess(List<Country> countries) {
                    countriesList = countries;

                    // 31, 49, 65,  71, 72, 172, 370, 443
                    if (countriesList != null) {

                        // displayCountries(countriesList);

                        for (int i = 0; i < countriesList.size(); i++) {
                            if (countriesList.get(i).getName().equals(sP_country)) {

                                c_Country = countriesList.get(i);

                            }
                        }

                    }

                }
                @SuppressLint("LogNotTimber")
                @Override
                public void onError(AtomException atomException){Log.e(TAG, "error" + atomException.getMessage() + " : " + atomException.getCode());}
                @SuppressLint("LogNotTimber")
                @Override
                public void onNetworkError(AtomException atomException){Log.e(TAG, "error" + atomException.getMessage() + " : " + atomException.getCode());}
            });
        }

        channel_open_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChannelsUrl();
            }
        });

    }


    public void UpdateStartingStatus(){

        SharedPreferences pref = getSharedPreferences("connection", Context.MODE_PRIVATE);
        if (pref != null) {
            String last_connection = pref.getString("last_connection", null);
            Log.d("shani","_______________________"+last_connection);
            if (last_connection!=null) {


                if (last_connection.equals("channel")) {
                    Channel channelMain = new Channel();
                    Gson gson = new Gson();
                    String st_streaming = pref.getString("channel_detail", null);

                    Log.d("shani", "channel method st_streaming....... " + st_streaming);

                    if (st_streaming != null) {

                        ChannelListModel chanel = gson.fromJson(st_streaming, ChannelListModel.class);
                        Log.d("shani", "channel method 4444....... " + st_streaming);

                        channelMain.setId(Integer.parseInt(chanel.getId()));
                        channelMain.setName(chanel.getName());
                        channelMain.setIconUrl(chanel.getIcon_url());
                        channelMain.setOrder(Integer.parseInt(chanel.getOrder()));
                        channelMain.setUrl(chanel.getChannel_url());
                        channelMain.setPackageNameAndroid(chanel.getPackage_name_android());
                        channelMain.setPackageNameAndroidTv(chanel.getPackage_name_android_tv());
                        channelMain.setPackageNameAmazonFireStick(chanel.getPackage_name_amazon_fs());

                        Log.d("shani", "channel method 3333....... " + st_streaming);

                        channel_url = chanel.getChannel_url();
                        Log.d("shani", "channel method 5555....... " + st_streaming);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                connect.setImageResource(R.drawable.hdstream_disconnect2);
                                status.setText(getResources().getString(R.string.connected_to));
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                location_container.setOnClickListener(MainAppActivity.this);
                                mode_click.setOnClickListener(MainAppActivity.this);
                                channel_open_channel.setVisibility(View.VISIBLE);
                                channel_open_channel.setText("Go to Channel Streaming");

                                String message = "You are connected to channel " + chanel.getName() + " . Do you want to navigate to this channel now?";

                                new android.app.AlertDialog.Builder(MainAppActivity.this)
                                        .setTitle("Channel")
                                        .setMessage(message)
                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("Navigate", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(chanel.getChannel_url()));
                                                startActivity(browserIntent);
                                            }
                                        })
                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        });
                        Log.d("shani", "before setText");
                        Log.d("shani", "before setText");


                    }
                } else if (last_connection.equals("country")) {

                    s_country.setText(pref.getString("country",null));
                    status.setText(getResources().getString(R.string.connected_to));
                }else if (last_connection.equals("city")) {
                    s_country.setText(pref.getString("cityName",null));
                    status.setText(getResources().getString(R.string.connected_to));
                }
            }
        }
    }


    public void ConnectMethod(Country SelectedCountry) {


        connect.setImageResource(R.drawable.hd_stream_connecting);
        if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {

            if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {
                AtomDemoApplicationController.getInstance().getAtomManager().disconnect(getApplicationContext());

            } else if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(CONNECTING)) {
                AtomDemoApplicationController.getInstance().getAtomManager().cancel(getApplicationContext());
            } else {

                try {


                    VPNProperties.Builder vpnPropertiesBuilder = new VPNProperties.Builder(SelectedCountry, c_Protocol_tcp);
                    s_country.setText(SelectedCountry.getName());

                    vpnPropertiesBuilder.withSecondaryProtocol(c_Protocol_udp);
                    SharedPreferences prefs = getSharedPreferences("OnDemand", MODE_PRIVATE);
                    if (prefs != null) {
                        if (prefs.getString("onDemand", null) != null && prefs.getString("onDemand", null).equals("true")) {
                            ArrayList<PInfo> list = databaseHelper.getAllAPPs();
                            String[] app_list = new String[list.size()];
                            boolean contains = false;
                            Log.d("shani", "list size.........." + list.size());
                            for (int i = 0; i < list.size(); i++) {
                                app_list[i] = list.get(i).getPname();
                                Log.d("shani", "app_list ..... " + app_list[i]);
                                contains = true;
                            }
                            if (contains) {
                                vpnPropertiesBuilder.withSplitTunneling(app_list);
                            }
                        }
                    }

                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    location_container.setOnClickListener(null);
                    mode_click.setOnClickListener(null);

                    AtomDemoApplicationController.getInstance().getAtomManager().connect(getApplicationContext(), vpnPropertiesBuilder.build());

                    SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                    editor.putString("last_connection", "country");
                    editor.apply();

                } catch (AtomValidationException e) {
                    e.printStackTrace();
                }


            }
        }


    }


    public void ChannelConnection() {

        SharedPreferences pref = getSharedPreferences("connection", Context.MODE_PRIVATE);
        if (pref != null) {
            String last_connection = pref.getString("last_connection", null);
            if (last_connection != null && last_connection.equals("channel")) {

                Channel channelMain = new Channel();
                Gson gson = new Gson();
                if (pref != null) {
                    String st_streaming = pref.getString("channel_detail", null);
                    if (st_streaming != null) {

                        ChannelListModel chanel = gson.fromJson(st_streaming, ChannelListModel.class);
                        channelMain.setId(Integer.parseInt(chanel.getId()));
                        channelMain.setName(chanel.getName());
                        channelMain.setIconUrl(chanel.getIcon_url());
                        channelMain.setOrder(Integer.parseInt(chanel.getOrder()));
                        channelMain.setUrl(chanel.getChannel_url());
                        channelMain.setPackageNameAndroid(chanel.getPackage_name_android());
                        channelMain.setPackageNameAndroidTv(chanel.getPackage_name_android_tv());
                        channelMain.setPackageNameAmazonFireStick(chanel.getPackage_name_amazon_fs());


                        try {

                            VPNProperties.Builder vpnPropertiesBuilder = new VPNProperties.Builder(channelMain, c_Protocol_tcp);
                            s_country.setText(chanel.getName());

                            vpnPropertiesBuilder.withSecondaryProtocol(c_Protocol_udp);
                            SharedPreferences prefs = getSharedPreferences("OnDemand", MODE_PRIVATE);
                            if (prefs != null) {
                                if (prefs.getString("onDemand", null) != null && prefs.getString("onDemand", null).equals("true")) {
                                    ArrayList<PInfo> list = databaseHelper.getAllAPPs();
                                    String[] app_list = new String[list.size()];
                                    boolean contains = false;
                                    Log.d("shani", "list size.........." + list.size());
                                    for (int i = 0; i < list.size(); i++) {
                                        app_list[i] = list.get(i).getPname();
                                        Log.d("shani", "app_list ..... " + app_list[i]);
                                        contains = true;
                                    }
                                    if (contains) {
                                        vpnPropertiesBuilder.withSplitTunneling(app_list);
                                    }
                                }
                            }

                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                            location_container.setOnClickListener(null);
                            mode_click.setOnClickListener(null);

                            AtomDemoApplicationController.getInstance().getAtomManager().connect(getApplicationContext(), vpnPropertiesBuilder.build());

                            SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                            editor.putString("last_connection", "channel");
                            editor.apply();

                        } catch (AtomValidationException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Log.d("shani", "st_streaming prefs is null..");
                    }
                } else {
                    Log.d("shani", "chanel prefs is null..");
                }

            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("shani", "request Code ..... " + requestCode);
        Log.d("shani", "resultCode ..... " + resultCode);
        Log.d("shani", "data ..... " + data);

        if (requestCode == 230) {
            if (data != null) {

                if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {
                    AtomDemoApplicationController.getInstance().getAtomManager().disconnect(getApplicationContext());
                } else if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTING)) {
                    AtomDemoApplicationController.getInstance().getAtomManager().cancel(getApplicationContext());
                }
                connect.setImageResource(R.drawable.hdstream_quickconnect2);
                s_country.setText("");
                status.setText("");

                connect.setImageResource(R.drawable.hd_stream_connecting);

                ChannelConnection();
                SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                editor.putString("last_connection", "channel");
                editor.apply();
            }
        } else if (requestCode == 220) {

            if (data == null) {
                Log.d("shani", "data is null .....");
            } else {
                if (resultCode == RESULT_OK) {
                    if (data.getStringExtra("cityId") != null) {

                        Log.d(TAG, "in City....." + data.getStringExtra("cityId"));
                        s_country.setText(data.getStringExtra("cityName"));
                        CityConnection(Integer.parseInt(data.getStringExtra("countryId")), Integer.parseInt(data.getStringExtra("cityId")), data.getStringExtra("access_token"), data.getStringArrayExtra("data_center_list"));

                        SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                        editor.putString("last_connection", "city");
                        editor.apply();

                    } else {

                        Uri country = data.getData();
                        SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                        editor.putString("last_connection", "country");
                        editor.apply();
                        for (int i = 0; i < countriesList.size(); i++) {
                            if (countriesList.get(i).getName().equals(country.getPath())) {
                                c_Country = countriesList.get(i);
                                ConnectMethod(c_Country);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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

        } else if (id == R.id.nav_p2p) {

            Intent nav_faq = new Intent(getApplicationContext(), P2PActivity.class);
            startActivity(nav_faq);

        } else if (id == R.id.share) {


            String shareBody = getResources().getString(R.string.share_text) + username;


            Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
            txtIntent.setType("text/plain");
            txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "“Share and Earn Link”");
            txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(txtIntent, "“Share and Earn Link”"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayCountries(List<Country> countries) {
        if (countries != null) {

        }
    }

    @Override
    public void onConnected() {

        Log.d("shani","connected+++++++++++++++++++++++++++++++++++");

        connect.setImageResource(R.drawable.hdstream_disconnect2);
        status.setText(getResources().getString(R.string.connected_to));
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);

        ShowChannelsUrl();
    }

    @Override
    public void onConnected(ConnectionDetails connectionDetails) {

        Log.d("shani","connected+++++++++++++++++++++++++++++++++++");

        connect.setImageResource(R.drawable.hdstream_disconnect2);
        status.setText(getResources().getString(R.string.connected_to));
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);

        ShowChannelsUrl();
    }

    public void ShowChannelsUrl(){

        Log.d("shani","channel method ....... ");

        SharedPreferences pref = getSharedPreferences("connection", Context.MODE_PRIVATE);
        if (pref != null) {
            String last_connection = pref.getString("last_connection", null);
            Log.d("shani","_______________________"+last_connection);
            if (last_connection != null && last_connection.equals("channel")) {
                Channel channelMain = new Channel();
                Gson gson = new Gson();
                    String st_streaming = pref.getString("channel_detail", null);

                Log.d("shani","channel method st_streaming....... "+st_streaming);

                if (st_streaming != null) {

                        ChannelListModel chanel = gson.fromJson(st_streaming, ChannelListModel.class);
                    Log.d("shani","channel method 4444....... "+st_streaming);

                    channelMain.setId(Integer.parseInt(chanel.getId()));
                        channelMain.setName(chanel.getName());
                        channelMain.setIconUrl(chanel.getIcon_url());
                        channelMain.setOrder(Integer.parseInt(chanel.getOrder()));
                        channelMain.setUrl(chanel.getChannel_url());
                        channelMain.setPackageNameAndroid(chanel.getPackage_name_android());
                        channelMain.setPackageNameAndroidTv(chanel.getPackage_name_android_tv());
                        channelMain.setPackageNameAmazonFireStick(chanel.getPackage_name_amazon_fs());

                    Log.d("shani","channel method 3333....... "+st_streaming);

                    channel_url = chanel.getChannel_url();
                    Log.d("shani","channel method 5555....... "+st_streaming);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            connect.setImageResource(R.drawable.hdstream_disconnect2);
                            status.setText(getResources().getString(R.string.connected_to));
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            location_container.setOnClickListener(MainAppActivity.this);
                            mode_click.setOnClickListener(MainAppActivity.this);
                            channel_open_channel.setVisibility(View.VISIBLE);
                            channel_open_channel.setText("Go to Channel Streaming");

                            String message = "You are connected to channel " + chanel.getName() + " . Do you want to navigate to this channel now?";

                            new android.app.AlertDialog.Builder(MainAppActivity.this)
                                    .setTitle("Channel")
                                    .setMessage(message)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Navigate", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(chanel.getChannel_url()));
                                            startActivity(browserIntent);
                                        }
                                    })
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    });
                        Log.d("shani","before setText");
                        Log.d("shani","before setText");


                    }
            }else {
                Log.d("shani","channel method 2222....... ");
            }
        }else {
            Log.d("shani","channel method 1111....... ");
        }
    }

    @Override
    public void onConnecting() {

        Log.d(TAG, "Connecting");
        connect.setImageResource(R.drawable.hd_stream_connecting);
        status.setText("Connecting");
    }

    @Override
    public void onRedialing(AtomException atomException, ConnectionDetails connectionDetails) {
        Log.d("shani", "Redialing...." + atomException.getMessage());
        status.setText("Redialing");

    }

    @Override
    public void onDialError(AtomException atomException, ConnectionDetails connectionDetails) {
        Log.d(TAG, "onDialError..." + atomException.getMessage());
        Log.d(TAG, "onDialError..." + atomException.getCode());
        Log.d(TAG, "onDialError..." + atomException.getCause());
        Log.d(TAG, "connectionDetails..." + connectionDetails);
        Log.d(TAG, "connectionDetails..." + connectionDetails.getUsername());
        // if (atomException.getCode() != Errors._5039) ;
//            changeButtonState(btnConnect, "Connect");

        connect.setImageResource(R.drawable.hdstream_quickconnect2);
        s_country.setText("");
        status.setText(atomException.getMessage());
        channel_open_channel.setVisibility(View.GONE);

        if (atomException.getMessage().equals("Provide VPN Credentials or a UUID") || atomException.getMessage().equals("Unable to generate VPN Credentials")) {
            if (atomException.getMessage().equals("Provide VPN Credentials or a UUID")) {
                status.setText("Provide PFN Credentials or a UUID");
            }
            if (atomException.getMessage().equals("Unable to generate VPN Credentials")) {
                status.setText("Unable to generate VPN Credentials");
            }
            ProvideCredentsDialog();

        }

        Log.d(TAG, "onDialError..." + atomException.getCause());

        if (mode_text_Pref.equals("dedicated_id")) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    new AlertDialog.Builder(MainAppActivity.this)
                            .setTitle("Failed")
                            .setMessage("Host you provided does not match. Please enter correct host...")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });


        } else {
            Log.d(TAG, "in lese exceotion.....");
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);

    }

    @Override
    public void onStateChange(String state) {
        Log.d(TAG, "state....." + state);


        if (!state.equals("Generating new user")) {
            status.setText(state);
        }

        if (state.equalsIgnoreCase(CONNECTING)) {
            connect.setImageResource(R.drawable.hd_stream_connecting);
            Log.d(TAG, "state in connecting.....");
        }
        if (state.equals("Connected")) {
            connect.setImageResource(R.drawable.hdstream_disconnect2);
            status.setText(getResources().getString(R.string.connected_to));
            Log.d(TAG, "state in connected.....");
        }
        if (state.equals("Disconnected")) {
            status.setText("");
            Log.d(TAG, "state in disconnected.....");
        }

        if (state.equals("Provide VPN Credentials or a UUID") || state.equals("Unable to generate VPN Credentials")) {

            if (state.equals("Provide VPN Credentials or a UUID")) {
                status.setText("Provide PFN Credentials or a UUID");
            }
            if (state.equals("Unable to generate VPN Credentials")) {
                status.setText("Unable to generate VPN Credentials");
            }
            ProvideCredentsDialog();
        }

    }

    @Override
    public void onDisconnected(boolean isCancelled) {
        if (isCancelled) {
            Log.d(TAG, "onCancelled" + "Cancelled");
        } else {
            Log.d(TAG, "onDisconnected....." + "Disconnected");
        }
        Log.d(TAG, "Disconnected");
        s_country.setText("");
        status.setText("");
        connect.setImageResource(R.drawable.hdstream_quickconnect2);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);
        Log.d(TAG, "1Disconnected end");
        channel_open_channel.setVisibility(View.GONE);

    }

    @Override
    public void onDisconnected(ConnectionDetails connectionDetails) {
        if (connectionDetails.isCancelled()) {
            Log.d(TAG, "onCancelled" + "Cancelled");
        } else {
            Log.d(TAG, "onDisconnected....." + "Disconnected");
        }
        Log.d(TAG, "Disconnected");
        s_country.setText("");
        status.setText("");
        connect.setImageResource(R.drawable.hdstream_quickconnect2);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);
        Log.d(TAG, "2Disconnected end");
        channel_open_channel.setVisibility(View.GONE);

    }

    @Override
    public void onUnableToAccessInternet(AtomException e, ConnectionDetails connectionDetails) {
        status.setText("No Internet");
        s_country.setText("");
        connect.setImageResource(R.drawable.hdstream_quickconnect2);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        location_container.setOnClickListener(this);
        mode_click.setOnClickListener(this);
        Log.d(TAG, "2Disconnected end");
        channel_open_channel.setVisibility(View.GONE);
    }

    @Override
    public void onPacketsTransmitted(String in, String out) {
        Log.d(TAG, "onPacketsTransmitted....." + in + " : " + out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AtomManager.removeVPNStateListener(this);
        if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {
            try {
                AtomDemoApplicationController.getInstance().getAtomManager().unBindIKEVStateService(getApplicationContext());
            } catch (Exception e) {
            }
        }
    }

    public void initializeLogging() {
        // Wraps Android's native log framework.
        try {
            logWrapper = new LogWrapper();
            com.next.sheharyar.logger.Log.setLogNode(logWrapper);
            MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
            logWrapper.setNext(msgFilter);

            logFragment = (LogFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.log_fragment);
            msgFilter.setNext(logFragment.getLogView());

            ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
            output.setDisplayedChild(1);

        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AtomDemoApplicationController.getInstance().getAtomManager().bindIKEVStateService(getApplicationContext());

        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)) {
            LocaleManager.setNewLocale(this, LocaleManager.getLanguage(this));
           /* finish();
            startActivity(getIntent());*/
        }

    }

    private void CityConnection(int countryId, int cityId, String token, String[] data_center_list) {

        //Log.d(TAG, "data center list......" + data_center_list);


        String url = "https://api.atom.purevpn.com/speedtest/v1/getPsk";
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("Accept", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {


            postparams.put("X-AccessToken", token);
            postparams.put("iCountryId", countryId);
            postparams.put("iCityId", cityId);
            postparams.put("iMcs", "1");
            postparams.put("iResellerId", "272");
            for (int i = 0; i < protocolList.size(); i++) {
                postparams.put("iProtocolNumber" + i, protocolList.get(i).getName());
            }
            postparams.put("sDeviceType", "android");
            postparams.put("sUsername", vpn_username);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Context that = this;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d(TAG, "response..........................." + response);

                            if (response != null) {

                                if (response.getJSONObject("header").getString("message").equals("Success")) {

                                    JSONObject pskBody = response.getJSONObject("body");

                                    Log.d(TAG, "psk key ...... " + pskBody.getString("psk"));


                                    String pskKey = pskBody.getString("psk");

                                    SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                                    editor.putString("city_psk", pskKey);
                                    editor.apply();


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            VPNProperties.Builder vpnPropertiesBuilder = null;
                                            try {
                                                vpnPropertiesBuilder = new VPNProperties.Builder(pskKey);
                                                SharedPreferences prefs = getSharedPreferences("OnDemand", MODE_PRIVATE);
                                                if (prefs != null) {
                                                    if (prefs.getString("onDemand", null) != null && prefs.getString("onDemand", null).equals("true")) {

                                                        ArrayList<PInfo> list = databaseHelper.getAllAPPs();
                                                        String[] app_list = new String[list.size()];
                                                        boolean contains = false;
                                                        Log.d("shani", "list size.........." + list.size());
                                                        for (int i = 0; i < list.size(); i++) {
                                                            app_list[i] = list.get(i).getPname();
                                                            Log.d("shani", "app_list ..... " + app_list[i]);
                                                            contains = true;
                                                        }
                                                        if (contains) {
                                                            vpnPropertiesBuilder.withSplitTunneling(app_list);
                                                        }

                                                    }
                                                }
                                                Log.d(TAG, "before connection vpnPropertiesBuilder...." + vpnPropertiesBuilder);
                                                status.setText("Connecting");

                                                location_container.setOnClickListener(MainAppActivity.this);
                                                mode_click.setOnClickListener(MainAppActivity.this);


                                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                                AtomDemoApplicationController.getInstance().getAtomManager().connect(getApplicationContext(), vpnPropertiesBuilder.build());
                                                SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
                                                editor.putString("last_connection", "city");
                                                editor.apply();

                                            } catch (AtomValidationException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } else if (response.getJSONObject("header").getString("message").equals("Country not found in user inventory")) {

                                    Log.d(TAG, "country not in user inventory......");

                                    new AlertDialog.Builder(MainAppActivity.this)
                                            .setTitle("Failed")
                                            .setMessage("The Country of selected city is not in your inventory. Please try with a different city.")

                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Continue with delete operation
                                                }
                                            })
                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setIcon(android.R.drawable.ic_delete)
                                            .show();

                                } else if (response.getJSONObject("header").getString("message").equals("Your account has expired. Buy premium account and enjoy unlimited freedom")) {
                                    new AlertDialog.Builder(MainAppActivity.this)
                                            .setTitle("Expire")
                                            .setMessage("Your account has expired. Buy premium account and enjoy unlimited freedom from panel.")

                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Continue with delete operation
                                                    Intent intent = new Intent(MainAppActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    Intent intent = new Intent(MainAppActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setIcon(android.R.drawable.ic_delete)
                                            .show();
                                } else if (response.getJSONObject("header").getString("message").equals("Protocol Not Allowed !")) {
                                    new AlertDialog.Builder(MainAppActivity.this)
                                            .setTitle("Failde")
                                            .setMessage("Protocol not allowed. Please try a different city.")

                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Continue with delete operation
                                                    Intent intent = new Intent(MainAppActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    Intent intent = new Intent(MainAppActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setIcon(android.R.drawable.ic_delete)
                                            .show();
                                }


//                                displayData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Volley error...." + error);
                Log.d(TAG, "Volley error...." + error.getMessage());
                Log.d(TAG, "Volley error...." + error.getCause());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Accept", "application/json");
                headers.put("X-AccessToken", token);
                String accessToken = AtomDemoApplicationController.getInstance().GetAccessToken().accessToken;
//
                return headers;
            }
        };
        mRequestQueue.add(request);
//        sendPostRequest();
    }

    public void CheckConnectionAndNavigate() {


        if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {

            new AlertDialog.Builder(MainAppActivity.this)
                    .setTitle("Connected to PFN")
                    .setMessage("You have to disconnect to change mode. Do you want to disconnect?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation

                            AtomDemoApplicationController.getInstance().getAtomManager().disconnect(getApplicationContext());
                            connect.setImageResource(R.drawable.hdstream_quickconnect2);
                            s_country.setText("");
                            status.setText("");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent mod_Text = new Intent(getApplicationContext(), ModeSelectionActivity.class);
                                    startActivity(mod_Text);
                                }
                            }, 2000);


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

        } else {
            Intent mod_Text = new Intent(getApplicationContext(), ModeSelectionActivity.class);
            startActivity(mod_Text);
        }

    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.mode_click) {
            CheckConnectionAndNavigate();

        } else if (v.getId() == R.id.location_container) {

            if (!mode_text_Pref.equals("dedicated_id")) {
                Intent locations_text = new Intent(getApplicationContext(), LocationCountriesActivity.class);
                locations_text.putExtra("mode", mode);
                startActivityForResult(locations_text, 220);
            }

        } else if (v.getId() == R.id.channel_container) {

            if (!mode_text_Pref.equals("dedicated_id")) {
                Intent locations_text = new Intent(getApplicationContext(), ChannelsActivity.class);
                locations_text.putExtra("mode", mode);
                startActivityForResult(locations_text, 230);
            }
        }

    }

    public void ProvideCredentsDialog() {

        new AlertDialog.Builder(MainAppActivity.this)
                .setTitle("Failed")
                .setMessage("Please Login again to connect.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor_clear = getSharedPreferences("connection", MODE_PRIVATE).edit();
                        editor_clear.clear().apply();

                        Intent intent = new Intent(MainAppActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        SharedPreferences.Editor editor_clear = getSharedPreferences("connection", MODE_PRIVATE).edit();
                        editor_clear.clear().apply();

                        Intent intent = new Intent(MainAppActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

}