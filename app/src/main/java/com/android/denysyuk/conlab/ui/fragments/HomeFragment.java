package com.android.denysyuk.conlab.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.adapters.RVAdapter;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.utils.services.DataLoaderIntentService;
import com.android.denysyuk.conlab.utils.NetworkUtils;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 27.09.15.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_DATE = "date";
    private SearchView mSearchView;
    private Finance mFinance;
    private DataManager mDataManager;
    private RecyclerView mRecyclerView;
    private RVAdapter mRVAdapter;
    private NetworkUtils mUtils;
    private BroadcastReceiver mReceiver;
    private SwipeRefreshLayout swipeRefreshHome;
    private String mFilterString;
    private SharedPreferences mPreferences;
    private String mDate = "77";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        mPreferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mPreferences.contains(APP_PREFERENCES_DATE)){
            mDate = mPreferences.getString(APP_PREFERENCES_DATE, "");
        }

        mUtils = new NetworkUtils(getActivity());
        mUtils.runReceiver();

        listenerReceiver();

        mDataManager = DataManager.get(getActivity());
        if (mUtils.isConnectingToInternet()) {
            mFinance = mDataManager.getFinance();
        } else {
            try {
                mFinance = mDataManager.readDB();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);

        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar_actionbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mSearchView = (SearchView)v.findViewById(R.id.action_search);

        swipeRefreshHome = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshHome);
        swipeRefreshHome.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);

        if(mFinance.getOrganizations() != null){
            setRVAdapter();
        }

        if(mUtils.isConnectingToInternet()) {
            runService();
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.requestFocus();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(this);
    }

    private void setRVAdapter(){
        mRVAdapter = new RVAdapter(getActivity(), mFinance);
        mRVAdapter.getFilter().filter(mFilterString);
        mRecyclerView.setAdapter(mRVAdapter);
        mRVAdapter.notifyDataSetChanged();
    }

    private void runService(){
        Intent i = new Intent(getActivity(), DataLoaderIntentService.class);
        getActivity().startService(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(mFinance.getOrganizations() != null) {
            mRVAdapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(mFinance.getOrganizations() != null) {
            mFilterString = newText;
            mRVAdapter.getFilter().filter(newText);
        }
        return false;
    }

    @Override
    public void onRefresh() {
        if(mUtils.isConnectingToInternet()) {
            runService();
            mFinance = mDataManager.getFinance();
        } else {
            swipeRefreshHome.setRefreshing(false);
        }
        mRVAdapter.notifyDataSetChanged();
    }

    private void listenerReceiver(){
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String intentAction = intent.getAction();
                if(intentAction.equals("com.android.denysyuk.conlab.utils")){
                    swipeRefreshHome.setRefreshing(false);
                    setRVAdapter();
                }
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.android.denysyuk.conlab.utils");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }
}
