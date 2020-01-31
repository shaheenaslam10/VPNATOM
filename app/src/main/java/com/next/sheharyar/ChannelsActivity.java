package com.next.sheharyar;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.atom.core.models.AccessToken;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atom.sdk.android.AtomManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.APIs.APIs;
import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.Model.ChannelListModel;
import com.next.sheharyar.adapter.ChannelsListAdapter;
import com.next.sheharyar.adapter.FavChannelsListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    SearchView search_view;
    TextView status;
    public static final String TAG = "shani";
    private RecyclerView recyclerView, fav_recyclerView;
    private ChannelsListAdapter channelsListAdapter;
    private FavChannelsListAdapter fav_channelsListAdapter;
    private ArrayList<ChannelListModel> arrayList = new ArrayList<>();
    private ArrayList<ChannelListModel> fav_arrayList = new ArrayList<>();
    private com.android.volley.RequestQueue mRequestQueue;
    ProgressBar progressBar;
    private LinearLayoutManager mLayoutManager;
    LinearLayout quick_connect;

    TextView app_bottom_Connect_text, hd_stream_header;
    ImageView app_bottom_Connect, navigation_right_arrow;
    String mode;
    String mode_text, username;
    String mode_text_Pref;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);


      /*  SharedPreferences mPrefs_put = getSharedPreferences("favourite_list_channels", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
        prefsEditor.putString("favourite_list", null);
        prefsEditor.commit();
*/

        quick_connect = (LinearLayout) findViewById(R.id.quick_connect);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fav_recyclerView = (RecyclerView) findViewById(R.id.fav_recyclerView);
        search_view = (SearchView) findViewById(R.id.search_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        status = (TextView) findViewById(R.id.status);

        progressBar.setVisibility(View.VISIBLE);
        status.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        search_view.setSearchableInfo(searchManager.getSearchableInfo(ChannelsActivity.this.getComponentName()));
        search_view.setMaxWidth(Integer.MAX_VALUE);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.length()>0){
                    UpdateSearchLists(s);
                }else {
                    SetAdapter();
                }

                return false;
            }
        });



        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getStringExtra("mode");
        }


        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            username = prefs.getString("username", null);
        }

        if (mode != null) {

            if (mode.equals("freedom")) {

                mode_text = "Internet Freedom";

            } else if (mode.equals("security")) {

                mode_text = "Security/Privacy";

            } else if (mode.equals("file_sharing")) {

                mode_text = "File Sharing";

            } else if (mode.equals("hd_stream")) {

                mode_text = "HD Stream";

            }
        }


        fav_arrayList = new ArrayList<ChannelListModel>();
        SharedPreferences mPrefs = getSharedPreferences("favourite_list_channels", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("favourite_list", "");

        Log.d(TAG,"channelsActivity json ......"+json);

        if (json.isEmpty()) {
            Log.d(TAG,"channelsActivity json empty......");
            fav_arrayList = new ArrayList<ChannelListModel>();
        } else {
            Log.d(TAG,"channelsActivity json not empty......");
            Type type = new TypeToken<List<ChannelListModel>>() {
            }.getType();
            fav_arrayList = gson.fromJson(json, type);
            com.next.sheharyar.logger.Log.d(TAG,"array........"+fav_arrayList.toString());
        }

        Log.d(TAG, "fav list size ..... "+fav_arrayList.size());

        fav_channelsListAdapter = new FavChannelsListAdapter( fav_arrayList,ChannelsActivity.this, ChannelsActivity.this);
        LinearLayoutManager linearLayoutManagerf = new LinearLayoutManager(ChannelsActivity.this);
        fav_recyclerView.setLayoutManager(linearLayoutManagerf);
        fav_recyclerView.setAdapter(fav_channelsListAdapter);


        if (fav_arrayList.size()<1){
            fav_recyclerView.setVisibility(View.GONE);
            //appear_here.setVisibility(View.VISIBLE);
        }else {
            fav_recyclerView.setVisibility(View.VISIBLE);
            //appear_here.setVisibility(View.GONE);
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
                }
            }
        }

        if (AtomDemoApplicationController.getInstance().GetAccessToken() != null) {
            Log.d("shani","ChannelsActivity() token exist");
            GetChannelList();
        } else {
            GetAccessToken();
        }

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
        hd_stream_header = (TextView) headerview.findViewById(R.id.hd_stream_header);
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
        if (mode != null) {

            if (mode.equals("freedom")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("security")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("file_sharing")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            } else if (mode.equals("hd_stream")) {

                menu.findItem(R.id.nav_p2p).setVisible(false);

            }
        }



        quick_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent app_bottom_onnect_image = new Intent(getApplicationContext(), MainAppActivity.class);
                app_bottom_onnect_image.putExtra("mode",mode);
                startActivity(app_bottom_onnect_image);
            }
        });
    }


    public void UpdateSearchLists(String text){


        ArrayList<ChannelListModel> list = new ArrayList<>();
        channelsListAdapter = new ChannelsListAdapter(list,ChannelsActivity.this, ChannelsActivity.this);
        recyclerView.setAdapter(channelsListAdapter);
        channelsListAdapter.notifyDataSetChanged();

        for (int i = 0; i < arrayList.size(); i++) {

            if (arrayList.get(i).getName().toLowerCase().contains(text.toLowerCase())){

                boolean exist = false;
                for (int j = 0; j <fav_arrayList.size(); j++) {
                    if (fav_arrayList.get(j).getId().equals(arrayList.get(i).getId())){
                        exist = true;
                    }
                }

                String id, name, channel_url, order, icon_url, package_name_android_tv, package_name_android, package_name_amazon_fs, is_free, is_new;

                id = arrayList.get(i).getId();
                name = arrayList.get(i).getName();
                channel_url = arrayList.get(i).getChannel_url();
                order = arrayList.get(i).getOrder();
                icon_url = arrayList.get(i).getIcon_url();
                package_name_android_tv = arrayList.get(i).getPackage_name_android_tv();
                package_name_android = arrayList.get(i).getPackage_name_android();
                package_name_amazon_fs = arrayList.get(i).getPackage_name_amazon_fs();
                is_free = arrayList.get(i).getIs_free();
                is_new = arrayList.get(i).getIs_new();

                ChannelListModel channelListModel = new ChannelListModel(id, name, channel_url, order, icon_url, package_name_android_tv, package_name_android, package_name_amazon_fs, is_free, is_new, exist);
                list.add(channelListModel);
            }

        }

        channelsListAdapter = new ChannelsListAdapter(list, ChannelsActivity.this, ChannelsActivity.this);
        recyclerView.setAdapter(channelsListAdapter);
        channelsListAdapter.notifyDataSetChanged();

    }

    public void UpdateLists(){

        fav_arrayList = new ArrayList<ChannelListModel>();
        SharedPreferences mPrefs = getSharedPreferences("favourite_list_channels", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("favourite_list", "");
        if (json.isEmpty()) {
            fav_arrayList = new ArrayList<ChannelListModel>();
        } else {
            Type type = new TypeToken<List<ChannelListModel>>() {
            }.getType();
            fav_arrayList = gson.fromJson(json, type);
            com.next.sheharyar.logger.Log.d(TAG,"array........"+fav_arrayList.toString());
        }

        fav_channelsListAdapter = new FavChannelsListAdapter( fav_arrayList,ChannelsActivity.this, ChannelsActivity.this);
        LinearLayoutManager linearLayoutManagerf = new LinearLayoutManager(ChannelsActivity.this);
        fav_recyclerView.setLayoutManager(linearLayoutManagerf);
        fav_recyclerView.setAdapter(fav_channelsListAdapter);
        fav_channelsListAdapter.notifyDataSetChanged();
        channelsListAdapter.notifyDataSetChanged();


        ArrayList<ChannelListModel> list = new ArrayList<>();
        channelsListAdapter = new ChannelsListAdapter(list,ChannelsActivity.this, ChannelsActivity.this);
        recyclerView.setAdapter(channelsListAdapter);
        channelsListAdapter.notifyDataSetChanged();


        for (int i = 0; i < arrayList.size(); i++) {
            boolean exist = false;
            for (int j = 0; j <fav_arrayList.size(); j++) {
                if (fav_arrayList.get(j).getId().equals(arrayList.get(i).getId())){
                    exist = true;
                }
            }

            String id, name, channel_url, order, icon_url, package_name_android_tv, package_name_android, package_name_amazon_fs, is_free, is_new;

            id = arrayList.get(i).getId();
            name = arrayList.get(i).getName();
            channel_url = arrayList.get(i).getChannel_url();
            order = arrayList.get(i).getOrder();
            icon_url = arrayList.get(i).getIcon_url();
            package_name_android_tv = arrayList.get(i).getPackage_name_android_tv();
            package_name_android = arrayList.get(i).getPackage_name_android();
            package_name_amazon_fs = arrayList.get(i).getPackage_name_amazon_fs();
            is_free = arrayList.get(i).getIs_free();
            is_new = arrayList.get(i).getIs_new();

            ChannelListModel channelListModel = new ChannelListModel(id, name, channel_url, order, icon_url, package_name_android_tv, package_name_android, package_name_amazon_fs, is_free, is_new, exist);
            list.add(channelListModel);

        }

        channelsListAdapter = new ChannelsListAdapter(list, ChannelsActivity.this, ChannelsActivity.this);
        recyclerView.setAdapter(channelsListAdapter);
        channelsListAdapter.notifyDataSetChanged();


        if (fav_arrayList.size()<1){
            fav_recyclerView.setVisibility(View.GONE);
        }else {
            fav_recyclerView.setVisibility(View.VISIBLE);
        }


    }
    private void GetAccessToken() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(ChannelsActivity.this);
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

                        GetChannelList();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("shani","Error GetAccessToken "+e.getMessage());
                    Toast.makeText(ChannelsActivity.this, " Error occurred.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle errors
                Log.d("shani","Error GetAccessToken VolleyError "+error.getMessage());
                Toast.makeText(ChannelsActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);



    }


    private void GetChannelList() {

        Log.d("shani","ChannelsActivity() GetChannelList");

        RequestQueue mRequestQueue = Volley.newRequestQueue(ChannelsActivity.this);

        HashMap<String, String> params = new HashMap<>();
        params.put("grantType", "secret");
        params.put("secretKey", getResources().getString(R.string.atom_secret_key));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APIs.GET_ChannelsList, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject header = response.getJSONObject("header");
                    if (header.getString("message").equals("Success")) {

                        JSONObject body = response.getJSONObject("body");
                        JSONArray channelArray = body.getJSONArray("channels");

                        Log.d("shani","ChannelsActivity() channelsArray.size = "+channelArray.length());

                        for (int i = 0; i < channelArray.length(); i++) {

                            JSONObject channelObj = channelArray.getJSONObject(i);
                            String id = channelObj.getString("id");
                            String name = channelObj.getString("name");
                            String channel_url = channelObj.getString("channel_url");
                            String order = channelObj.getString("order");
                            String icon_url = channelObj.getString("icon_url");
                            String package_name_android_tv = channelObj.getString("package_name_android_tv");
                            String package_name_android = channelObj.getString("package_name_android");
                            String package_name_amazon_fs = channelObj.getString("package_name_amazon_fs");
                            String is_free = channelObj.getString("is_free");
                            String is_new = channelObj.getString("is_new");

                            boolean exist = false;
                            for (int j = 0; j <fav_arrayList.size() ; j++) {

                                if (fav_arrayList.get(j).getId().equals(id))
                                    exist = true;

                            }

                            ChannelListModel channelListModel = new ChannelListModel(id, name, channel_url, order, icon_url, package_name_android_tv, package_name_android, package_name_amazon_fs, is_free, is_new, exist);
                            arrayList.add(channelListModel);

                        }
                        SetAdapter();

                    } else {
                        Toast.makeText(ChannelsActivity.this, getResources().getString(R.string.error_list), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        status.setVisibility(View.VISIBLE);
                        status.setText(getResources().getString(R.string.error_list));
                        recyclerView.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    Toast.makeText(ChannelsActivity.this, getResources().getString(R.string.error_list), Toast.LENGTH_SHORT).show();
                    Log.d("shani", "json exception....." + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    status.setVisibility(View.VISIBLE);
                    status.setText(getResources().getString(R.string.error_list));
                    recyclerView.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    Toast.makeText(ChannelsActivity.this, getResources().getString(R.string.error_list), Toast.LENGTH_SHORT).show();
                    Log.d("shani", "NullPointerException exception....." + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    status.setVisibility(View.VISIBLE);
                    status.setText(getResources().getString(R.string.error_list));
                    recyclerView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChannelsActivity.this, getResources().getString(R.string.error_list), Toast.LENGTH_SHORT).show();
                Log.d("shani", "volley error......." + error.getMessage());
                progressBar.setVisibility(View.GONE);
                status.setVisibility(View.VISIBLE);
                status.setText(getResources().getString(R.string.error_list));
                recyclerView.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                Log.d("shani", "tokkkk......." + AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                headers.put("X-AccessToken", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                return headers;
            }
        };
        mRequestQueue.add(request);


    }

    private void SetAdapter() {

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(ChannelsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        channelsListAdapter = new ChannelsListAdapter(arrayList, ChannelsActivity.this, ChannelsActivity.this);
        recyclerView.setAdapter(channelsListAdapter);

        Log.d("shani","adapter end.....");
    }

    public void CheckConnectionAndNavigate() {

        if (AtomDemoApplicationController.getInstance().getAtomManager().getCurrentVpnStatus(getApplicationContext()).equalsIgnoreCase(AtomManager.VPNStatus.CONNECTED)) {

            new AlertDialog.Builder(ChannelsActivity.this)
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

        } else if (id == R.id.nav_p2p) {

            Intent nav_faq = new Intent(getApplicationContext(), P2PActivity.class);
            startActivity(nav_faq);

        } else if (id == R.id.share) {

            String shareBody = getResources().getString(R.string.share_text) + username;

            Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
            txtIntent.setType("text/plain");
            txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(txtIntent, "Share link!"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
