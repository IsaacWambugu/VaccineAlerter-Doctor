package com.example.vaccine_alerter_doctor.activites;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.interfaces.IdCheckerListener;
import com.example.vaccine_alerter_doctor.interfaces.LoadContentListener;
import com.example.vaccine_alerter_doctor.interfaces.UploadContentListener;
import com.example.vaccine_alerter_doctor.network.NetWorker;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChildActivity extends AppCompatActivity implements LoadContentListener,IdCheckerListener, UploadContentListener {

    private TextView guard_id, f_name, l_name, d_o_b;
    private Button add_btn;
    private View rootView;
    private SpinKitView spinKitView;
    private DatePickerDialog picker;
    private Spinner gender_spin;
    private int action;
    private SpinKitView dialogSpinKitView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIncomingIntent(savedInstanceState);
        setContentView(R.layout.activity_child);
        setUIConfig();

    }
    private void setUIConfig(){

        rootView = (View) findViewById(R.id.view_activity_add_child);
        guard_id = (TextView) findViewById(R.id.textChildGId);
        f_name = (TextView) findViewById(R.id.textChildFName);
        l_name = (TextView) findViewById(R.id.textChildLName);
        d_o_b = (TextView) findViewById(R.id.textChildDOB);
        gender_spin = (Spinner) findViewById(R.id.child_gender);
        add_btn = (Button) findViewById(R.id.add_child_btn);
        spinKitView = (SpinKitView) findViewById(R.id.child_spin_kit);

        d_o_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ChildActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                d_o_b.setText(year+"-"+ (monthOfYear + 1)+"-"+ dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String guardianId  = guard_id.getText().toString();
                String fName    = f_name.getText().toString();
                String lName    = l_name.getText().toString();
                String dOB      = d_o_b.getText().toString();
                String gender   = gender_spin.getSelectedItem().toString();

                if (fName.isEmpty() || fName.length() == 0 || fName == null || lName.isEmpty() || lName.length() == 0 || lName == null) {

                    showSnackBar("Invalid names");

                } else if (dOB.isEmpty() || dOB.length() == 0 || dOB == null) {

                    showSnackBar("Enter Date of Birth");

                } else if (guardianId.isEmpty() || guardianId.length() == 0 || guardianId == null) {

                    showSnackBar("Invalid Guardian ID");

                } else if (gender.isEmpty() || gender.length() == 0 || gender == null) {

                    showSnackBar("Select the child's gender");

                } else {

                    uploadToChildToServer(guardianId, fName, lName, dOB, gender);
                }
            }
        });

        if (action == 0) {

            add_btn.setText("Add");
        } else if (action == 1) {

            add_btn.setText("Update");
            showChildIdDialog();
            //spinKitView.setVisibility(View.VISIBLE);

        }

    }
    private void getIncomingIntent(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

                moveToHomeActivity();

            } else {

                action = extras.getInt("action");

            }
        } else {

            action = (int) savedInstanceState.getSerializable("action");

        }

    }
    private void showSnackBar(String msg){

            Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
    }

    private void fetchGuardianDetails(String id) {

        new NetWorker().loadGuardianDetails(ChildActivity.this, id);
    }
    private void displayFetchResults(JSONObject response) {
        try {

            guard_id.setText(String.valueOf(response.getInt("guardian_id")));
            f_name.setText(response.getJSONObject("details").getString("fname"));
            l_name.setText(response.getJSONObject("details").getString("lname"));
            d_o_b.setText(response.getJSONObject("details").getString("dob"));
            String gender = response.getJSONObject("details").getString("gender");

            if(gender  == "Male"){
                gender_spin.setSelection(0);
            }else{
                gender_spin.setSelection(1);
            }

        } catch (JSONException jsonE) {
            showSnackBar("Something went wrong!");
            moveToHomeActivity();
        }
    }

    @Override
    public void onValidResponse(JSONObject response) {
        displayFetchResults(response);
        alertDialog.dismiss();
    }

    public void onErrorResponse(Pair response) {

       // dialogSpinKitView.setVisibility(View.GONE);
        errorChecker(response);
    }

    private void uploadToChildToServer(String guardianId, String fName, String lName, String dOB,String gender){

           spinKitView.setVisibility(View.VISIBLE);
           new NetWorker().uploadChild(ChildActivity.this,guardianId, fName, lName, dOB, gender);

        }
    @Override
    public void onUploadValidResponse(JSONObject response) {
        spinKitView.setVisibility(View.INVISIBLE);
        showSnackBar("Guardian added Successfully");
        // moveToHomeActivity();
    }

    @Override
    public void onUploadErrorResponse(Pair response) {

        errorChecker(response);
        spinKitView.setVisibility(View.INVISIBLE);

    }
    private void errorChecker(Pair response){

        int code = (int) response.first;
        String msg = response.second.toString();

        switch (code){

            case 1:
                showSnackBar(msg);
                break;
            case 2:
                startActivity(new Intent(ChildActivity.this, LoginActivity.class));
                break;
            case 3:
                showSnackBar(msg);
                moveToHomeActivity();
                break;
            case 4:
                showSnackBar(msg);
                break;
            case 5:
                break;

        }

    }
    private void moveToHomeActivity() {

        startActivity(new Intent(this, HomeActivity.class));
    }
    private void showChildIdDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_child_id, null);
        dialogBuilder.setView(dialogView);

        dialogSpinKitView = dialogView.findViewById(R.id.child_spin_kit);
        Button cancelButton = (Button) dialogView.findViewById(R.id.child_dialog_cancel);
        Button proceedButton = (Button) dialogView.findViewById(R.id.child_dialog_proceed);
        final EditText editText = (EditText) dialogView.findViewById(R.id.text_child_id);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                goToPreviousActivity();

            }
        });
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editText.getText().toString();
                dialogSpinKitView.setVisibility(View.VISIBLE);
                fetchGuardianDetails(id);
            }

        });
    }
    private void goToPreviousActivity() {

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    @Override
    public void onLoadValidResponse(JSONObject response) {

        showSnackBar("Guardian has been added!");
        onBackPressed();
    }

    @Override
    public void onLoadErrorResponse(Pair response) {

        spinKitView.setVisibility(View.INVISIBLE);
        errorChecker(response);
    }
}