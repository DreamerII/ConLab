package com.android.denysyuk.conlab.models;

import java.util.List;

/**
 * Created by root on 27.09.15.
 */
public class Organization {
    private String mId;
    private String mOrgType;
    private String mTitle;
    private String mRegionId;
    private String mCityId;
    private String mPhone;
    private String mAddress;
    private String mLink;
    private List<Currencies> mCurrencies;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOrgType() {
        return mOrgType;
    }

    public void setOrgType(String orgType) {
        mOrgType = orgType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getRegionId() {
        return mRegionId;
    }

    public void setRegionId(String regionId) {
        mRegionId = regionId;
    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        mCityId = cityId;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public List<Currencies> getCurrencies() {
        return mCurrencies;
    }

    public void setCurrencies(List<Currencies> currencies) {
        mCurrencies = currencies;
    }
}
