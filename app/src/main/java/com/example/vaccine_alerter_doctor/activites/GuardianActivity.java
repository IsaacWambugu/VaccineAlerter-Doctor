package com.example.vaccine_alerter_doctor.activites;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.interfaces.IdCheckerListener;
import com.example.vaccine_alerter_doctor.interfaces.LoadContentListener;
import com.example.vaccine_alerter_doctor.interfaces.UploadContentListener;
import com.example.vaccine_alerter_doctor.network.Mtandao;
import com.example.vaccine_alerter_doctor.network.NetWorker;
import com.example.vaccine_alerter_doctor.util.SoftKeyBoard;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GuardianActivity extends AppCompatActivity implements LoadContentListener, IdCheckerListener, UploadContentListener {

    private EditText guard_id,
            f_name,
            l_name,
            d_o_b,
            phone_no;
    private String id,
            fName,
            lName,
            phone,
            dOB,
            gender;

    private TextView message;
    private Button add_btn;
    private View rootView;
    private DatePickerDialog picker;
    private Spinner gender_spin;
    private SpinKitView spinKitView;
    private int action;
    private SpinKitView dialogSpinKitView;
    private AlertDialog alertDialog;
    private Toolbar toolbar;
    private String[] genderList = {
            "Select Gender",
            "Male",
            "Female"
    };
    private String guardianId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);
        getIncomingIntent(savedInstanceState);
        setUIConfig();

        if (action == 2) {
            showGuardianIdDialog();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (action == 2) {
            getMenuInflater().inflate(R.menu.menu_user, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.delete_user:
                showConfirmationMessage(3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getIncomingIntent(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

                moveToHomeActivity();

            } else {

                action = extras.getInt("option");

            }
        } else {

            action = (int) savedInstanceState.getSerializable("option");

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
        toolbar = (Toolbar) findViewById(R.id.toolbar_guardian);

        if (action == 1) {
            toolbar.setTitle("Add Guardian");
            add_btn.setText("Add");

        } else if (action == 2) {
            toolbar.setTitle("Edit Guardian");
            add_btn.setText("Update");
            guard_id.setVisibility(View.GONE);

        } else {

            moveToHomeActivity();
        }


        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_back);

        } catch (NullPointerException npE) {

            finish();
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, genderList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        gender_spin.setAdapter(spinnerArrayAdapter);

        gender_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if (position > 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        d_o_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
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


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = "";
                // SoftKeyBoard.hideSoftKeyBoard(GuardianActivity.this);
                int operation = 0;
                if (action == 1) {
                    id = guard_id.getText().toString();
                    operation = 1;
                }else if (action == 2) {
                    id = guardianId;
                    operation = 2;
                }

                fName = f_name.getText().toString().trim();
                lName = l_name.getText().toString().trim();
                phone = phone_no.getText().toString().trim();
                dOB = d_o_b.getText().toString().trim();
                gender = gender_spin.getSelectedItem().toString();

                if (fName.isEmpty() || fName.length() == 0 || fName == null || lName.isEmpty() || lName.length() == 0 || lName == null) {

                    showSnackBar("Invalid names");

                } else if (dOB.isEmpty() || dOB.length() == 0 || dOB == null) {

                    showSnackBar("Enter Date of Birth");

                } else if (id.isEmpty() || id.length() == 0 || id == null) {

                    showSnackBar("Invalid Guardian ID");

                } else if (gender.equals("Select Gender")) {

                    showSnackBar("Select the child's gender");

                } else if (phone.isEmpty() || phone.length() < 9 || phone == null || phone.length() > 10) {

                    showSnackBar("Please provide a valid phone number");

                } else {

                    SoftKeyBoard.hideSoftKeyBoard(GuardianActivity.this);
                    showConfirmationMessage(operation);
                }

            }
        });
    }

    private void showSnackBar(String msg) {
        Snackbar.make(rootView, msg, 2000)
                .setActionTextColor(Color.YELLOW)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

    }

    private void uploadGuardianToServer(String guardianId, String fName, String lName, String phoneNumber, String dOB, String gender) {
        add_btn.setVisibility(View.INVISIBLE);
        spinKitView.setVisibility(View.VISIBLE);
        new NetWorker().uploadGuardian(GuardianActivity.this, action, guardianId, fName, lName, phoneNumber, dOB, gender);

    }

    @Override
    public void onLoadErrorResponse(Pair response) {

        onLoadWaitComplete();
        errorChecker(1, response);

    }

    @Override
    public void onLoadValidResponse(JSONObject response) {
        showSnackBar("Your request has been accepted");
        afterSnackBarAction(2);
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
        message = (TextView) dialogView.findViewById(R.id.text_guardian_id_alert);
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
                dialogSpinKitView.setVisibility(View.GONE);
                message.setVisibility(View.GONE);
                String id = editText.getText().toString();
                if (id.isEmpty() || id.length() == 0 || id == null) {

                    message.setText("Invalid Guardian ID");
                    message.setVisibility(View.VISIBLE);

                } else if (!Mtandao.checkInternet(getApplicationContext())) {

                    showSnackBar("Check Internet Connection and try again");

                } else {

                    dialogSpinKitView.setVisibility(View.VISIBLE);
                    SoftKeyBoard.hideSoftKeyBoard(GuardianActivity.this);
                    fetchGuardianDetails(id);

                }
            }

        });
    }

    private void goToPreviousActivity() {

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void fetchGuardianDetails(String id) {

        new NetWorker().loadDetails(GuardianActivity.this, 2, id);
    }


    private void onLoadWaitComplete() {
        if (action == 2) {
            dialogSpinKitView.setVisibility(View.GONE);

        }
        spinKitView.setVisibility(View.GONE);
        add_btn.setVisibility(View.VISIBLE);

    }

    private void displayFetchResults(JSONObject response) {
        try {

            guardianId = String.valueOf(response.getInt("guardian_id"));
            f_name.setText(response.getJSONObject("details").getString("fname"));
            l_name.setText(response.getJSONObject("details").getString("lname"));
            d_o_b.setText(response.getJSONObject("details").getString("dob"));
            phone_no.setText(response.getJSONObject("details").getString("phone_number"));
            String gender = response.getJSONObject("details").getString("gender");

            if (gender == "Male") {
                gender_spin.setSelection(0);
            } else {
                gender_spin.setSelection(1);
            }

        } catch (JSONException jsonE) {

            showSnackBar("Something went wrong! Try again later");
            afterSnackBarAction(2);
        }

    }

    @Override
    public void onUploadValidResponse(JSONObject response) {

        onLoadWaitComplete();
        showSnackBar(getResponseMessage(response));
        afterSnackBarAction(2);
    }

    @Override
    public void onUploadErrorResponse(Pair response) {

        errorChecker(1, response);
        onLoadWaitComplete();

    }

    private void errorChecker(int type, Pair response) {

        int code = (int) response.first;
        String msg = response.second.toString();

        switch (code) {

            case 2:
                showSnackBar("Please login and try again");
                startActivity(new Intent(GuardianActivity.this, LoginActivity.class));
                break;
            case 3:
                responseAlerter(type, msg);
                break;

            default:
                responseAlerter(type, msg);
                break;

        }
    }

    @Override
    public void onValidResponse(JSONObject response) {
        setUIConfig();
        displayFetchResults(response);
        alertDialog.dismiss();
    }

    public void onErrorResponse(Pair response) {

        errorChecker(0, response);
        onLoadWaitComplete();

    }

    private void responseAlerter(int type, String msg) {

        if (type == 0) {
            message.setText(msg);
            message.setVisibility(View.VISIBLE);
        } else if (type == 1)
            showSnackBar(msg);
        else if (type == 2) {
            showSnackBar(msg);
            moveToHomeActivity();
        }
    }

    private void afterSnackBarAction(final int operation) {

        new CountDownTimer(1000, 2000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                if (operation == 1)
                    startActivity(new Intent(GuardianActivity.this, LoginActivity.class));
                else
                    finish();
            }

        }.start();
    }


    @Override
    protected void onStart() {
        SoftKeyBoard.hideSoftKeyBoard(GuardianActivity.this);
        super.onStart();
    }

    private void showConfirmationMessage(final int operation) {

        String msg = "";

        switch (operation) {
            case 1:
                msg = "Are you want to add guardian?";
                break;
            case 3:
                msg = "Are you want to delete guardian?";
                break;
            case 2:
                msg = "Are you want to update guardian?";
                break;
        }


        new LovelyStandardDialog(this, R.style.TintTheme, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.customGrey)
                .setTopTitleColor(R.color.white)
                .setPositiveButtonColorRes(R.color.black)
                .setTitle("Confirmation")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (operation == 1 || operation == 2) {
                            uploadGuardianToServer(id, fName, lName, phone, dOB, gender);
                        } else if (operation == 3) {
                            new NetWorker().deleteUser(GuardianActivity.this, 2, guardianId);
                        }
                    }

                }).show();

    }

    private String getResponseMessage(JSONObject response) {

        String msg = "";

        try {

            msg = response.getString("message");
        } catch (JSONException jsonE) {

            moveToHomeActivity();
        }

        return msg;
    }
}
