package com.next.sheharyar.APIs;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Shani on 3/11/2019.
 */

public class APIs {

    public static final String GENERAL_URL = "https://user.theprivatefamilynetwork.com/api.php";
    public static final String about = "https://onlinesecurityapp.com/wp-mobile-api.php";
//    public static final String GENERATE_VPN_ACCOUNT = "https://api.platform.purevpn.com/vam/v1/generate";
public static final String GET_ChannelsList = "https://api.atom.purevpn.com/inventory/v1/getAllChannels";
    public static final String GET_ACCESS_TOKEN = "https://api.atom.purevpn.com/auth/v1/accessToken";
    public static final String GENERATE_VPN_ACCOUNT = "https://api.atom.purevpn.com/vam/v1/generate";

    public static final String CITIES_URL = "http://api.atom.purevpn.com/inventory/v1/getCities/272";

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
