package com.example.examapp;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentTransaction;
//import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.Menu;
//import android.support.design.widget.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import androidx.core.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;

import com.example.examapp.databinding.ActivityMainBinding;
import com.example.examapp.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;



public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;
    private TextView drawerProfileName, drawerProfileText;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            setFragment(new CategoryFragment());
                            return true;
                        case R.id.nav_dashboard:
                            setFragment(new DashboardFragment());
                            return true;
                        case R.id.nav_account:
                            setFragment(new AccountFragment());
                            return true;
                        case R.id.nav_admin:
                            setFragment(new AdminFragment());
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        getSupportActionBar().setTitle("Categories");
        //part of main code*/

        // setSupportActionBar(binding.appBarMain.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.main_frame);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override//don"t need this part
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*@SuppressLint("ResourceType") ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,binding.appBarMain.toolbar,2,3);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_dashboard)
                .setDrawerLayout(drawer)
                .build();
        //avigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        //vigationView1.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        drawerProfileName = navigationView1.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView1.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

        String name = DBQuery.myProfile.getName();
        drawerProfileName.setText(name);

        drawerProfileText.setText(name.toUpperCase().substring(0, 1));

        setFragment(new CategoryFragment());
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(main_frame.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            setFragment(new CategoryFragment());
        } else if (id == R.id.nav_account) {
            setFragment(new AccountFragment());
        } else if (id == R.id.nav_dashboard) {
            setFragment(new DashboardFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
