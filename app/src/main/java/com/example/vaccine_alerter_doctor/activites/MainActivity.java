package com.example.vaccine_alerter_doctor.activites;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.adapters.ChildrenAdapter;
import com.example.vaccine_alerter_doctor.models.ChildModel;
import com.example.vaccine_alerter_doctor.network.NetWorker;
import com.example.vaccine_alerter_doctor.others.DividerItemDecoration;
import com.example.vaccine_alerter_doctor.util.Mtandao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab1, fab2, fab3;
    private Boolean   isFABOpen = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private View view;
    private RecyclerView recyclerView;
    private ChildrenAdapter childrenAdapter;
    private ArrayList<ChildModel> childrenList;
    private DrawerLayout drawerLayout;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUiConfig();
    }
    private void setUiConfig(){

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });


        view = getWindow().getDecorView().getRootView();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_children);
        // activity_site_list = (View) findViewById(R.id.activity_rental_list_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.child_swipe_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Children List");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);


        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                loadChildren();

            }

        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        childrenList = new ArrayList<>();
        childrenAdapter = new ChildrenAdapter(childrenList);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(childrenAdapter);

        View header = navigationView.getHeaderView(0);
        TextView nav_names = (TextView) header.findViewById(R.id.nav_names);
        TextView nav_number = (TextView) header.findViewById(R.id.nav_number);
        TextView nav_gender = (TextView) header.findViewById(R.id.nav_gender);

        nav_names.setText(new PreferenceManager(this).getGuardianName());
        nav_number.setText(new PreferenceManager(this).getGuardianNumber());
        nav_gender.setText(new PreferenceManager(this).getGuardianGender());

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();




/*
        sitesData       = new ArrayList<>();
        sitesAdapter    = new SitesAdapter(sitesData,SiteListActivity.this, addStatus);
        recyclerView    = (RecyclerView) findViewById(R.id.rental_list_recycler_view);

        Configuration orientation = new Configuration();

        if(this.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else if (this.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sitesAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

                Log.d("Paginating--->","Please wait");
                progressBar.setVisibility(View.VISIBLE);
                if(!addStatus && !endPagination) {

                    loadSiteData();
                }

            }
        });
        */

    }
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }
    private void loadChildren() {

        swipeRefreshLayout.setRefreshing(true);

        if (Mtandao.checkInternet(getApplicationContext())) {

            new NetWorker().loadChildren(this, String.valueOf(new PreferenceManager(this).getGuardianId()));

        } else {

            showSnackBar("Check internet connection and try again!");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(false);

    }

    @Override
    public void onLoadValidResponse(JSONObject response) {

        childrenList.clear();
        extractJSONResponse(response);
        displayChildrenList();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoadErrorResponse(String response) {

        swipeRefreshLayout.setRefreshing(false);
        showSnackBar("Something went wrong.Try again later");

    }

    private void showSnackBar(String mesg) {

        Snackbar.make(view, mesg, Snackbar.LENGTH_SHORT)
                .show();

    }

    private void extractJSONResponse(JSONObject json) {


        try {

            for (int i = 0; i < json.getJSONArray("children").length(); i++) {

                Boolean vaccineDue = false;

                int id = json.getJSONArray("children").getJSONObject(i).getInt("id");
                String firstName = json.getJSONArray("children").getJSONObject(i).getString("first_name");
                String lastName = json.getJSONArray("children").getJSONObject(i).getString("last_name");
                String gender = json.getJSONArray("children").getJSONObject(i).getString("gender");

                int opv1_due = json.getJSONArray("children").getJSONObject(i).getInt("opv1_due");
                int bcg1_due = json.getJSONArray("children").getJSONObject(i).getInt("bcg1_due");
                int hepB1_due = json.getJSONArray("children").getJSONObject(i).getInt("hepB1_due");

                int dpt1_due = json.getJSONArray("children").getJSONObject(i).getInt("dpt1_due");
                int hibB1_due = json.getJSONArray("children").getJSONObject(i).getInt("hibB1_due");
                int hepB2_due = json.getJSONArray("children").getJSONObject(i).getInt("hepB2_due");

                int opv2_due = json.getJSONArray("children").getJSONObject(i).getInt("opv2_due");
                int pneu_due = json.getJSONArray("children").getJSONObject(i).getInt("pneu_due");
                int rota1_due = json.getJSONArray("children").getJSONObject(i).getInt("rota1_due");

                int dpt2_due = json.getJSONArray("children").getJSONObject(i).getInt("dpt2_due");
                int hibB2_due = json.getJSONArray("children").getJSONObject(i).getInt("hibB2_due");
                int hepB3_due = json.getJSONArray("children").getJSONObject(i).getInt("hepB3_due");

                int opv3_due = json.getJSONArray("children").getJSONObject(i).getInt("opv3_due");
                int vitA1_due = json.getJSONArray("children").getJSONObject(i).getInt("vitA1_due");
                int rota2_due = json.getJSONArray("children").getJSONObject(i).getInt("rota2_due");

                int vitA2_due = json.getJSONArray("children").getJSONObject(i).getInt("vitA2_due");
                int measles_due = json.getJSONArray("children").getJSONObject(i).getInt("measles_due");
                int yellow_due = json.getJSONArray("children").getJSONObject(i).getInt("yellow_due");


                if (opv1_due == 1 || bcg1_due == 1 || hepB1_due == 1 ||
                        dpt1_due == 1 || hibB1_due == 1 || hepB2_due == 1 ||
                        opv2_due == 1 || pneu_due == 1 || rota1_due == 1 ||
                        dpt2_due == 1 || hibB2_due == 1 || hepB3_due == 1 ||
                        opv3_due == 1 || vitA1_due == 1 || rota2_due == 1 ||
                        vitA2_due == 1 || measles_due == 1 || yellow_due == 1
                ) {

                    vaccineDue = true;

                }

                childrenList.add(new ChildModel(id, firstName, lastName, gender,
                        opv1_due, bcg1_due, hepB1_due, dpt1_due, hibB1_due, hepB2_due,
                        opv2_due, pneu_due, rota1_due, dpt2_due, hibB2_due, hepB3_due,
                        opv3_due, vitA1_due, rota2_due, vitA2_due, measles_due, yellow_due, vaccineDue));

            }

            Log.d("Children------->", childrenList.toString());
        } catch (JSONException jsonE) {


            Log.d("Err---->", jsonE.toString());
        }


    }

    public void displayChildrenList() {

        childrenAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d("---->","Sign out");
        if (id == R.id.nav_log_out) {

            Log.d("---->","Sign out");
            Intent intent = new Intent(this, Intro.class);
            clearGuardianDetails();
            stopScheduledJob();
            startActivity(intent);

        }
        return true;
    }

    private void clearGuardianDetails(){

        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.setGuardianNumber(null);
        preferenceManager.setGuardianGender(null);
        preferenceManager.setGuardianId(-1);
        preferenceManager.setGuardianName(null);

    }


}
