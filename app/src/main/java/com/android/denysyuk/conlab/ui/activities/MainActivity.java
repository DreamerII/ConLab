package com.android.denysyuk.conlab.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.ui.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container);

        if(fragment == null){
            fragment = new HomeFragment();
            fm.beginTransaction().replace(R.id.container, fragment).commit();
        }

    }
}
