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

public class GuardianActivity extends AppCompatActivity implements LoadContentListener, IdCheckerListener, UploadContentListener {

    private EditText guard_id, f_name, l_name, d_o_b, phone_no;
    private Button add_btn;
    private View rootView;
    private ProgressBar progressBar;
    private DatePickerDialog picker;
    private Spinner gender_spin;
    private SpinKitView spinKitView;
    private int action;
    private SpinKitView dialogSpinKitView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);
        getIncomingIntent(savedInstanceState);
        setUIConfig();

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

    private void setUIConfig() {

        rootView = (View) findViewById(R.id.view_activity_add_guardian);
        guard_id = (EditText) findViewById(R.id.textGuardianId);
        f_name = (EditText) findViewById(R.id.testGuardianFName);
        l_name = (EditText) findViewById(R.id.testGuardianLName);
        d_o_b = (EditText) findViewById(R.id.textGuardDOB);
        gender_spin = (Spinner) findViewById(R.id.guard_gender);
        add_btn = (Button) findViewById(R.id.add_guard_btn);
        phone_no = (EditText) findViewById(R.id.textGuardianNo);
        spinKitView = (SpinKitView) findViewById(R.id.guardian_spin_kit);

        d_o_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(GuardianActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                d_o_b.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        if (action == 0) {

            add_btn.setText("Add");


        } else if (action == 1) {

            add_btn.setText("Update");
            showGuardianIdDialog();
            //spinKitView.setVisibility(View.VISIBLE);

        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String guardianId = guard_id.getText().toString();
                String fName = f_name.getText().toString().trim();
                String lName = l_name.getText().toString().trim();
                String phone = phone_no.getText().toString().trim();
                String dOB = d_o_b.getText().toString().trim();
                String gender = gender_spin.getSelectedItem().toString();

                if (fName.isEmpty() || fName.length() == 0 || fName == null || lName.isEmpty() || lName.length() == 0 || lName == null) {

                    showSnackBar("Invalid names");

                } else if (dOB.isEmpty() || dOB.length() == 0 || dOB == null) {

                    showSnackBar("Enter Date of Birth");

                } else if (guardianId.isEmpty() || guardianId.length() == 0 || guardianId == null) {

                    showSnackBar("Invalid Guardian ID");

                } else if (gender.isEmpty() || gender.length() == 0 || gender == null) {

                    showSnackBar("Select the child's gender");

                } else if (phone.isEmpty() || phone.length() < 9 || phone == null) {

                    showSnackBar("Please provide a valid phone number");

                } else {

                   uploadGuardianToServer(guardianId, fName, lName, phone, dOB, gender);
                }

            }
        });

    }

    private void showSnackBar(String msg) {

        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

    }

    private void uploadGuardianToServer(String guardianId, String fName, String lName, String phoneNumber, String dOB, String gender) {

        spinKitView.setVisibility(View.VISIBLE);
        new NetWorker().uploadGuardian(GuardianActivity.this, guardianId, fName, lName, phoneNumber, dOB, gender);

    }

    @Override
    public void onLoadErrorResponse(Pair response) {

        spinKitView.setVisibility(View.INVISIBLE);
        errorChecker(response);

    }

    @Override
    public void onLoadValidResponse(JSONObject response) {

        showSnackBar("Guardian has been added!");
        onBackPressed();

    }

    private void moveToHomeActivity() {

        startActivity(new Intent(this, HomeActivity.class));
    }

    private void showGuardianIdDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_guardian_id, null);
        dialogBuilder.setView(dialogView);

        dialogSpinKitView = dialogView.findViewById(R.id.guardian_spin_kit);
        Button cancelButton = (Button) dialogView.findViewById(R.id.guardian_dialog_cancel);
        Button proceedButton = (Button) dialogView.findViewById(R.id.guardian_dialog_proceed);
        final EditText editText = (EditText) dialogView.findViewById(R.id.text_guardian_id);
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

    private void fetchGuardianDetails(String id) {

        new NetWorker().loadGuardianDetails(GuardianActivity.this, id);
    }

    @Override
    public void onValidResponse(JSONObject response) {
        displayFetchResults(response);
        alertDialog.dismiss();
    }

    public void onErrorResponse(Pair response) {

        dialogSpinKitView.setVisibility(View.GONE);
        errorChecker(response);
    }

    private void displayFetchResults(JSONObject response) {
        try {

            guard_id.setText(String.valueOf(response.getInt("guardian_id")));
            f_name.setText(response.getJSONObject("details").getString("fname"));
            l_name.setText(response.getJSONObject("details").getString("lname"));
            d_o_b.setText(response.getJSONObject("details").getString("dob"));
            phone_no.setText(response.getJSONObject("details").getString("phone_number"));
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
                startActivity(new Intent(GuardianActivity.this, LoginActivity.class));
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
}
