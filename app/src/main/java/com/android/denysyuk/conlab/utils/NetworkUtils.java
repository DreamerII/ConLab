package com.android.denysyuk.conlab.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.android.denysyuk.conlab.adapters.RVAdapter;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Organization;
import com.android.denysyuk.conlab.ui.activities.MapActivity;
import com.android.denysyuk.conlab.utils.receivers.ConventerReceiver;
import com.android.denysyuk.conlab.utils.services.DataLoaderIntentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 25.09.15.
 */
public class NetworkUtils {
    private static final int ALARM_TIME = 1800000;

    private Context mContext;

    public NetworkUtils(Context _context){
        this.mContext = _context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }

    public void runReceiver(){
        if (isConnectingToInternet()) {
            Intent intent = new Intent(mContext, ConventerReceiver.class);
            mContext.startService(new Intent(mContext, DataLoaderIntentService.class));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), ALARM_TIME, pendingIntent);
        } else {
            Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void openLink(String link){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        mContext.startActivity(intent);
    }

    public void openMap(Bundle args){
        Intent intent = new Intent(mContext, MapActivity.class);
        intent.putExtra(RVAdapter.ORGANIZATION_POSITION, args);
        mContext.startActivity(intent);
    }

    public void openCall(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        mContext.startActivity(intent);
    }

    public int getPositionId(String id){
        List<Organization> lists = new ArrayList<>();
        lists = DataManager.get(mContext).getFinance().getOrganizations();
        for(int i=0; i<lists.size(); i++){
            if(lists.get(i).getId().equals(id)){
                return i;
            }
        }
        return 0;
    }
}
