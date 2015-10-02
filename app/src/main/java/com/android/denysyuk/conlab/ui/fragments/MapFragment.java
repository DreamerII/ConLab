package com.android.denysyuk.conlab.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.adapters.RVAdapter;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.utils.NetworkUtils;
import com.android.denysyuk.conlab.utils.loaders.PlaceMapLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 28.09.15.
 */
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback{
    private GoogleApiClient mGoogleApiClient;
    private PlaceMapLoader geo;
    private int position;
    private Finance mFinance;
    private String location;
    private NetworkUtils mUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUtils = new NetworkUtils(getActivity());

        mFinance = DataManager.get(getActivity()).getFinance();

        Bundle b = getActivity().getIntent().getBundleExtra(RVAdapter.ORGANIZATION_POSITION);
        position = mUtils.getPositionId(b.getString(RVAdapter.ORGANIZATION_POSITION));

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        String cid = mFinance.getOrganizations().get(position).getCityId();

        location = mFinance.getCities().get(cid)
        + " " +mFinance.getOrganizations().get(position).getAddress();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_map, container, false);

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


        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(mUtils.isConnectingToInternet()) {
            geo = new PlaceMapLoader();
            geo.execute(location);

            try {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geo.get(), 16));
                googleMap.addMarker(new MarkerOptions().position(geo.get()).title(
                        mFinance.getOrganizations().get(position).getTitle()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
