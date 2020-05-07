package com.example.boatpoolingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.boatpoolingapp.Fragments.FindrideFragment;
import com.example.boatpoolingapp.Fragments.ProfileFragment;
import com.example.boatpoolingapp.Fragments.RidesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

public class Dashboard extends AppCompatActivity implements FindrideFragment.OnFragmentInteractionListener {
    private Toolbar mToolbar;
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bottom_navigation = findViewById(R.id.bottom_navigation);


        setItemSelectedListener(bottom_navigation);
    }


    private void setItemSelectedListener(BottomNavigationView bottom_navigation) {
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Rides:
                        if (!item.isChecked())
                           Toast.makeText(Dashboard.this, "Action Rides", Toast.LENGTH_LONG).show();

                            addFragment(RidesFragment.newInstance());
                        return true;

                    case R.id.action_message:
                        if (!item.isChecked())
                            Toast.makeText(Dashboard.this, "Messages", Toast.LENGTH_LONG).show();

                        //  addFragment(HomeFragment.newInstance());
                        return true;

                    case R.id.action_Profile:
                        if (!item.isChecked())
                            Toast.makeText(Dashboard.this, "Profile", Toast.LENGTH_LONG).show();

                          addFragment(ProfileFragment.newInstance());
                        return true;


                }
                return false;
            }
        });
    }
    public void addFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
