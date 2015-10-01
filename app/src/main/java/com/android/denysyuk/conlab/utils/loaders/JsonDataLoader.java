package com.android.denysyuk.conlab.utils.loaders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.denysyuk.conlab.api.ApiConstant;
import com.android.denysyuk.conlab.database.DBHelper;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Currencies;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.models.Organization;
import com.android.denysyuk.conlab.utils.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 28.09.15.
 */
public class JsonDataLoader {
    private static final String REGIONS = "regions";
    private static final String CITIES = "cities";
    private static final String CURRENCIES = "currencies";
    private static final String ORGANIZATIONS = "organizations";
    private static final String ORGANIZATION_ID = "id";
    private static final String ORGANIZATION_ORG_TYPE = "orgType";
    private static final String ORGANIZATION_TITLE = "title";
    private static final String ORGANIZATION_REGION_ID = "regionId";
    private static final String ORGANIZATION_CITY_ID = "cityId";
    private static final String ORGANIZATION_PHONE = "phone";
    private static final String ORGANIZATION_ADDRESS = "address";
    private static final String ORGANIZATION_LINK = "link";
    private static final String ASK = "ask";
    private static final String BID = "bid";

    private RestClient mRestClient;
    private DataManager mDataManager;
    private Finance mFinance;
    private Context mContext;

    public JsonDataLoader(Context _context){
        mContext = _context;
        mRestClient = new RestClient();
        mDataManager = DataManager.get(_context);
        mFinance = mDataManager.getFinance();
    }

    public Finance updateData(){
        try {
            String result = mRestClient.getUrl(ApiConstant.BASE_URL);
            JSONObject jsonObject = new JSONObject(result);

            String date = jsonObject.getString("date");
            mFinance.setDate(date);

                Map<String, String> regionMap = new HashMap<>();
                regionMap = toMap(new JSONObject(jsonObject.get(REGIONS).toString()));
                mFinance.setRegions(regionMap);

                Map<String, String> cityMap = new HashMap<>();
                cityMap = toMap(new JSONObject(jsonObject.get(CITIES).toString()));
                mFinance.setCities(cityMap);

                Map<String, String> currencyMap = new HashMap<>();
                currencyMap = toMap(new JSONObject(jsonObject.get(CURRENCIES).toString()));
                mFinance.setCurrencies(currencyMap);

                JSONArray jsonOrganizations = new JSONArray(jsonObject.get(ORGANIZATIONS).toString());

                List<Organization> organizations = new ArrayList<>();

                for (int i = 0; i < jsonOrganizations.length(); i++) {
                    Organization o = new Organization();
                    JSONObject object = new JSONObject(jsonOrganizations.get(i).toString());
                    String id = object.getString(ORGANIZATION_ID);
                    o.setId(id);
                    o.setTitle(object.getString(ORGANIZATION_TITLE));
                    o.setOrgType(object.getString(ORGANIZATION_ORG_TYPE));
                    o.setRegionId(object.getString(ORGANIZATION_REGION_ID));
                    o.setCityId(object.getString(ORGANIZATION_CITY_ID));
                    o.setAddress(object.getString(ORGANIZATION_ADDRESS));
                    o.setPhone(object.getString(ORGANIZATION_PHONE));
                    o.setLink(object.getString(ORGANIZATION_LINK));
                    o.setCurrencies(toMapCurrencies(new JSONObject(object.get(CURRENCIES).toString()), id));

                    organizations.add(o);
                }

                mFinance.setOrganizations(organizations);

                Intent intent = new Intent("com.android.denysyuk.conlab.utils");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDataManager.setFinance(mFinance);
        return mFinance;
    }

    public Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<>();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, object.get(key).toString());
        }
        return map;
    }

    public List<Currencies> toMapCurrencies(JSONObject object, String id) throws JSONException {
        List<Currencies> lists = new ArrayList<>();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            JSONObject json = new JSONObject(object.get(key).toString());
            String ask = json.getString(ASK);
            String bid = json.getString(BID);

            Organization o = mFinance.getOrganizationId(id);
            Currencies c = new Currencies();
            if(o != null){
                    Currencies currencies = getCurrenciesId(o.getCurrencies(), key);
                if(currencies != null) {
                    Double oldAsk = Double.valueOf(currencies.getAsk());
                    Double oldBid = Double.valueOf(currencies.getBid());

                    c.setId(key);
                    c.setOrgId(id);
                    c.setAsk(ask);
                    c.setBid(bid);
                    if (Double.valueOf(ask) > oldAsk) {
                        c.setAskIcon(1);
                    } else {
                        c.setAskIcon(0);
                    }
                    if (Double.valueOf(bid) > oldBid) {
                        c.setBidIcon(1);
                    } else {
                        c.setBidIcon(0);
                    }
                }
            } else {

                c.setId(key);
                c.setOrgId(id);
                c.setAsk(ask);
                c.setBid(bid);
                c.setAskIcon(0);
                c.setBidIcon(0);
            }

            lists.add(c);
        }

        return lists;
    }

    private Currencies getCurrenciesId(List<Currencies> lists, String id){
        if(lists != null)
        for(Currencies c : lists){
            if(c.getId() != null)
            if (c.getId().equals(id))
                return c;
        }

        return null;
    }

}
