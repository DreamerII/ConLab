package com.android.denysyuk.conlab.utils.loaders;

import android.os.AsyncTask;

import com.android.denysyuk.conlab.api.ApiConstant;
import com.android.denysyuk.conlab.utils.RestClient;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by root on 28.09.15.
 */
public class PlaceMapLoader extends AsyncTask<String, Void, LatLng> {

    @Override
    protected LatLng doInBackground(String... args) {
        return getLatLng(getUrl(args[0]));
    }

    private String getUrl(String location) {
        try {
            location = URLEncoder.encode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address = "address=" + location;
        String sensor = "sensor=false";
        String url = ApiConstant.BASE_PLACE_URL + address + "&" + sensor;
        return url;
    }

    private LatLng getLatLng(String uri) {
        String mJsonString = null;

        try {
            mJsonString = new RestClient().getUrl(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object;
        double lat = 0;
        double lng = 0;
        try {
            object = new JSONObject(mJsonString);
            JSONArray array =new JSONArray(object.getJSONArray("results").toString());

            lat = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            lng = array.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng latLng = new LatLng(lat, lng);
        return latLng;
    }
}
