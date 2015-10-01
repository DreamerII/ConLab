package com.android.denysyuk.conlab.models;

/**
 * Created by root on 28.09.15.
 */
public class Currencies {
    private String mOrgId;
    private String mId;
    private String mAsk;
    private String mBid;
    private int mAskIcon;
    private int mBidIcon;

    public String getOrgId() {
        return mOrgId;
    }

    public void setOrgId(String orgId) {
        mOrgId = orgId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAsk() {
        return mAsk;
    }

    public void setAsk(String ask) {
        mAsk = ask;
    }

    public String getBid() {
        return mBid;
    }

    public void setBid(String bid) {
        mBid = bid;
    }

    public int getAskIcon() {
        return mAskIcon;
    }

    public void setAskIcon(int askIcon) {
        mAskIcon = askIcon;
    }

    public int getBidIcon() {
        return mBidIcon;
    }

    public void setBidIcon(int bidIcon) {
        mBidIcon = bidIcon;
    }
}
