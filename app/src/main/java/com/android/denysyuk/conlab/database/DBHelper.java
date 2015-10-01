package com.android.denysyuk.conlab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.denysyuk.conlab.models.City;
import com.android.denysyuk.conlab.models.Currencies;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.models.Organization;
import com.android.denysyuk.conlab.models.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 22.09.15.
 */
public class DBHelper extends SQLiteOpenHelper{
    //Database name & version
    private static final String DB_NAME = "finance.sqlite";
    private static final int DB_VERSION = 1;

    //Tables names
    private static final String TABLE_ORGANIZATIONS = "organizations";
    private static final String TABLE_CURRENCIES = "currencies";
    private static final String TABLE_REGIONS = "regions";
    private static final String TABLE_CITIES = "cities";
    private static final String TABLE_CURRENCIES_VALUE = "currenciesValue";
    private static final String TABLE_DATE = "date";

    //TABLE_ORGANIZATIONS column names
    private static final String ORGANIZATION_ID = "id";
    private static final String ORGANIZATION_TYPE = "orgType";
    private static final String ORGANIZATION_TITLE = "title";
    private static final String ORGANIZATION_REGION_ID = "regionId";
    private static final String ORGANIZATION_CITY_ID = "cityId";
    private static final String ORGANIZATION_PHONE = "phone";
    private static final String ORGANIZATION_ADDRESS = "address";
    private static final String ORGANIZATION_LINK = "link";
    private static final String ORGANIZATION_DATE = "date";

    //TABLE_CURRENCIES column names
    private static final String CURRENCIES_ID = "id";
    private static final String CURRENCIES_NAME = "name";

    //TABLE_CITIES column names
    private static final String CITIES_ID = "id";
    private static final String CITIES_NAME = "name";

    //TABLE_REGIONS column names
    private static final String REGION_ID = "id";
    private static final String REGION_NAME = "name";

    //TABLE_CURRENCIES_VALUE column names
    private static final String CURRENCIES_VALUE_ID = "id";
    private static final String CURRENCIES_VALUE_OID = "oid";
    private static final String CURRENCIES_VALUE_ASK = "ask";
    private static final String CURRENCIES_VALUE_BID = "bid";
    private static final String CURRENCIES_VALUE_ASK_ICON = "ask_icon";
    private static final String CURRENCIES_VALUE_BID_ICON = "bid_icon";

    private static final String COLUMN_DATE = "date";

