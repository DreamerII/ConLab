package com.android.denysyuk.conlab.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.denysyuk.conlab.utils.NetworkUtils;

/**
 * Created by root on 28.09.15.
 */
public class ConventerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new NetworkUtils(context).runReceiver();
    }
}
