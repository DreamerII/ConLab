package com.android.denysyuk.conlab.models;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 28.09.15.
 */
public class Finance {
    private String mDate;
    private List<Organization> mOrganizations;
    private Map<String, String> mCurrencies;
    private Map<String, String> mRegions;
    private Map<String, String> mCities;

    public String getDate() {
        return mDate;
    }

    public Organization getOrganizationId(String id) {
        if(getOrganizations() != null)
        for(Organization o : getOrganizations()){
            if(o.getId().equals(id))
                return o;
        }
        return null;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public List<Organization> getOrganizations() {
        return mOrganizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        mOrganizations = organizations;
    }

    public Map<String, String> getCurrencies() {
        return mCurrencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        mCurrencies = currencies;
    }

    public Map<String, String> getRegions() {
        return mRegions;
    }

    public void setRegions(Map<String, String> regions) {
        mRegions = regions;
    }

    public Map<String, String> getCities() {
        return mCities;
    }

    public void setCities(Map<String, String> cities) {
        mCities = cities;
    }
}
