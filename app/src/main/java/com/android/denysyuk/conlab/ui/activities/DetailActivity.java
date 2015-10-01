package com.android.denysyuk.conlab.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.ui.fragments.DetailFragment;

/**
 * Created by root on 28.09.15.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new DetailFragment()).commit();
    }
}
