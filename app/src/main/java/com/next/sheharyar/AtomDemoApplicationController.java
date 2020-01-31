/*
 * Copyright (c) 2018 Atom SDK Demo.
 * All rights reserved.
 */
package com.next.sheharyar;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import android.text.TextUtils;
import android.widget.Toast;

import com.atom.core.exceptions.AtomValidationException;
import com.atom.core.models.AccessToken;
import com.atom.core.models.AtomConfiguration;
import com.atom.sdk.android.AtomManager;
import com.next.sheharyar.logger.Log;
//import com.atom.vpn.demo.common.Constants;


/**
 * AtomDemoApplicationController
 */

public class AtomDemoApplicationController extends Application {

    private static AtomDemoApplicationController mInstance;
    private AtomManager atomManager;
    public static final String TAG = "shani";
    AccessToken accessToken;


    public static synchronized AtomDemoApplicationController getInstance() {
        return mInstance;
    }

    public AtomManager getAtomManager() {
        return atomManager;
    }

    public void SetAtomManager(AtomManager atomManager){
        this.atomManager = atomManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Log.d("shani","onCreate of AtomDemo");
        //put Atom Application Secret here
//        String ATOM_SECRET_KEY = getString(R.string.atom_secret_key);
        String ATOM_SECRET_KEY = "b12e0405d803ba771c46bb94be29a0a59f976b06";

        if (!TextUtils.isEmpty(ATOM_SECRET_KEY)) {
            Log.d(TAG,"secret key exist.......");

            // configure the Atom SDK
            AtomConfiguration.Builder atomConfigurationBuilder = new AtomConfiguration.Builder(ATOM_SECRET_KEY);
            AtomConfiguration atomConfiguration = atomConfigurationBuilder.build();


            try {
//                AtomManager.initialize(this, atomConfiguration, mAtomManager -> atomManager = mAtomManager);

                AtomManager.initialize(this, atomConfiguration, new AtomManager.InitializeCallback() {
                    @Override
                    public void onInitialized(AtomManager aatomManager) {

                        atomManager = aatomManager;
                    }
                });


                Log.d(TAG,"after initialized");
            } catch (AtomValidationException e) {
                e.printStackTrace();
                Log.d(TAG,"exception...."+e.getMessage());
            }
        } else {
            Toast.makeText(this, Constants.SecretKeyRequired, Toast.LENGTH_SHORT).show();
            Log.d(TAG,"empty secret key.......");
        }

    }


    public AccessToken GetAccessToken() {
        return accessToken;
    }

    public void SetAccessToken(AccessToken accessToken){
        this.accessToken = accessToken;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
