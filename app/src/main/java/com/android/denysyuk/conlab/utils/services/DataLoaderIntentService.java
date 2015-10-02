package com.android.denysyuk.conlab.utils.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.denysyuk.conlab.utils.loaders.JsonDataLoader;

/**
 * Created by root on 28.09.15.
 */
public class DataLoaderIntentService extends IntentService {

    public DataLoaderIntentService() {
        super("DataLoaderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JsonDataLoader jsonDataLoader = new JsonDataLoader(getApplicationContext());
        jsonDataLoader.updateData();
    }
}
