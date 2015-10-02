package com.android.denysyuk.conlab.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.models.Organization;
import com.android.denysyuk.conlab.ui.activities.DetailActivity;
import com.android.denysyuk.conlab.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28.09.15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.FinanceViewHolder> implements Filterable{
    public static final String ORGANIZATION_POSITION = "position";
    private Context mContext;
    private Finance mFinance = new Finance();
    private NetworkUtils mUtils;

    public RVAdapter(Context _context, Finance _finance) {
        mContext = _context;
        mFinance.setOrganizations(_finance.getOrganizations());
        mFinance.setCurrencies(_finance.getCurrencies());
        mFinance.setCities(_finance.getCities());
        mFinance.setRegions(_finance.getRegions());
        mFinance.setDate(_finance.getDate());

    }

    @Override
    public FinanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_item, parent, false);

        FinanceViewHolder fvh = new FinanceViewHolder(v);

        mUtils = new NetworkUtils(mContext);

        return fvh;
    }

    @Override
    public void onBindViewHolder(FinanceViewHolder holder, final int position) {
        String cid = mFinance.getOrganizations().get(position).getCityId();
        String rid = mFinance.getOrganizations().get(position).getRegionId();
        holder.mName.setText(mFinance.getOrganizations().get(position).getTitle());
        holder.mRegion.setText(mFinance.getRegions().get(rid));
        holder.mCity.setText(mFinance.getCities().get(cid));
        holder.mPhone.setText("Тел: " + mFinance.getOrganizations().get(position).getPhone());
        holder.mAddress.setText("Адрес : " + mFinance.getOrganizations().get(position).getAddress());

        holder.mBtnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.openLink(mFinance.getOrganizations().get(position).getLink());
            }
        });

        holder.mBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFinance.getOrganizations().get(position).getPhone() != null)
                mUtils.openCall(mFinance.getOrganizations().get(position).getPhone());
            }
        });

        holder.mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(ORGANIZATION_POSITION, mFinance.getOrganizations().get(position).getId());
                mUtils.openMap(b);
            }
        });

        holder.mBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(ORGANIZATION_POSITION, mFinance.getOrganizations().get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mFinance.getOrganizations() != null) {
            return mFinance.getOrganizations().size();
        } else {
            return 0;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if(constraint == null || constraint.length() == 0) {
                    results.values = DataManager.get(mContext).getFinance().getOrganizations();
                    results.count = DataManager.get(mContext).getFinance().getOrganizations().size();
                } else {
                    List<Organization> lists = new ArrayList<>();
                    for(Organization o : DataManager.get(mContext).getFinance().getOrganizations()) {
                        if (o.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())
                                || DataManager.get(mContext).getFinance().getRegions().get(o.getRegionId()).toLowerCase().contains(constraint.toString().toLowerCase())
                                || DataManager.get(mContext).getFinance().getCities().get(o.getCityId()).toLowerCase().contains(constraint.toString().toLowerCase())) {
                            lists.add(o);
                        }
                    }
                    results.values = lists;
                    results.count = lists.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count == 0) {
                    notifyDataSetChanged();
                } else {
                    mFinance.setOrganizations((List<Organization>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

    public static class FinanceViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mName;
        TextView mRegion;
        TextView mCity;
        TextView mPhone;
        TextView mAddress;
        ImageView mBtnLink;
        ImageView mBtnMap;
        ImageView mBtnPhone;
        ImageView mBtnDetail;

        FinanceViewHolder(View itemView){
            super(itemView);
            mCardView = (CardView)itemView.findViewById(R.id.cardViewListItem);
            mName = (TextView)itemView.findViewById(R.id.textItemName);
            mRegion = (TextView)itemView.findViewById(R.id.textItemRegion);
            mCity = (TextView)itemView.findViewById(R.id.textItemCity);
            mPhone = (TextView)itemView.findViewById(R.id.textItemPhone);
            mAddress = (TextView)itemView.findViewById(R.id.textItemAddress);

            mBtnLink = (ImageView)itemView.findViewById(R.id.btnLink);
            mBtnMap = (ImageView)itemView.findViewById(R.id.btnMap);
            mBtnPhone = (ImageView)itemView.findViewById(R.id.btnPhone);
            mBtnDetail = (ImageView)itemView.findViewById(R.id.btnDetail);

        }
    }
}
