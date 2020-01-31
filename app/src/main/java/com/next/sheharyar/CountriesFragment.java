package com.next.sheharyar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.atom.core.exceptions.AtomException;
import com.atom.core.models.Country;
import com.atom.core.models.Protocol;
import com.atom.sdk.android.ConnectionDetails;
import com.atom.sdk.android.Errors;
import com.atom.sdk.android.VPNStateListener;
import com.atom.sdk.android.data.callbacks.CollectionCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.sheharyar.Model.CountryImageMatcher;
import com.next.sheharyar.Model.ModelMethods;
import com.next.sheharyar.logger.LogFragment;
import com.next.sheharyar.logger.LogWrapper;
import com.next.sheharyar.logger.MessageOnlyLogFilter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.atom.sdk.android.AtomManager.VPNStatus.CONNECTING;


public class CountriesFragment extends Fragment implements VPNStateListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public CountriesFragment() {
        // Required empty public constructor
    }
    public static CountriesFragment newInstance(String param1, String param2) {
        CountriesFragment fragment = new CountriesFragment();
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



    LogFragment logFragment;
    public LogWrapper logWrapper;
    ArrayAdapter<Country> countryAdapter;
    EditText inputSearch;
    SearchView editsearch;
    List<Protocol> protocolList; //adapter
    List<Country> countriesList = new ArrayList<>(); //adapter
    ArrayList<Country> countriesListFilter = new ArrayList<>(); // for search
    LinearLayout appear_here;
    ImageView app_bottom_Connect, app_bottom_purpose, app_bottom_channel, navigation_right_arrow;
    RelativeLayout segmentedButtonLayout, searchViewLayout, favouritiesLayout, countriesLayout, mainAppLayout;
    Country country;

    FavouriteCountryAdapter fav_countryAdapter;
    List<Country> savedCountry;
    ListView countryList , fav_list; //listview
    public static final String TAG = "shani";
    String mode;
    boolean[] booleans_list , booleans_list_temp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_countries, container, false);



        initializeLogging();
        editsearch = (SearchView) v.findViewById(R.id.search_view);
        appear_here = (LinearLayout) v.findViewById(R.id.appear_here);

        SharedPreferences prefs = getActivity().getSharedPreferences("Mode", MODE_PRIVATE);
        if (prefs != null) {
            mode = prefs.getString("selected_mode", null);
        }


        countryList = (ListView)v.findViewById(R.id.list);
        fav_list = (ListView) v.findViewById(R.id.fav_list);

        if (countryList!=null){

            country = (Country) countryList.getSelectedItem();
        }

        savedCountry = new ArrayList<Country>();
        SharedPreferences mPrefs = getActivity().getSharedPreferences("favourite_list", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("favourite_list", "");
        if (json.isEmpty()) {
            savedCountry = new ArrayList<Country>();
        } else {
            Type type = new TypeToken<List<Country>>() {
            }.getType();
            savedCountry = gson.fromJson(json, type);
            Log.d(TAG,"array........"+savedCountry.toString());
        }
        Country[] fav_countryArray = savedCountry.toArray(new Country[savedCountry.size()]);
        fav_countryAdapter = new FavouriteCountryAdapter(getActivity(), R.layout.favourite_list_item, fav_countryArray );
        fav_list.setAdapter(fav_countryAdapter);
        fav_countryAdapter.notifyDataSetChanged();


        if (savedCountry.size()<1){
            fav_list.setVisibility(View.GONE);
            appear_here.setVisibility(View.VISIBLE);
        }else {
            fav_list.setVisibility(View.VISIBLE);
            appear_here.setVisibility(View.GONE);
        }


        fav_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView, int position, long l) {
//                Country country = (Country)countryList.getSelectedItem();

                Log.d(TAG,"favLIstClicked.....");

                List<Country> country1;
                country1 = savedCountry;


                Country country = (Country) fav_list.getItemAtPosition(position);
                TextView textView = (TextView) v.findViewById(R.id.country_text);
                String text = textView.getText().toString();
                countryClick(country);


            }
        });


        // get Countries from Atom SDK
        if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {


            AtomDemoApplicationController.getInstance().getAtomManager().getCountries(new CollectionCallback<Country>() {
                @Override
                public void onError(AtomException e) {

                }

                @Override
                public void onSuccess(List<Country> countries) {


                    if (mode.equals("file_sharing")) {
                        for (int i = 0; i < countries.size(); i++) {
                            if (!ModelMethods.IsBannedCountry(countries.get(i).getName())){
//                                Log.d(TAG, "removed equals....." + countries.get(i).getId());
                                countriesList.add(countries.get(i));
                            }
                        }
                    }else {
                        countriesList = countries;
                    }


                    booleans_list = new boolean[countriesList.size()];
                    for (int i = 0; i < countriesList.size(); i++) {


                        countriesListFilter.add(countriesList.get(i));

//                        Log.d(TAG,"countries id......."+countriesList.get(i).getId());
                        Log.d(TAG,"countries name......."+countriesList.get(i).getName());


                        boolean exist = false;
                        for (int j = 0; j <savedCountry.size(); j++) {
                            if (savedCountry.get(j).getName() == countriesList.get(i).getName()){
                                exist = true;
                            }
                        }
                        if (exist){
                            booleans_list[i] = true;
                        }else {
                            booleans_list[i] = false;
                        }
                    }


                    Log.d(TAG, "countries......." + countriesList.get(0).getName());
                    Log.d(TAG, "countries......." + countriesList.toString());

                    if (countriesList != null) {
                        displayCountries(countriesList);
                    }

                }
                @SuppressLint("LogNotTimber")
                @Override
                public void onNetworkError(AtomException atomException) {
                    Log.e("error", atomException.getMessage() + " : " + atomException.getCode());

                }

            });
        }






        return v;
    }


    public void UpdateCountryList(int position){

        Log.d(TAG,"called"+savedCountry.toString());


        savedCountry = new ArrayList<Country>();
        Country[] fav_countryArray1 = savedCountry.toArray(new Country[savedCountry.size()]);
        fav_countryAdapter = new FavouriteCountryAdapter(getActivity(), R.layout.favourite_list_item, fav_countryArray1);
        fav_list.setAdapter(fav_countryAdapter);
        fav_countryAdapter.notifyDataSetChanged();


        savedCountry = new ArrayList<Country>();
        SharedPreferences mPrefs = getActivity().getSharedPreferences("favourite_list", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("favourite_list", "");
        if (json.isEmpty()) {
            savedCountry = new ArrayList<Country>();
        } else {
            Type type = new TypeToken<List<Country>>() {
            }.getType();
            savedCountry = gson.fromJson(json, type);
            Log.d(TAG,"array........"+savedCountry.toString());
            Log.d(TAG,"array this........"+savedCountry.toString());
        }
        Country[] fav_countryArray = savedCountry.toArray(new Country[savedCountry.size()]);
        fav_countryAdapter = new FavouriteCountryAdapter(getActivity(), R.layout.favourite_list_item, fav_countryArray);
        fav_list.setAdapter(fav_countryAdapter);
        fav_countryAdapter.notifyDataSetChanged();

        fav_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView, int position, long l) {
//                Country country = (Country)countryList.getSelectedItem();

                Log.d(TAG,"favLIstClicked.....");

                List<Country> country1;
                country1 = savedCountry;


                Country country = (Country) fav_list.getItemAtPosition(position);
                TextView textView = (TextView) v.findViewById(R.id.country_text);
                String text = textView.getText().toString();
                countryClick(country);


            }
        });


        booleans_list = new boolean[countriesList.size()];
        for (int i = 0; i < countriesList.size(); i++) {
            boolean exist = false;
            for (int j = 0; j <savedCountry.size(); j++) {
                if (savedCountry.get(j).getName() == countriesList.get(i).getName()){
                    exist = true;
                }
            }
            if (exist){
                booleans_list[i] = true;
            }else {
                booleans_list[i] = false;
            }
        }


        Country[] countryArray = countriesList.toArray(new Country[countriesList.size()]);
        countryAdapter = new CountryAdapter(getActivity(), R.layout.listitem,
                countryArray , booleans_list);

        countryList.setAdapter(countryAdapter);
        countryAdapter.notifyDataSetChanged();

        if (savedCountry.size()<1){
            fav_list.setVisibility(View.GONE);
            appear_here.setVisibility(View.VISIBLE);
        }else {
            fav_list.setVisibility(View.VISIBLE);
            appear_here.setVisibility(View.GONE);
        }
    }




    private List<Country> getCountriesByAllSelectedProtocol(List<Country> countries, Protocol primaryProtocol, Protocol secondaryProtocol, Protocol tertiaryProtocol) {
        List<Country> filteredCountries = new LinkedList<>();

        if (countries != null) {
            for (Country country : countries) {

                if (primaryProtocol != null && secondaryProtocol != null && tertiaryProtocol != null) {

                    if (country.getProtocols().contains(primaryProtocol) && country.getProtocols().contains(secondaryProtocol) && country.getProtocols().contains(tertiaryProtocol)) {
                        filteredCountries.add(country);
                    }

                } else if (primaryProtocol != null && secondaryProtocol != null) {

                    if (country.getProtocols().contains(primaryProtocol) && country.getProtocols().contains(secondaryProtocol)) {
                        filteredCountries.add(country);
                    }

                } else if (primaryProtocol != null) {

                    if (country.getProtocols().contains(primaryProtocol)) {
                        filteredCountries.add(country);
                    }

                } else {
                    return countries;
                }
            }
        }
        return filteredCountries;
    }
    public void displayCountries(List<Country> countries) {

        if (countries != null) {
            Country[] countryArray = countries.toArray(new Country[countries.size()]);
            try {

                countryAdapter = new CountryAdapter(getActivity(), R.layout.listitem,
                        countryArray , booleans_list);
//            countryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1); // android.R.layout.activity_list_item

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        countryList.setAdapter(countryAdapter);
                        countryAdapter.notifyDataSetChanged();

                    }
                });

            }catch (NullPointerException e){

                Log.d(TAG,"exception.,,,,,,,,,,,,,,,,,,,,,,");
            }




        }

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d(TAG, "query text....." + newText);

                filter(newText);

                return false;
            }
        });

        editsearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Country[] countryArrayn = countriesList.toArray(new Country[countriesList.size()]);
                countryAdapter = new CountryAdapter(getActivity(), R.layout.listitem, countryArrayn,booleans_list);

                countryList.setAdapter(countryAdapter);
                countryAdapter.notifyDataSetChanged();

                return false;
            }
        });

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView, int position, long l) {
//                Country country = (Country)countryList.getSelectedItem();
                List<Country> country1;
                country1 = countries;

                if (position == 29) {
//                    Country country = (Country)countryList.getSelectedItem();
                    Country country = (Country) countryList.getItemAtPosition(29);
                    TextView textView = (TextView) v.findViewById(R.id.country_text);
                    String text = textView.getText().toString();
                   // int id = country.getId();
                    String name = country.getName();
                    String protocols = country.getProtocols().toString();

                    Bundle extras = new Bundle();
                    //extras.putInt("ID_KEY", id);
                    extras.putString("NAME_KEY", name);
                    extras.putString("PROTOCOLS_KEY", protocols);
                    initializeLogging();
                    onConnecting();

                } else {
                    Country country = (Country) countryList.getItemAtPosition(position);
                    TextView textView = (TextView) v.findViewById(R.id.country_text);
                    String text = textView.getText().toString();
                    countryClick(country);
                }

            }
        });

    }



    @Override
    public void onConnecting() {

    }

    @Override
    public void onRedialing(AtomException atomException, ConnectionDetails connectionDetails) {
    }

    @Override
    public void onDialError(AtomException atomException, ConnectionDetails connectionDetails) {
        if (atomException.getCode() != Errors._5039) ;
    }

    @Override
    public void onStateChange(String state) {

        if (state.equalsIgnoreCase(CONNECTING)) {
            final ImageView disconnectButton = (ImageView) v.findViewById(R.id.center_button);
            disconnectButton.setImageResource(R.drawable.hd_stream_connecting);
        }
    }

    @Override
    public void onDisconnected(boolean b) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onConnected(ConnectionDetails connectionDetails) {}

    @Override
    public void onDisconnected(ConnectionDetails connectionDetails) {
        if (connectionDetails.isCancelled()) {
        } else {
        }
        final ImageView disconnectButton = (ImageView) v.findViewById(R.id.center_button);
        disconnectButton.setImageResource(R.drawable.hdstream_quickconnect2);
    }

    @Override
    public void onUnableToAccessInternet(AtomException e, ConnectionDetails connectionDetails) {}

    @Override
    public void onPacketsTransmitted(String s, String s1) {}


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

            logFragment = (LogFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.log_fragment);
            msgFilter.setNext(logFragment.getLogView());

            ViewAnimator output = (ViewAnimator) v.findViewById(R.id.sample_output);
            output.setDisplayedChild(1);

        } catch (Exception e){}
    }

    public void countryClick(Country country) {

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("connection", MODE_PRIVATE).edit();
        editor.putString("country", country.getName());
        editor.putString("last_connection", "country");
        editor.apply();

        Intent data = new Intent();
        String text = country.getName();
//---set the data to pass back---
        data.setData(Uri.parse(text));
        getActivity().setResult(RESULT_OK, data);
//---close the activity---
        getActivity().finish();

    }



    public void filter(String charText) {

        booleans_list_temp = new boolean[countriesList.size()];

        countriesListFilter.clear();
        Country[] countryArray = countriesListFilter.toArray(new Country[countriesListFilter.size()]);
        countryAdapter = new CountryAdapter(getActivity(), R.layout.listitem,
                countryArray , booleans_list);

        countryList.setAdapter(countryAdapter);
        countryAdapter.notifyDataSetChanged();

        charText = charText.toLowerCase(Locale.getDefault());
        // countriesListFilter.clear();
        if (charText.length() == 0) {
            countriesListFilter.addAll(countriesList);
            Country[] countryArrayn = countriesListFilter.toArray(new Country[countriesListFilter.size()]);
            countryAdapter = new CountryAdapter(getActivity(), R.layout.listitem,
                    countryArrayn , booleans_list);

            countryList.setAdapter(countryAdapter);
            countryAdapter.notifyDataSetChanged();
        }
        else
        {

            for (int i = 0; i < countriesList.size(); i++) {

                if (countriesList.get(i).getName().toLowerCase(Locale.getDefault()).contains(charText)) {

                    boolean ex = false;
                    countriesListFilter.add(countriesList.get(i));
                    for (int j = 0; j < savedCountry.size(); j++) {
                        if (savedCountry.get(j).getName()==countriesList.get(i).getName()){
                            ex = true;
                        }
                    }

                    if (ex){
                        booleans_list_temp[i] = true;
                    }else {
                        booleans_list_temp[i] = false;
                    }

                }else {
                }
            }

            Country[] countryArrayn = countriesListFilter.toArray(new Country[countriesListFilter.size()]);
            countryAdapter = new CountryAdapter(getActivity(), R.layout.listitem,
                    countryArrayn , booleans_list_temp);

            countryList.setAdapter(countryAdapter);
            countryAdapter.notifyDataSetChanged();
        }

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

    public class FavouriteCountryAdapter extends ArrayAdapter<Country> {


        private Country[] countries;
        private List<Country> countriesList;
        private Context context;
        public static final String TAG = "shani";
        CountrySelectionFragment fragment;


        public FavouriteCountryAdapter(Context context, int textViewResourceId, Country[] countries) {

            super(context, textViewResourceId, countries);
            this.countries = countries;
            this.context = context;
        }



        @Override
        public int getCount() {
            if (countries != null) {
                return countries.length;
            }
            return 0;
        }

        @Override
        public Country getItem(int position) {
            return countries[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*private view holder class*/
        private class ViewHolder {
            ImageView imageView, remove;
            TextView txtTitle;
            TextView txtDesc;
        }

        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            FavouriteCountryAdapter.ViewHolder holder;
            LayoutInflater myInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = myInflater.inflate(R.layout.favourite_list_item, null);
            holder = new FavouriteCountryAdapter.ViewHolder();
//        holder.txtTitle.setTextColor(Color.WHITE);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.country_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.country_image);
            holder.remove = (ImageView) convertView.findViewById(R.id.remove);
            holder.txtTitle.setText(countries[position].getName());

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<Country> savedCountry = new ArrayList<Country>();
                    SharedPreferences mPrefs = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("favourite_list", "");

                    Type type = new TypeToken<List<Country>>() {
                    }.getType();

                    savedCountry = gson.fromJson(json, type);

                    savedCountry.remove(position);
                    SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                    Gson gson_put = new Gson();
                    String json_put = gson_put.toJson(savedCountry);
                    prefsEditor.putString("favourite_list", json_put);
                    prefsEditor.commit();


                    UpdateCountryList(position);


                }
            });

            if (CountryImageMatcher.GetImageId(countries[position].getName()) != -1){
                holder.imageView.setImageDrawable(context.getResources().getDrawable(CountryImageMatcher.GetImageId(countries[position].getName())));
            }/*else {
            holder.imageView.setImageResource(context.getResources().getDrawable(R.drawable.));
        }*/


          /*  if (countries[position].getId() == 254) {
                holder.imageView.setImageResource(R.drawable.us);
            } else if (countries[position].getId() == 253) {
                holder.imageView.setImageResource(R.drawable.uk);
            } else if (countries[position].getId() == 14) {
                holder.imageView.setImageResource(R.drawable.australia);
            } else if (countries[position].getId() == 91) {
                holder.imageView.setImageResource(R.drawable.germany);
            } else if (countries[position].getId() == 63) {
                holder.imageView.setImageResource(R.drawable.cyprus);
            } else if (countries[position].getId() == 43) {
                holder.imageView.setImageResource(R.drawable.canada);
            } else if (countries[position].getId() == 175) {
                holder.imageView.setImageResource(R.drawable.netherlands);
            } else if (countries[position].getId() == 109) {
                holder.imageView.setImageResource(R.drawable.hongkong);
            } else if (countries[position].getId() == 233) {
                holder.imageView.setImageResource(R.drawable.sweden);
            } else if (countries[position].getId() == 122) {
                holder.imageView.setImageResource(R.drawable.japan);
            } else if (countries[position].getId() == 144) {
                holder.imageView.setImageResource(R.drawable.luxembourg);
            } else if (countries[position].getId() == 113) {
                holder.imageView.setImageResource(R.drawable.india);
            } else if (countries[position].getId() == 15) {
                holder.imageView.setImageResource(R.drawable.austria);
            } else if (countries[position].getId() == 65) {
                holder.imageView.setImageResource(R.drawable.denmark);
            } else if (countries[position].getId() == 185) {
                holder.imageView.setImageResource(R.drawable.norway);
            } else if (countries[position].getId() == 64) {
                holder.imageView.setImageResource(R.drawable.czech);
            } else if (countries[position].getId() == 226) {
                holder.imageView.setImageResource(R.drawable.spain);
            } else if (countries[position].getId() == 159) {
                holder.imageView.setImageResource(R.drawable.mexico);
            } else if (countries[position].getId() == 246) {
                holder.imageView.setImageResource(R.drawable.turkey);
            } else if (countries[position].getId() == 75) {
                holder.imageView.setImageResource(R.drawable.estonia);
            } else if (countries[position].getId() == 129) {
                holder.imageView.setImageResource(R.drawable.kenya);
            } else if (countries[position].getId() == 224) {
                holder.imageView.setImageResource(R.drawable.southafrica);
            } else if (countries[position].getId() == 236) {
                holder.imageView.setImageResource(R.drawable.taiwan);
            } else if (countries[position].getId() == 16) {
                holder.imageView.setImageResource(R.drawable.azerbaijan);
            } else if (countries[position].getId() == 35) {
                holder.imageView.setImageResource(R.drawable.britishvirgin);
            } else if (countries[position].getId() == 198) {
                holder.imageView.setImageResource(R.drawable.portugal);
            } else if (countries[position].getId() == 199) {
                holder.imageView.setImageResource(R.drawable.puertorico);
            } else if (countries[position].getId() == 238) {
                holder.imageView.setImageResource(R.drawable.tanzania);
            } else if (countries[position].getId() == 252) {
                holder.imageView.setImageResource(R.drawable.uae);
            } else if (countries[position].getId() == 303) {
                holder.imageView.setImageResource(R.drawable.isleofman);
            }else if (countries[position].getId() == 149) {
                holder.imageView.setImageResource(R.drawable.malaysia);//malaysia
            }
            else if (countries[position].getId() == 24) {
                holder.imageView.setImageResource(R.drawable.belgium);//belgium
            }
            else if (countries[position].getId() == 82) {
                holder.imageView.setImageResource(R.drawable.france);//france
            }
            else if (countries[position].getId() == 219) {
                holder.imageView.setImageResource(R.drawable.singapore);//singapore
            }
            else if (countries[position].getId() == 119) {
                holder.imageView.setImageResource(R.drawable.italy);//italy
            }
            else if (countries[position].getId() == 117) {
                holder.imageView.setImageResource(R.drawable.ireland);//ireland
            }
            else if (countries[position].getId() == 202) {
                holder.imageView.setImageResource(R.drawable.romania);//romania
            }
            else if (countries[position].getId() == 203) {
                holder.imageView.setImageResource(R.drawable.russian_federation);//russia
            }
            else if (countries[position].getId() == 197) {
                holder.imageView.setImageResource(R.drawable.poland);//poland
            }
            else if (countries[position].getId() == 81) {
                holder.imageView.setImageResource(R.drawable.finland);//finland
            }
            else if (countries[position].getId() == 234) {
                holder.imageView.setImageResource(R.drawable.switzerland);//switzerland
            }
            else if (countries[position].getId() == 251) {
                holder.imageView.setImageResource(R.drawable.ukraine);//ukraine
            }
            else if (countries[position].getId() == 178) {
                holder.imageView.setImageResource(R.drawable.new_zealand);//newzealand
            }
            else if (countries[position].getId() == 137) {
                holder.imageView.setImageResource(R.drawable.latvia);//latvia
            }
            else if (countries[position].getId() == 112) {
                holder.imageView.setImageResource(R.drawable.iceland);//Iceland
            }
            else if (countries[position].getId() == 143) {
                holder.imageView.setImageResource(R.drawable.lithuania);//lithuania
            }
            else if (countries[position].getId() == 95) {
                holder.imageView.setImageResource(R.drawable.greece);//greece
            }
            else if (countries[position].getId() == 220) {
                holder.imageView.setImageResource(R.drawable.slovakia);//slovakia
            }
            else if (countries[position].getId() == 33) {
                holder.imageView.setImageResource(R.drawable.brazil);//brazil
            }
            else if (countries[position].getId() == 59) {
                holder.imageView.setImageResource(R.drawable.costa_rica);//costa_rica
            }
            else if (countries[position].getId() == 111) {
                holder.imageView.setImageResource(R.drawable.hungary);//hungary
            }
            else if (countries[position].getId() == 133) {
                holder.imageView.setImageResource(R.drawable.south_korea);//south_korea
            }
            else if (countries[position].getId() == 239) {
                holder.imageView.setImageResource(R.drawable.thailand);//thailand
            }
            else if (countries[position].getId() == 181) {
                holder.imageView.setImageResource(R.drawable.nigeria);//nigeria
            }
            else if (countries[position].getId() == 163) {
                holder.imageView.setImageResource(R.drawable.moldova);//moldova
            }
            else if (countries[position].getId() == 92) {
                holder.imageView.setImageResource(R.drawable.ghana);//ghana
            }
            else if (countries[position].getId() == 2) {
                holder.imageView.setImageResource(R.drawable.albania);//albania
            }
            else if (countries[position].getId() == 90) {
                holder.imageView.setImageResource(R.drawable.georgia);//georgia
            }
            else if (countries[position].getId() == 114) {
                holder.imageView.setImageResource(R.drawable.indonesia);//indonesia
            }
            else if (countries[position].getId() == 260) {
                holder.imageView.setImageResource(R.drawable.vietnam);//vietnam
            }
            else if (countries[position].getId() == 195) {
                holder.imageView.setImageResource(R.drawable.philippines);//philippines
            }
            else if (countries[position].getId() == 301) {
                holder.imageView.setImageResource(R.drawable.serbia);//serbia
            }*/

            return convertView;

        }


    }
    public class CountryAdapter extends ArrayAdapter<Country> {


        private Country[] countries;
        private boolean[] booleans_list;
        private List<Country> countriesList;
        private Context context;
        public static final String TAG = "shani";


        public CountryAdapter(Context context, int textViewResourceId, Country[] countries , boolean[] booleans_list) {

            super(context, textViewResourceId, countries);
            this.countries = countries;
            this.booleans_list = booleans_list;
//        this.countriesList = new List<Country>();
//        this.countriesList.addAll(countries);
            this.context = context;
        }
        @Override
        public int getCount() {
            if (countries != null) {
                return countries.length;
            }
            return 0;
        }

        @Override
        public Country getItem(int position) {
            return countries[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*private view holder class*/
        private class ViewHolder {
            ImageView imageView , favourite;
            TextView txtTitle;
            TextView latency;
        }
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            CountryAdapter.ViewHolder holder;
            LayoutInflater myInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView =  myInflater.inflate(R.layout.listitem, null);
            holder = new CountryAdapter.ViewHolder();
//        holder.txtTitle.setTextColor(Color.WHITE);
            holder.txtTitle = (TextView)convertView.findViewById(R.id.country_text);
            holder.latency = (TextView)convertView.findViewById(R.id.latency);
            holder.imageView = (ImageView)convertView.findViewById(R.id.country_image);
            holder.favourite = (ImageView)convertView.findViewById(R.id.favourite);
            holder.txtTitle.setText(countries[position].getName());



            if (booleans_list[position]){
                holder.favourite.setImageResource(R.drawable.filled_heart);
            }else {
                holder.favourite.setImageResource(R.drawable.heart);
            }

            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<Country> savedCountry = new ArrayList<Country>();
                    SharedPreferences mPrefs = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("favourite_list", "");
                    if (json.isEmpty()) {

                        savedCountry = new ArrayList<Country>();
                        savedCountry.add(countries[position]);

                        SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                        Gson gson_put = new Gson();
                        String json_put = gson_put.toJson(savedCountry);
                        prefsEditor.putString("favourite_list", json_put);
                        prefsEditor.commit();

                        holder.favourite.setImageResource(R.drawable.filled_heart);

                    } else {
                        Type type = new TypeToken<List<Country>>() {
                        }.getType();

                        savedCountry = gson.fromJson(json, type);

                        boolean matched = false;
                        for (int i = 0; i <savedCountry.size() ; i++) {
                            if (savedCountry.get(i).getName()==countries[position].getName()){
                                matched = true;
                            }
                        }
                        if (matched){
                            /// delete item from favourites

                            for (int i = 0; i <savedCountry.size() ; i++) {
                                if (savedCountry.get(i).getName() == countries[position].getName()){
                                    savedCountry.remove(i);
                                    SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                                    SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                                    Gson gson_put = new Gson();
                                    String json_put = gson_put.toJson(savedCountry);
                                    prefsEditor.putString("favourite_list", json_put);
                                    prefsEditor.commit();
                                    holder.favourite.setImageResource(R.drawable.heart);
                                }
                            }

                        }else {

                            savedCountry.add(countries[position]);

                            Log.d(TAG,"array........"+savedCountry.toString());

                            SharedPreferences mPrefs_put = context.getSharedPreferences("favourite_list", context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs_put.edit();
                            Gson gson_put = new Gson();
                            String json_put = gson_put.toJson(savedCountry);
                            prefsEditor.putString("favourite_list", json_put);
                            prefsEditor.commit();
                            holder.favourite.setImageResource(R.drawable.filled_heart);

                        }



                    }


                    UpdateCountryList(position);


                }
            });


            if (CountryImageMatcher.GetImageId(countries[position].getName()) != -1){
                holder.imageView.setImageDrawable(context.getResources().getDrawable(CountryImageMatcher.GetImageId(countries[position].getName())));
            }/*else {
            holder.imageView.setImageResource(context.getResources().getDrawable(R.drawable.));
        }*/


           /* if (countries[position].getId() == 254 )
            {
                holder.imageView.setImageResource(R.drawable.us);
            } else if (countries[position].getId() == 253)
            {
                holder.imageView.setImageResource(R.drawable.uk);
            } else if (countries[position].getId() == 14)
            {
                holder.imageView.setImageResource(R.drawable.australia);
            } else if (countries[position].getId() == 91)
            {
                holder.imageView.setImageResource(R.drawable.germany);
            } else if (countries[position].getId() == 63)
            {
                holder.imageView.setImageResource(R.drawable.cyprus);
            } else if (countries[position].getId() == 43)
            {
                holder.imageView.setImageResource(R.drawable.canada);
            }
            else if (countries[position].getId() == 175)
            {
                holder.imageView.setImageResource(R.drawable.netherlands);
            }
            else if (countries[position].getId() == 109)
            {
                holder.imageView.setImageResource(R.drawable.hongkong);
            }
            else if (countries[position].getId() == 233)
            {
                holder.imageView.setImageResource(R.drawable.sweden);
            }
            else if (countries[position].getId() == 122)
            {
                holder.imageView.setImageResource(R.drawable.japan);
            }
            else if (countries[position].getId() == 144)
            {
                holder.imageView.setImageResource(R.drawable.luxembourg);
            }
            else if (countries[position].getId() == 113)
            {
                holder.imageView.setImageResource(R.drawable.india);
            }
            else if (countries[position].getId() == 15)
            {
                holder.imageView.setImageResource(R.drawable.austria);
            }
            else if (countries[position].getId() == 65)
            {
                holder.imageView.setImageResource(R.drawable.denmark);
            }
            else if (countries[position].getId() == 185)
            {
                holder.imageView.setImageResource(R.drawable.norway);
            }
            else if (countries[position].getId() == 64)
            {
                holder.imageView.setImageResource(R.drawable.czech);
            }
            else if (countries[position].getId() == 226)
            {
                holder.imageView.setImageResource(R.drawable.spain);
            }
            else if (countries[position].getId() == 159)
            {
                holder.imageView.setImageResource(R.drawable.mexico);
            }
            else if (countries[position].getId() == 246)
            {
                holder.imageView.setImageResource(R.drawable.turkey);
            }
            else if (countries[position].getId() == 75)
            {
                holder.imageView.setImageResource(R.drawable.estonia);
            }
            else if (countries[position].getId() == 129)
            {
                holder.imageView.setImageResource(R.drawable.kenya);
            }
            else if (countries[position].getId() == 224)
            {
                holder.imageView.setImageResource(R.drawable.southafrica);
            }
            else if (countries[position].getId() == 236)
            {
                holder.imageView.setImageResource(R.drawable.taiwan);
            }
            else if (countries[position].getId() == 16)
            {
                holder.imageView.setImageResource(R.drawable.azerbaijan);
            }
            else if (countries[position].getId() == 35)
            {
                holder.imageView.setImageResource(R.drawable.britishvirgin);
            }
            else if (countries[position].getId() == 198)
            {
                holder.imageView.setImageResource(R.drawable.portugal);
            }
            else if (countries[position].getId() == 199)
            {
                holder.imageView.setImageResource(R.drawable.puertorico);
            }
            else if (countries[position].getId() == 238)
            {
                holder.imageView.setImageResource(R.drawable.tanzania);
            }
            else if (countries[position].getId() == 252)
            {
                holder.imageView.setImageResource(R.drawable.uae);
            }
            else if (countries[position].getId() == 303) {
                holder.imageView.setImageResource(R.drawable.isleofman);
            }
            else if (countries[position].getId() == 149) {
                holder.imageView.setImageResource(R.drawable.malaysia);//malaysia
            }
            else if (countries[position].getId() == 24) {
                holder.imageView.setImageResource(R.drawable.belgium);//belgium
            }
            else if (countries[position].getId() == 82) {
                holder.imageView.setImageResource(R.drawable.france);//france
            }
            else if (countries[position].getId() == 219) {
                holder.imageView.setImageResource(R.drawable.singapore);//singapore
            }
            else if (countries[position].getId() == 119) {
                holder.imageView.setImageResource(R.drawable.italy);//italy
            }
            else if (countries[position].getId() == 117) {
                holder.imageView.setImageResource(R.drawable.ireland);//ireland
            }
            else if (countries[position].getId() == 202) {
                holder.imageView.setImageResource(R.drawable.romania);//romania
            }
            else if (countries[position].getId() == 203) {
                holder.imageView.setImageResource(R.drawable.russian_federation);//russia
            }
            else if (countries[position].getId() == 197) {
                holder.imageView.setImageResource(R.drawable.poland);//poland
            }
            else if (countries[position].getId() == 81) {
                holder.imageView.setImageResource(R.drawable.finland);//finland
            }
            else if (countries[position].getId() == 234) {
                holder.imageView.setImageResource(R.drawable.switzerland);//switzerland
            }
            else if (countries[position].getId() == 251) {
                holder.imageView.setImageResource(R.drawable.ukraine);//ukraine
            }
            else if (countries[position].getId() == 178) {
                holder.imageView.setImageResource(R.drawable.new_zealand);//newzealand
            }
            else if (countries[position].getId() == 137) {
                holder.imageView.setImageResource(R.drawable.latvia);//latvia
            }
            else if (countries[position].getId() == 112) {
                holder.imageView.setImageResource(R.drawable.iceland);//Iceland
            }
            else if (countries[position].getId() == 143) {
                holder.imageView.setImageResource(R.drawable.lithuania);//lithuania
            }
            else if (countries[position].getId() == 95) {
                holder.imageView.setImageResource(R.drawable.greece);//greece
            }
            else if (countries[position].getId() == 220) {
                holder.imageView.setImageResource(R.drawable.slovakia);//slovakia
            }
            else if (countries[position].getId() == 33) {
                holder.imageView.setImageResource(R.drawable.brazil);//brazil
            }
            else if (countries[position].getId() == 59) {
                holder.imageView.setImageResource(R.drawable.costa_rica);//costa_rica
            }
            else if (countries[position].getId() == 111) {
                holder.imageView.setImageResource(R.drawable.hungary);//hungary
            }
            else if (countries[position].getId() == 133) {
                holder.imageView.setImageResource(R.drawable.south_korea);//south_korea
            }
            else if (countries[position].getId() == 239) {
                holder.imageView.setImageResource(R.drawable.thailand);//thailand
            }
            else if (countries[position].getId() == 181) {
                holder.imageView.setImageResource(R.drawable.nigeria);//nigeria
            }
            else if (countries[position].getId() == 163) {
                holder.imageView.setImageResource(R.drawable.moldova);//moldova
            }
            else if (countries[position].getId() == 92) {
                holder.imageView.setImageResource(R.drawable.ghana);//ghana
            }
            else if (countries[position].getId() == 2) {
                holder.imageView.setImageResource(R.drawable.albania);//albania
            }
            else if (countries[position].getId() == 90) {
                holder.imageView.setImageResource(R.drawable.georgia);//georgia
            }
            else if (countries[position].getId() == 114) {
                holder.imageView.setImageResource(R.drawable.indonesia);//indonesia
            }
            else if (countries[position].getId() == 260) {
                holder.imageView.setImageResource(R.drawable.vietnam);//vietnam
            }
            else if (countries[position].getId() == 195) {
                holder.imageView.setImageResource(R.drawable.philippines);//philippines
            }
            else if (countries[position].getId() == 301) {
                holder.imageView.setImageResource(R.drawable.serbia);//serbia
            }
            else if (countries[position].getId() == 49) {
                holder.imageView.setImageResource(R.drawable.china);//serbia
            }
            else if (countries[position].getId() == 37) {
                holder.imageView.setImageResource(R.drawable.bulgaria);//serbia
            }
*/
            return convertView;

        }

    }

}