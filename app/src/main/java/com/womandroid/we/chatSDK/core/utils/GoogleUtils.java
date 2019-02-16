package com.womandroid.we.chatSDK.core.utils;

/**
 * Created by benjaminsmiley-andrews on 09/06/2017.
 */

import com.google.android.gms.maps.model.LatLng;

import com.womandroid.we.chatSDK.core.session.ChatSDK;

public class GoogleUtils {

    public static String getMapImageURL (LatLng location, int width, int height) {

        String googleMapsAPIKey = ChatSDK.config().googleMapsApiKey;

        String api = "https://maps.googleapis.com/maps/api/staticmap";
        String markers = "markers="+location.latitude+","+location.longitude;
        String size = "zoom=18&size="+width+"x"+ height;
        String key = "key=" + googleMapsAPIKey;

        return api + "?" + markers + "&" + size + "&" + key;
    }

}