    public DBHelper(Context _context){
        super(_context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_DATE + " (" + COLUMN_DATE + " text)");

        //Create currencies table
        db.execSQL("create table " + TABLE_CURRENCIES + " (" +
        CURRENCIES_ID + " text, " + CURRENCIES_NAME + " text)");

        //Create regions table
        db.execSQL("create table " + TABLE_REGIONS + " (" +
        REGION_ID + " text, " + REGION_NAME + " text)");

        //Create cities table
        db.execSQL("create table " + TABLE_CITIES + " (" +
        CITIES_ID + " text, " + CITIES_NAME + " text)");

        //Create organizations table
        db.execSQL("create table " + TABLE_ORGANIZATIONS + " (" +
        "_id integer primary key autoincrement, " +
        ORGANIZATION_ID + " text, " + ORGANIZATION_TYPE + " integer, " +
        ORGANIZATION_TITLE + " text, " + ORGANIZATION_REGION_ID + " text, " +
        ORGANIZATION_CITY_ID + " text, " +
        ORGANIZATION_PHONE + " integer, " + ORGANIZATION_ADDRESS + " text, " + ORGANIZATION_LINK + " text)");

        //Create currencies_value table
        db.execSQL("create table " + TABLE_CURRENCIES_VALUE + " (" +
        CURRENCIES_VALUE_ID + " text, " + CURRENCIES_VALUE_OID + " text, "
                + CURRENCIES_VALUE_ASK + " text, " +
        CURRENCIES_VALUE_BID + " text, " + CURRENCIES_VALUE_ASK_ICON + " text, "
        + CURRENCIES_VALUE_BID_ICON + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCity(Map<String, String> _cities){
        getWritableDatabase().delete(TABLE_CITIES, null, null);
        Set<String> keys = _cities.keySet();
        String[] array = keys.toArray(new String[keys.size()]);
        for (String s : array){
            ContentValues cv = new ContentValues();
            cv.put(CITIES_ID, s);
            cv.put(CITIES_NAME, _cities.get(s));

            getWritableDatabase().insert(TABLE_CITIES, null, cv);
        }
    }

    public void insertRegion(Map<String, String> _region){
        getWritableDatabase().delete(TABLE_REGIONS, null, null);
        Set<String> keys = _region.keySet();
        String[] array = keys.toArray(new String[keys.size()]);
        for (String s : array){
            ContentValues cv = new ContentValues();
            cv.put(REGION_ID, s);
            cv.put(REGION_NAME, _region.get(s));

            getWritableDatabase().insert(TABLE_REGIONS, null, cv);
        }
    }

    public void insertCurrency(Map<String, String> _currency){
        getWritableDatabase().delete(TABLE_CURRENCIES, null, null);
        Set<String> keys = _currency.keySet();
        String[] array = keys.toArray(new String[keys.size()]);
        for (String s : array){
            ContentValues cv = new ContentValues();
            cv.put(CURRENCIES_ID, s);
            cv.put(CURRENCIES_NAME, _currency.get(s));

            getWritableDatabase().insert(TABLE_CURRENCIES, null, cv);
        }
    }

    public void insertOrganization(List<Organization> _lists){
        getWritableDatabase().delete(TABLE_ORGANIZATIONS, null, null);
        for(Organization o : _lists){
            ContentValues cv = new ContentValues();
            cv.put(ORGANIZATION_ID, o.getId());
            cv.put(ORGANIZATION_TYPE, o.getOrgType());
            cv.put(ORGANIZATION_TITLE, o.getTitle());
            cv.put(ORGANIZATION_REGION_ID, o.getRegionId());
            cv.put(ORGANIZATION_CITY_ID, o.getCityId());
            cv.put(ORGANIZATION_PHONE, o.getPhone());
            cv.put(ORGANIZATION_ADDRESS, o.getAddress());
            cv.put(ORGANIZATION_LINK, o.getLink());

            getWritableDatabase().insert(TABLE_ORGANIZATIONS, null, cv);
            insertCurrencies(o.getCurrencies(), o.getId());
        }
    }

    public void insertCurrencies(List<Currencies> _lists, final String _id) {
        List<Currencies> currencies = new ArrayList<>();
        currencies = getCurrencies(_id);
        String selection = CURRENCIES_ID + " LIKE ?";
        //getWritableDatabase().delete(TABLE_CURRENCIES_VALUE, null, null);
        for(Currencies c : _lists){
            String[] selectionArgs = {c.getId()};
            Currencies oldCurrencies = getCurrenciesId(currencies, c.getId());
            ContentValues cv = new ContentValues();
            cv.put(CURRENCIES_VALUE_ID, c.getId());
            cv.put(CURRENCIES_VALUE_OID, _id);
            cv.put(CURRENCIES_VALUE_ASK, c.getAsk());
            cv.put(CURRENCIES_VALUE_BID, c.getBid());
            cv.put(CURRENCIES_VALUE_ASK_ICON, setAskIcon(oldCurrencies, c));
            cv.put(CURRENCIES_VALUE_BID_ICON, setBidIcon(oldCurrencies, c));

            if(getCurrenciesId(currencies, _id) != null)
                getWritableDatabase().update(TABLE_CURRENCIES_VALUE,
                        cv, selection, selectionArgs);

            getWritableDatabase().insert(TABLE_CURRENCIES_VALUE, null, cv);

        }
    }

    private int setAskIcon(Currencies _oldCurrencies, Currencies _newCurrencies){
        if(_oldCurrencies != null) {
            Double oldAsk = Double.valueOf(_oldCurrencies.getAsk());
            Double newAsk = Double.valueOf(_newCurrencies.getAsk());

            if (newAsk > oldAsk) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    private int setBidIcon(Currencies _oldCurrencies, Currencies _newCurrencies){
        if(_oldCurrencies != null) {
            Double oldBid = Double.valueOf(_oldCurrencies.getBid());
            Double newBid = Double.valueOf(_newCurrencies.getBid());

            if (newBid > oldBid) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public Map<String, String> getCities(){
        Cursor c = getReadableDatabase().query(TABLE_CITIES, null, null, null, null, null, null);
        Map<String, String> map = new HashMap<>();
        if(c != null){
            while (c.moveToNext()){
                map.put(c.getString(c.getColumnIndex(CITIES_ID)), c.getString(c.getColumnIndex(CITIES_NAME)));
            }
            return map;
        }
        return map;
    }

    public Map<String, String> getRegion(){
        Cursor c = getReadableDatabase().query(TABLE_REGIONS, null, null, null, null, null, null);
        Map<String, String> map = new HashMap<>();
        if(c != null){
            while (c.moveToNext()){
                map.put(c.getString(c.getColumnIndex(REGION_ID)), c.getString(c.getColumnIndex(REGION_NAME)));
            }
            return map;
        }
        return map;
    }

    public Map<String, String> getCurrency(){
        Cursor c = getReadableDatabase().query(TABLE_CURRENCIES, null, null, null, null, null, null);
        Map<String, String> map = new HashMap<>();
        if(c != null){
            while (c.moveToNext()){
                map.put(c.getString(c.getColumnIndex(CURRENCIES_ID)), c.getString(c.getColumnIndex(CURRENCIES_NAME)));
            }
            return map;
        }
        return map;
    }

    public List<Currencies> getCurrencies(String _id){
        List<Currencies> lists = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_CURRENCIES_VALUE, null,
                CURRENCIES_VALUE_OID + " LIKE ?", new String[] {_id}, null, null, null, null);
        if(c != null){
            while (c.moveToNext()){
                Currencies currencies = new Currencies();
                currencies.setId(c.getString(c.getColumnIndex(CURRENCIES_VALUE_ID)));
                currencies.setOrgId(c.getString(c.getColumnIndex(CURRENCIES_VALUE_OID)));
                currencies.setAsk(c.getString(c.getColumnIndex(CURRENCIES_VALUE_ASK)));
                currencies.setBid(c.getString(c.getColumnIndex(CURRENCIES_VALUE_BID)));
                currencies.setAskIcon(c.getInt(c.getColumnIndex(CURRENCIES_VALUE_ASK_ICON)));
                currencies.setBidIcon(c.getInt(c.getColumnIndex(CURRENCIES_VALUE_BID_ICON)));

                lists.add(currencies);

            }
        }

        return lists;

    }

    public List<Organization> getOrganizations(){
        List<Organization> lists = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_ORGANIZATIONS, null, null, null, null, null, null);
        if(c != null){
            while (c.moveToNext()){
                Organization o = new Organization();
                String id = c.getString(c.getColumnIndex(ORGANIZATION_ID));
                o.setId(id);
                o.setOrgType(c.getString(c.getColumnIndex(ORGANIZATION_TYPE)));
                o.setTitle(c.getString(c.getColumnIndex(ORGANIZATION_TITLE)));
                o.setRegionId(c.getString(c.getColumnIndex(ORGANIZATION_REGION_ID)));
                o.setCityId(c.getString(c.getColumnIndex(ORGANIZATION_CITY_ID)));
                o.setPhone(c.getString(c.getColumnIndex(ORGANIZATION_PHONE)));
                o.setAddress(c.getString(c.getColumnIndex(ORGANIZATION_ADDRESS)));
                o.setLink(c.getString(c.getColumnIndex(ORGANIZATION_LINK)));
                o.setCurrencies(getCurrencies(id));

                lists.add(o);
            }
        }
        return lists;
    }

    private Currencies getCurrenciesId(List<Currencies> list, String _id){
        for(Currencies c : list){
            if(c.getId().equals(_id))
                return c;
        }
        return null;
    }

}
