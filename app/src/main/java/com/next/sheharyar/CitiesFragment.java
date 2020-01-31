package com.next.sheharyar;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atom.core.models.AccessToken;
import com.atom.sdk.android.VPNCredentials;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.APIs.APIs;
import com.next.sheharyar.Model.CitiesItem;
import com.next.sheharyar.Model.ModelMethods;
import com.next.sheharyar.adapter.CitiesAdapter;
import com.next.sheharyar.adapter.FavouriteCitiesAdapter;
import com.next.sheharyar.logger.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;



public class CitiesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public CitiesFragment() {
        // Required empty public constructor
    }
    public static CitiesFragment newInstance(String param1, String param2) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    View v;
    SearchView editsearch;
    public static final String TAG = "shani";
    LinearLayout appear_here;
    private RecyclerView mRecyclerView , fav_recyclerView;
    private CitiesAdapter mCitiesAdapter;
    private FavouriteCitiesAdapter fav_CitiesAdapter;
    private ArrayList<CitiesItem> mCitiesList= new ArrayList<>();
    private ArrayList<CitiesItem> fav_CitiesList= new ArrayList<>();
    private com.android.volley.RequestQueue mRequestQueue;
    int connection_type;
    String mode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_cities, container, false);



        //new Handler().postDelayed(() -> AtomDemoApplicationController.getInstance().getAtomManager().bindIKEVStateService(getActivity()), 500);

        mRecyclerView = v.findViewById(R.id.recycler_view_cities_list);
        appear_here = (LinearLayout)v.findViewById(R.id.appear_here);
        fav_recyclerView = v.findViewById(R.id.fav_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mCitiesAdapter);

        mCitiesList = new ArrayList<>();
        mCitiesAdapter = new CitiesAdapter(getActivity(), mCitiesList, CitiesFragment.this);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        editsearch = (SearchView) v.findViewById(R.id.search_view);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        editsearch.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        editsearch.setMaxWidth(Integer.MAX_VALUE);
        // Listning to search query text change ..
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //filtering recycler view when query submitted ..
                //mCitiesAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //filtering recyclerview when text is changed..
                mCitiesAdapter.getFilter().filter(s);

                return false;
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("Mode", MODE_PRIVATE);
        if (prefs != null) {
            mode = prefs.getString("selected_mode", null);
        }

        fav_CitiesList = new ArrayList<CitiesItem>();
        SharedPreferences mPrefs = getActivity().getSharedPreferences("favourite_list_cities", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("favourite_list", "");
        if (json.isEmpty()) {
            fav_CitiesList = new ArrayList<CitiesItem>();
        } else {
            Type type = new TypeToken<List<CitiesItem>>() {
            }.getType();
            fav_CitiesList = gson.fromJson(json, type);
            Log.d(TAG,"array........"+fav_CitiesList.toString());
        }

        fav_CitiesAdapter = new FavouriteCitiesAdapter(getActivity(), fav_CitiesList, CitiesFragment.this);
        LinearLayoutManager linearLayoutManagerf = new LinearLayoutManager(getActivity());
        fav_recyclerView.setLayoutManager(linearLayoutManagerf);
        fav_recyclerView.setAdapter(fav_CitiesAdapter);


        if (fav_CitiesList.size()<1){
            fav_recyclerView.setVisibility(View.GONE);
            appear_here.setVisibility(View.VISIBLE);
        }else {
            fav_recyclerView.setVisibility(View.VISIBLE);
            appear_here.setVisibility(View.GONE);
        }



        if (AtomDemoApplicationController.getInstance().getAtomManager() != null)
        {
            AtomDemoApplicationController.getInstance().getAtomManager().getConnectionDetails();
            VPNCredentials.class.getSigners();
            //AtomNetworkModule.class.getResource(AccessToken.getInstance().getAccessToken());

            if (AtomDemoApplicationController.getInstance().GetAccessToken() != null) {
                android.util.Log.d("shani","ChannelsActivity() token exist");
                String accessToken = AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken();
                Log.d(TAG,"access token....."+accessToken);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("connection", MODE_PRIVATE).edit();
                editor.putString("access_token", accessToken);
                editor.apply();

                GetCities();


            } else {
                GetAccessToken();
            }





        }
        if (savedInstanceState == null) {

            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                if (extras.containsKey("connection_type")) {
                    connection_type = extras.getInt("connection_type");
                }
            }
        }

        List<CitiesItem> rowListItem = getAllItemList();
        LinearLayoutManager Layout = new LinearLayoutManager(getActivity());
        RecyclerView rView = (RecyclerView)v.findViewById(R.id.recycler_view_cities_list);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(Layout);
        CitiesAdapter rclAdapter = new CitiesAdapter(getActivity(), (ArrayList<CitiesItem>) rowListItem, CitiesFragment.this);
        rView.setAdapter(rclAdapter);
