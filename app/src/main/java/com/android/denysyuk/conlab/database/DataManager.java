package com.android.denysyuk.conlab.database;

import android.content.Context;
import android.util.Log;

import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.utils.NetworkUtils;

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
    }

    public static DataManager get(Context _context){
        if(sDataManager == null) {
            sDataManager = new DataManager(_context.getApplicationContext());
        }
        return sDataManager;
    }


    public void setFinance(Finance _finance){
        mFinance = _finance;
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

    public Finance readDB(){
        Log.d("DENYSYUK", "READ DATABASE");
        mFinance.setDate(mDate);
        mFinance.setCities(mDBHelper.getCities());
        mFinance.setRegions(mDBHelper.getRegion());
        mFinance.setCurrencies(mDBHelper.getCurrency());
        mFinance.setOrganizations(mDBHelper.getOrganizations());

        return mFinance;
    }

    public Finance getFinance(){
            return mFinance;
    }

}