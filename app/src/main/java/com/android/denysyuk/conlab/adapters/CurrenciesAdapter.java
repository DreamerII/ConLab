package com.android.denysyuk.conlab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Currencies;
import com.android.denysyuk.conlab.models.Finance;

import java.util.List;

/**
 * Created by root on 28.09.15.
 */
public class CurrenciesAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Currencies> mCurrencies;
    private TextView mTextCashName;
    private TextView mTextAsk;
    private TextView mTextBid;
    private Finance mFinance;
    private ImageView imageAsk;
    private ImageView imageBid;


    public CurrenciesAdapter(Context _context, List<Currencies> _currencies){
        mContext = _context;
        mCurrencies = _currencies;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFinance = DataManager.get(_context).getFinance();
    }

    @Override
    public int getCount() {
        return mCurrencies.size();
    }

    @Override
    public Object getItem(int position) {
        return mCurrencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null)
            v = mInflater.inflate(R.layout.currencies_list_item, parent, false);

        mTextCashName = (TextView)v.findViewById(R.id.textCashName);
        mTextCashName.setText(mFinance.getCurrencies().get(mCurrencies.get(position).getId()));
        mTextAsk = (TextView)v.findViewById(R.id.textAsk);
        mTextAsk.setText(mCurrencies.get(position).getAsk());
        if(mCurrencies.get(position).getAskIcon() == 1){
            mTextAsk.setTextColor(mContext.getResources().getColor(R.color.accent));
            imageAsk.setImageResource(R.drawable.ic_red_arrow_down);
        }
        mTextBid = (TextView)v.findViewById(R.id.textBid);
        mTextBid.setText(mCurrencies.get(position).getBid());
        if(mCurrencies.get(position).getAskIcon() == 1){
            mTextBid.setTextColor(mContext.getResources().getColor(R.color.accent));
            imageBid.setImageResource(R.drawable.ic_red_arrow_down);
        }


        return v;
    }
}
