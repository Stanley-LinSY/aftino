package com.sp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Aftino extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView navView;
    private DetailsFragment detailfragment;
    private ListFragment listfragment;
    private AR ar;
    private int bottomSelectedMenu = R.id.reallist;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        navView = findViewById(R.id.bottomNavigationView);
        navView.setOnItemSelectedListener(menuSelected);
        detailfragment = new DetailsFragment();
        listfragment = new ListFragment();
        ar = new AR();

        //set a Toolbar to replace the ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer layout instance to toggle the menu icon to open
        drawerLayout = findViewById(R.id.realdrawer);
        //drawer and back button to close drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        //pass the open and close toggle for the drawer layout listener
        //to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navSelected);
    }

    @Override
    protected void onStart(){
        //set list tab as the default
        navView.setSelectedItemId(R.id.reallist);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case (R.id.add):
                navView.setSelectedItemId(R.id.realdetail);
                Bundle bundle = new Bundle(); //Create bundle
                bundle.putString("id", null);
                detailfragment.getParentFragmentManager().setFragmentResult("listToDetailKey", bundle);
                //getParentFragmentManager().setFragmentResult("listToDetailKey", bundle);
                //activate the BottomNavigationView's detail menu
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    NavigationBarView.OnItemSelectedListener menuSelected = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch(id) {
                //select detail menu option - display the detail fragment in the FrameLayout
                case (R.id.realdetail): fragmentManager.beginTransaction()
                        .replace(R.id.realestateFragmentContainer, detailfragment)
                        .setReorderingAllowed(true)
                        //.addToBackStack(null)
                        .commit();
                    return true;
                //select item menu option - display the detail fragment in the FrameLayout
                case (R.id.reallist): fragmentManager.beginTransaction()
                        .replace(R.id.realestateFragmentContainer, listfragment)
                        .setReorderingAllowed(true)
                        //.addToBackStack(null)
                        .commit();
                    return true;
                    //AR activity
                case (R.id.realar): Intent intent;
                    intent = new Intent(Aftino.this, AR.class);
                    startActivity(intent);
            }
            return false;
        }
    };

    NavigationView.OnNavigationItemSelectedListener navSelected = new NavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //handle navigation view item clicks here
            int id = item.getItemId();
            if (id == R.id.nav_exit) {
                finish();
            } else {
                Fragment fragment = null;
                switch (id) {
                    case R.id.nav_list: fragment = new ListFragment();
                        break;
                    case R.id.nav_add: fragment = new DetailsFragment();
                        break;
                    case R.id.nav_pre_loan: fragment = new PreLoanFragment();
                        break;
                    case R.id.nav_about: fragment = new AboutFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.realestateFragmentContainer, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        }
    };
}