//        editsearch.setOnQueryTextListener(this);





        return v;
    }


    public void UpdateLists(int position, String operation){


        fav_CitiesList = new ArrayList<CitiesItem>();
        SharedPreferences mPrefs = getActivity().getSharedPreferences("favourite_list_cities", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("favourite_list", "");
        if (json.isEmpty()) {
            fav_CitiesList = new ArrayList<CitiesItem>();
        } else {
            Type type = new TypeToken<List<CitiesItem>>() {
            }.getType();
            fav_CitiesList = gson.fromJson(json, type);
            Log.d(TAG,"array........"+fav_CitiesList.toString());
        }

        fav_CitiesAdapter = new FavouriteCitiesAdapter(getActivity(), fav_CitiesList, CitiesFragment.this);
        LinearLayoutManager linearLayoutManagerf = new LinearLayoutManager(getActivity());
        fav_recyclerView.setLayoutManager(linearLayoutManagerf);
        fav_recyclerView.setAdapter(fav_CitiesAdapter);
        mCitiesAdapter.notifyDataSetChanged();


        ArrayList<CitiesItem> list = new ArrayList<>();
        mCitiesAdapter = new CitiesAdapter(getActivity(), list, CitiesFragment.this);
        mRecyclerView.setAdapter(mCitiesAdapter);
        mCitiesAdapter.notifyDataSetChanged();



        for (int i = 0; i < mCitiesList.size(); i++) {
            boolean exist = false;
            for (int j = 0; j <fav_CitiesList.size(); j++) {
                if (fav_CitiesList.get(j).getmId() == mCitiesList.get(i).getmId()){
                    exist = true;
                }
            }

            list.add(new CitiesItem(mCitiesList.get(i).getmId(), mCitiesList.get(i).getmCityName(), mCitiesList.get(i).getmCountryId(), mCitiesList.get(i).getData_center_array(), exist));

        }

        mCitiesAdapter = new CitiesAdapter(getActivity(), list, CitiesFragment.this);
        mRecyclerView.setAdapter(mCitiesAdapter);
        mCitiesAdapter.notifyDataSetChanged();


        if (fav_CitiesList.size()<1){
            fav_recyclerView.setVisibility(View.GONE);
            appear_here.setVisibility(View.VISIBLE);
        }else {
            fav_recyclerView.setVisibility(View.VISIBLE);
            appear_here.setVisibility(View.GONE);
        }


    }


    private List<CitiesItem> getAllItemList() {

        List<CitiesItem> allItems = new ArrayList<>();
        return allItems;
    }

    private void parseCitiesJson() {

        mCitiesList = new ArrayList<>();

        Log.d(TAG,"ccacccccccccccaaaaaaaaaaaaaaaaaaaaaaallllllllllllleeeeeeeedddddddddddddddddddd .......................................");


        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        String url = "http://api.atom.purevpn.com/inventory/v1/getCities/272";
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("Accept", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d(TAG, "111111  access tokennnnnn    "+AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
            postparams.put("x-AccessToken", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "response city    "+response);


                        try {
                            JSONArray j = response.getJSONObject("body").getJSONArray("city");
                            for (int x = 0; x < j.length(); x++)
                            {
                                JSONObject citiesRecord = j.getJSONObject(x);
//                                String imageUrl = channelRecord.getString("icon_url");
                                Log.d(TAG,"cities record...."+citiesRecord);
                                int id = citiesRecord.getInt("id");
                                String name      = citiesRecord.getString("name");
                                String countryid = citiesRecord.getString("country_id");

                                JSONArray array = new JSONArray(citiesRecord.getString("data_centers"));

                                String[] center_array = new String[array.length()];

                                for (int i = 0; i <array.length() ; i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    center_array[i] = object.getString("id");
                                }

                               // Log.d(TAG,"citiesRecord.... array array......."+citiesRecord);

//                                String channelUrl = channelRecord.getString("channel_url");

                                boolean exist = false;
                                for (int k = 0; k <fav_CitiesList.size(); k++) {
                                    if (fav_CitiesList.get(k).getmId() == id){
                                        exist = true;
                                    }
                                }

                                if (mode.equals("file_sharing")){
                                    if (!ModelMethods.IsBannedCountryInt(Integer.parseInt(countryid))){
                                        mCitiesList.add(new CitiesItem(id, name, countryid , center_array, exist));
                                    }
                                }else {
                                    mCitiesList.add(new CitiesItem(id, name, countryid , center_array, exist));
                                }

                            }

                            mCitiesAdapter = new CitiesAdapter(getActivity(), mCitiesList, CitiesFragment.this);
                            mRecyclerView.setAdapter(mCitiesAdapter);
                         /*   mCitiesAdapter.setOnItemClickListener(new CitiesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {


                                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("connection", MODE_PRIVATE).edit();
                                        editor.putString("countryId", mCitiesList.get(position).getmCountryId());
                                        editor.putString("cityId", String.valueOf(mCitiesList.get(position).getmId()));
                                        editor.putString("cityName", mCitiesList.get(position).getmCityName());
                                        //editor.putString("access_token", AccessToken.getInstance().getAccessToken());
                                        editor.apply();

                                        Intent intent = new Intent();
                                        intent.putExtra("countryId", mCitiesList.get(position).getmCountryId());
                                        intent.putExtra("cityId", String.valueOf(mCitiesList.get(position).getmId()));
                                        intent.putExtra("cityName", mCitiesList.get(position).getmCityName());
                                        intent.putExtra("access_token", AccessToken.getInstance().getAccessToken());
                                        intent.putExtra("data_center_list", mCitiesList.get(position).getData_center_array());
                                        getActivity().setResult(RESULT_OK,intent);
                                        getActivity().finish();



                                }
                            });*/

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "Exception    "+e.getMessage());

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "VolleyError    "+error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                Log.d(TAG, "2222  access tokennnnnn    "+AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                headers.put("Accept","application/json" );
                headers.put("x-AccessToken", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());
                String accessToken = AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken();
//
                return headers;
            }
        };
        mRequestQueue.add(request);
//        sendPostRequest();
    }



    private void GetCities() {

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        HashMap<String, String> params = new HashMap<>();
        params.put("Accept", "application/json");
        params.put("x-AccessToken", AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken());

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, APIs.CITIES_URL, new JSONObject(params), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, "response city    "+response);


                try {
                    JSONArray j = response.getJSONObject("body").getJSONArray("city");
                    for (int x = 0; x < j.length(); x++)
                    {
                        JSONObject citiesRecord = j.getJSONObject(x);
//                                String imageUrl = channelRecord.getString("icon_url");
                        Log.d(TAG,"cities record...."+citiesRecord);
                        int id = citiesRecord.getInt("id");
                        String name      = citiesRecord.getString("name");
                        String countryid = citiesRecord.getString("country_id");

                        JSONArray array = new JSONArray(citiesRecord.getString("data_centers"));

                        String[] center_array = new String[array.length()];

                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            center_array[i] = object.getString("id");
                        }

                        // Log.d(TAG,"citiesRecord.... array array......."+citiesRecord);

//                                String channelUrl = channelRecord.getString("channel_url");

                        boolean exist = false;
                        for (int k = 0; k <fav_CitiesList.size(); k++) {
                            if (fav_CitiesList.get(k).getmId() == id){
                                exist = true;
                            }
                        }

                        if (mode.equals("file_sharing")){
                            if (!ModelMethods.IsBannedCountryInt(Integer.parseInt(countryid))){
                                mCitiesList.add(new CitiesItem(id, name, countryid , center_array, exist));
                            }
                        }else {
                            mCitiesList.add(new CitiesItem(id, name, countryid , center_array, exist));
                        }

                    }

                    mCitiesAdapter = new CitiesAdapter(getActivity(), mCitiesList, CitiesFragment.this);
                    mRecyclerView.setAdapter(mCitiesAdapter);
                         /*   mCitiesAdapter.setOnItemClickListener(new CitiesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {


                                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("connection", MODE_PRIVATE).edit();
                                        editor.putString("countryId", mCitiesList.get(position).getmCountryId());
                                        editor.putString("cityId", String.valueOf(mCitiesList.get(position).getmId()));
                                        editor.putString("cityName", mCitiesList.get(position).getmCityName());
                                        //editor.putString("access_token", AccessToken.getInstance().getAccessToken());
                                        editor.apply();

                                        Intent intent = new Intent();
                                        intent.putExtra("countryId", mCitiesList.get(position).getmCountryId());
                                        intent.putExtra("cityId", String.valueOf(mCitiesList.get(position).getmId()));
                                        intent.putExtra("cityName", mCitiesList.get(position).getmCityName());
                                        intent.putExtra("access_token", AccessToken.getInstance().getAccessToken());
                                        intent.putExtra("data_center_list", mCitiesList.get(position).getData_center_array());
                                        getActivity().setResult(RESULT_OK,intent);
                                        getActivity().finish();



                                }
                            });*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Exception    "+e.getMessage());

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle errors
                android.util.Log.d("shani","Error GetAccessToken VolleyError "+error);
                android.util.Log.d("shani","Error GetAccessToken VolleyError "+error.networkResponse);
                android.util.Log.d("shani","Error GetAccessToken VolleyError "+error.getCause());
                android.util.Log.d("shani","Error GetAccessToken VolleyError "+error.getMessage());
                Toast.makeText(getActivity(), "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);



    }

    private void GetAccessToken() {

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        HashMap<String, String> params = new HashMap<>();
        params.put("grantType", "secret");
        params.put("secretKey", getResources().getString(R.string.atom_secret_key));

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, APIs.GET_ACCESS_TOKEN, new JSONObject(params), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                android.util.Log.d("shani","response ..... "+response);
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

                        String accessTokennnnnn = AtomDemoApplicationController.getInstance().GetAccessToken().getAccessToken();
                        Log.d(TAG,"access token....."+accessTokennnnnn);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("connection", MODE_PRIVATE).edit();
                        editor.putString("access_token", accessTokennnnnn);
                        editor.apply();

                        GetCities();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    android.util.Log.d("shani","Error GetAccessToken "+e.getMessage());
                    Toast.makeText(getActivity(), " Error occurred.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle errors
                android.util.Log.d("shani","Error GetAccessToken VolleyError "+error.getMessage());
                Toast.makeText(getActivity(), "Error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);



    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
