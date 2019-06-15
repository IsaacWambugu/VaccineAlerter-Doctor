package com.example.vaccine_alerter_doctor.activites;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.adapters.ChildrenAdapter;
import com.example.vaccine_alerter_doctor.data.PreferenceManager;
import com.example.vaccine_alerter_doctor.interfaces.LoadContentListener;
import com.example.vaccine_alerter_doctor.models.ChildModel;
import com.example.vaccine_alerter_doctor.network.NetWorker;
import com.example.vaccine_alerter_doctor.others.DividerItemDecoration;
import com.example.vaccine_alerter_doctor.network.Mtandao;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChildrenListActivity extends AppCompatActivity implements LoadContentListener {

    private Boolean isFABOpen = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private View view;
    private RecyclerView recyclerView;
    private ChildrenAdapter childrenAdapter;
    private ArrayList<ChildModel> childrenList;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUiConfig();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadChildren();

    }

    private void setUiConfig() {

        view = getWindow().getDecorView().getRootView();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_children);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.child_swipe_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Children List");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_back);

        } catch (NullPointerException npE) {

            finish();
        }

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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(childrenAdapter);

    }

    private void loadChildren() {

        swipeRefreshLayout.setRefreshing(true);

        if (Mtandao.checkInternet(getApplicationContext())) {

            new NetWorker().loadChildren(this, String.valueOf(new PreferenceManager(this).getDoctorId()));

        } else {

            swipeRefreshLayout.setRefreshing(false);
            showActionSnackBar("Check internet connection and try again!");

        }
    }

    @Override
    public void onLoadValidResponse(JSONObject response) {

        childrenList.clear();
        extractJSONResponse(response);
        displayChildrenList();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoadErrorResponse(Pair response) {

        swipeRefreshLayout.setRefreshing(false);
        errorChecker(response);

    }

    private void showActionSnackBar(String msg) {

        Snackbar.make(view, msg, 2000)
                .setActionTextColor(Color.YELLOW)
                .setAction(R.string.Retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadChildren();

                    }
                }).show();
    }

    private void showSnackBar(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();

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

        } catch (JSONException jsonE) {


            showWaitSnackBar(2, "Something went wrong .Try again later");
        }

    }

    public void displayChildrenList() {

        runAnimationAgain();

    }

    private void showWaitSnackBar(final int operation, final String msg) {

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

                showSnackBar(msg);
            }

            public void onFinish() {

                if (operation == 1) {

                    startActivity(new Intent(ChildrenListActivity.this, LoginActivity.class));
                    finish();
                } else
                    onBackPressed();


            }
        }.start();
    }

    private void errorChecker(Pair response) {

        int code = (int) response.first;
        String msg = response.second.toString();

        switch (code) {

            case 1:
                showActionSnackBar(msg);
                break;
            case 2:
                showWaitSnackBar(1, "Please login and try again");
                break;
            case 3:
                showSnackBar(msg);
                break;

            default:
                showSnackBar(msg);
                break;

        }

    }
    private void runAnimationAgain() {

        int resId = R.anim.layout_right_to_left;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        childrenAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }
}
