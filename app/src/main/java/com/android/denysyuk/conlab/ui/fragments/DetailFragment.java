package com.android.denysyuk.conlab.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.adapters.CurrenciesAdapter;
import com.android.denysyuk.conlab.adapters.RVAdapter;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Currencies;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.utils.NetworkUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

/**
 * Created by root on 28.09.15.
 */
public class DetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private int position;
    private Finance mFinance;

    private TextView mTextName;
    private TextView mTextLink;
    private TextView mTextRegion;
    private TextView mTextCity;
    private TextView mTextPhone;
    private TextView mTextAddress;

    private CurrenciesAdapter mAdapter;
    private ListView mListView;

    private FloatingActionMenu fam;
    private FrameLayout mFrameLayout;
    private FloatingActionButton fabMap;
    private FloatingActionButton fabLink;
    private FloatingActionButton fabPhone;
    private NetworkUtils mUtils;

    private LinearLayout mLayout;
    private View mItemView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        mFinance = DataManager.get(getActivity()).getFinance();

        mUtils = new NetworkUtils(getActivity());

        position = mUtils.getPositionId(getActivity().getIntent().getStringExtra(RVAdapter.ORGANIZATION_POSITION));

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail, container, false);

        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar_actionbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mFinance.getOrganizations().get(position).getTitle());
        toolbar.setSubtitle(mFinance.getCities().get(mFinance.getOrganizations().get(position).getCityId()));

        mLayout = (LinearLayout)v.findViewById(R.id.lLayout);

        if(mFinance.getOrganizations().get(position).getCurrencies() != null)
        addItemView();

        String rid = mFinance.getOrganizations().get(position).getRegionId();
        String cid = mFinance.getOrganizations().get(position).getCityId();

        mTextName = (TextView)v.findViewById(R.id.textName);
        mTextName.setText(mFinance.getOrganizations().get(position).getTitle());
        mTextLink = (TextView)v.findViewById(R.id.textLink);
        mTextLink.setText("Сайт банка: " + mFinance.getOrganizations().get(position).getLink());
        mTextRegion = (TextView)v.findViewById(R.id.textRegion);
        mTextRegion.setText("Область: " + mFinance.getRegions().get(rid));
        mTextCity = (TextView)v.findViewById(R.id.textCity);
        mTextCity.setText("Город: " + mFinance.getCities().get(cid));
        mTextPhone = (TextView)v.findViewById(R.id.textPhone);
        mTextPhone.setText("Телефон: " + mFinance.getOrganizations().get(position).getPhone());
        mTextAddress = (TextView)v.findViewById(R.id.textAddress);
        mTextAddress.setText("Адрес: " + mFinance.getOrganizations().get(position).getAddress());

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshDetail);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mFrameLayout = (FrameLayout)v.findViewById(R.id.frameLayout);
        fam = (FloatingActionMenu)v.findViewById(R.id.menu);
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean b) {
                Log.d("DENYSYUK", "Menu = " + b);
            }
        });
//        fam.setOnFloatingActionsMenuUpdateListener(new FloatingActionMenu.OnFloatingActionsMenuUpdateListener() {
//            @Override
//            public void onMenuExpanded() {
//                mFrameLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onMenuCollapsed() {
//                mFrameLayout.setVisibility(View.GONE);
//            }
//        });

        fabMap = (FloatingActionButton)v.findViewById(R.id.menu_item_map);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(RVAdapter.ORGANIZATION_POSITION, mFinance.getOrganizations().get(position).getId());
                mUtils.openMap(b);
            }
        });
        fabLink = (FloatingActionButton)v.findViewById(R.id.menu_item_link);
        fabLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.openLink(mFinance.getOrganizations().get(position).getLink());
            }
        });
        fabPhone = (FloatingActionButton)v.findViewById(R.id.menu_item_phone);
        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.openCall(mFinance.getOrganizations().get(position).getPhone());
            }
        });

        return v;
    }

    private void addItemView(){
        List<Currencies> lists = mFinance.getOrganizations().get(position).getCurrencies();
        if(mItemView != null)
            mLayout.removeAllViews();
        for(Currencies c : lists) {
            LayoutInflater inflater1 = getActivity().getLayoutInflater();
            mItemView = inflater1.inflate(R.layout.currencies_list_item, null);

            ImageView imageAsk = (ImageView)mItemView.findViewById(R.id.imageAsk);
            ImageView imageBid = (ImageView)mItemView.findViewById(R.id.imageBid);

            TextView text = (TextView) mItemView.findViewById(R.id.textCashName);
            text.setText(mFinance.getCurrencies().get(c.getId()));
            TextView textAsk = (TextView) mItemView.findViewById(R.id.textAsk);
            textAsk.setText(c.getAsk());
            if(c.getAskIcon() == 1){
                textAsk.setTextColor(getResources().getColor(R.color.accent));
                imageAsk.setImageResource(R.drawable.ic_red_arrow_down);
            }
            TextView textbid = (TextView) mItemView.findViewById(R.id.textBid);
            textbid.setText(c.getBid());
            if(c.getBidIcon() == 1){
                textbid.setTextColor(getResources().getColor(R.color.accent));
                imageBid.setImageResource(R.drawable.ic_red_arrow_down);
            }
            mLayout.addView(mItemView);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem searchItem = menu.findItem(R.id.action_share);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ShareDialog dialog = new ShareDialog();
                Bundle b = new Bundle();
                b.putInt(RVAdapter.ORGANIZATION_POSITION, position);
                dialog.setArguments(b);
                dialog.show(getActivity().getSupportFragmentManager(), "Dialog");
                return false;
            }
        });

    }

    @Override
    public void onRefresh() {
        addItemView();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
