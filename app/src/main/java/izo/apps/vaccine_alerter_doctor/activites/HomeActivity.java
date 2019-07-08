package izo.apps.vaccine_alerter_doctor.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import izo.apps.vaccine_alerter_doctor.R;
import izo.apps.vaccine_alerter_doctor.data.PreferenceManager;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private int onBackPressCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUIConfig();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_log_out) {

            Intent intent = new Intent(this, LoginActivity.class);
            clearDoctorDetails();
            startActivity(intent);

        }
        return true;
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

    private void setUIConfig() {

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView nav_names = (TextView) header.findViewById(R.id.nav_names);
        TextView nav_number = (TextView) header.findViewById(R.id.nav_number);
        TextView nav_gender = (TextView) header.findViewById(R.id.nav_gender);
        nav_names.setText(new PreferenceManager(this).getDoctorName());
        nav_number.setText(new PreferenceManager(this).getDoctorNumber());
        nav_gender.setText(new PreferenceManager(this).getDoctorGender());
        navigationView.bringToFront();
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Home");
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger);

    }

    private void clearDoctorDetails() {

        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.setDoctorNumber(null);
        preferenceManager.setDoctorGender(null);
        preferenceManager.setDoctorId(-1);
        preferenceManager.setDoctorName(null);

    }

    @Override
    public void onBackPressed() {
        onBackPressCount ++;
        if(onBackPressCount>1){
            exitApp();
        }

    }

    public void onHomeButton(View view) {

        Intent intent = null;

        switch (view.getId()) {

            case R.id.home_card_view_1:
                intent = new Intent(getApplicationContext(), ChildrenListActivity.class);
                intent.putExtra("title","Check Children");
                break;
            case R.id.home_card_view_2:
                intent = new Intent(getApplicationContext(), ChildActivity.class);
                intent.putExtra("title","Add Child");
                intent.putExtra("option",1);

                break;
            case R.id.home_card_view_3:

                intent = new Intent(getApplicationContext(), ChildActivity.class);
                intent.putExtra("title","Edit Child");
                intent.putExtra("option",2);

                break;
            case R.id.home_card_view_4:
                intent = new Intent(getApplicationContext(), GuardianActivity.class);
                intent.putExtra("title","Add Guardian");
                intent.putExtra("option",1);

                break;
            case R.id.home_card_view_5:
                intent = new Intent(getApplicationContext(), GuardianActivity.class);
                intent.putExtra("title","Edit Guardian");
                intent.putExtra("option",2);
                break;

                default:
                    finish();
        }
        startActivity(intent);
    }
    private void exitApp(){

        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);

    }


}
