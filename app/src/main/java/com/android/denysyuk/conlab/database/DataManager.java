package com.android.denysyuk.conlab.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.utils.NetworkUtils;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 28.09.15.
 */
public class DataManager {
    private static DataManager sDataManager;
    private Context mContext;
    private DBHelper mDBHelper;
    private Finance mFinance;
    private NetworkUtils mUtils;
    private String mDate;
    private String mPrefDate="";
    private static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_DATE = "date";
    private SharedPreferences mPreference;

    public void setDate(String _date){
        mDate = _date;
    }

    public String getDate(){
        return mDate;
    }


    private DataManager(Context _context){
        mContext = _context;
        mFinance = new Finance();
        mUtils = new NetworkUtils(_context);
        mDBHelper = new DBHelper(_context);
        mPreference = mContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        getPreferences();
    }

    public static DataManager get(Context _context){
        if(sDataManager == null) {
            sDataManager = new DataManager(_context.getApplicationContext());
        }
        return sDataManager;
    }

    private void getPreferences(){
        if(mPreference.contains(APP_PREFERENCES_DATE)){
            mPrefDate = mPreference.getString(APP_PREFERENCES_DATE, " ");
            Log.d("DENYSYUK", "PREFS DATE = " + mPrefDate);
        }
    }

    private void setPreferences(String _date){
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(APP_PREFERENCES_DATE, _date);
        edit.apply();
    }


    public void setFinance(Finance _finance){
        mFinance = _finance;
        if(!mFinance.getDate().equals(mPrefDate)) {
            setPreferences(mFinance.getDate());
            insertDB(mFinance);
            Log.d("DENYSYUK", "INSERT");
        }
    }

    public void insertDB(Finance _finance){
        Finance finance = _finance;
        if(finance != null){
            mDBHelper.insertCity(finance.getCities());
            mDBHelper.insertRegion(finance.getRegions());
            mDBHelper.insertCurrency(finance.getCurrencies());
            mDBHelper.insertOrganization(finance.getOrganizations());
        }
    }

    public Finance readDB() throws ExecutionException, InterruptedException {
        return new DBLoader().execute().get();
    }

    public Finance getFinance(){
            return mFinance;
    }

    private class DBLoader extends AsyncTask<Void, Void, Finance> {
        @Override
        protected Finance doInBackground(Void... params) {
            mFinance.setDate(mDate);
            mFinance.setCities(mDBHelper.getCities());
            mFinance.setRegions(mDBHelper.getRegion());
            mFinance.setCurrencies(mDBHelper.getCurrency());
            mFinance.setOrganizations(mDBHelper.getOrganizations());

            return mFinance;
        }
    }

}
