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
import com.example.vaccine_alerter_doctor.data.Const;
import com.example.vaccine_alerter_doctor.interfaces.IdCheckerListener;
import com.example.vaccine_alerter_doctor.interfaces.LoadContentListener;
import com.example.vaccine_alerter_doctor.interfaces.MultiSpinnerListener;
import com.example.vaccine_alerter_doctor.interfaces.UploadContentListener;
import com.example.vaccine_alerter_doctor.network.Mtandao;
import com.example.vaccine_alerter_doctor.network.NetWorker;
import com.example.vaccine_alerter_doctor.util.MultiSpinner;
import com.example.vaccine_alerter_doctor.util.SoftKeyBoard;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChildActivity extends AppCompatActivity implements LoadContentListener, IdCheckerListener, UploadContentListener {

    private EditText guard_id,
            f_name,
            l_name,
            d_o_b;
    private String id,
            fName,
            lName,
            dOB,
            gender;

    private TextView message;
    private List<Integer> adminVaccineIndex = new ArrayList<>();
    private String vaccines = "";
    private Button add_btn;
    private View rootView;
    private SpinKitView spinKitView;
    private DatePickerDialog picker;
    private Spinner gender_spin;
    private int action;
    private SpinKitView dialogSpinKitView;
    private AlertDialog alertDialog;
    private MultiSpinner simpleSpinner;
    private Toolbar toolbar;
    private String[] genderList = {
            "Select Gender",
            "Male",
            "Female"
    };

    private String selectedVaccines = "";
    private String childId = "";
    private String guardianId = "";
    private LinkedHashMap<String, Boolean> linkedVaccine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIncomingIntent(savedInstanceState);
        setContentView(R.layout.activity_child);
        setUIConfig();

        if (action == 2 || action == 3) {
            showChildIdDialog();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(action == 2 || action == 3){
            getMenuInflater().inflate(R.menu.menu_user, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;

            case R.id.delete_user:
                showConfirmationMessage(3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUIConfig() {

        rootView = (View) findViewById(R.id.view_activity_add_child);
        guard_id = (EditText) findViewById(R.id.textChildGId);
        f_name = (EditText) findViewById(R.id.textChildFName);
        l_name = (EditText) findViewById(R.id.textChildLName);
        d_o_b = (EditText) findViewById(R.id.textChildDOB);
        gender_spin = (Spinner) findViewById(R.id.child_gender);
        add_btn = (Button) findViewById(R.id.add_child_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar_child);
        simpleSpinner = (MultiSpinner) findViewById(R.id.child_vaccine_spinner);
        spinKitView = (SpinKitView) findViewById(R.id.child_spin_kit);
        toolbar = (Toolbar) findViewById(R.id.toolbar_child);

        if (action == 1) {
            toolbar.setTitle("Add Child");
            add_btn.setText("Add");

        } else if (action == 2 || action == 3) {
            toolbar.setTitle("Edit Child");
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
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ChildActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                d_o_b.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        linkedVaccine = new LinkedHashMap<>();

        for (int i = 0; i < Const.VACCINE_LIST.length; i++) {

            linkedVaccine.put(Const.VACCINE_LIST[i], false);
        }

        setVaccineSpinner(linkedVaccine);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int operation = 0;
                if (action == 1) {
                    guardianId = guard_id.getText().toString();
                    operation = 1;
                }else if(action == 2 || action == 3){
                    operation = 2;

                }

                fName = f_name.getText().toString();
                lName = l_name.getText().toString();
                dOB = d_o_b.getText().toString();
                gender = gender_spin.getSelectedItem().toString();

                if (fName.isEmpty() || fName.length() == 0 || fName == null || lName.isEmpty() || lName.length() == 0 || lName == null) {

                    showSnackBar("Invalid names");

                } else if (dOB.isEmpty() || dOB.length() == 0 || dOB == null) {

                    showSnackBar("Enter Date of Birth");

                } else if (action == 1 && (guardianId.isEmpty() || guardianId.length() == 0 || guardianId == null)) {

                    showSnackBar("Invalid Guardian ID");

                } else if (gender.equals("Select Gender")) {

                    showSnackBar("Select the child's gender");

                } else {

                    showConfirmationMessage(operation);

                }
            }
        });


    }

    private void getIncomingIntent(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

                moveToHomeActivity();

            } else {

                action = extras.getInt("option");
                childId = extras.getString("childId");

            }
        } else {

            action = (int) savedInstanceState.getSerializable("option");
            action = (int) savedInstanceState.getSerializable("childId");

        }


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

    private void fetchChildDetails(String id) {

        SoftKeyBoard.hideSoftKeyBoard(ChildActivity.this);
        new NetWorker().loadDetails(ChildActivity.this, 1, id);
    }

    private void displayFetchResults(JSONObject response) {


        try {

            fName = response.getJSONObject("details").getString("fname");
            lName = response.getJSONObject("details").getString("lname");
            int[] vaccines = new int[18];
            f_name.setText(fName);
            l_name.setText(lName);
            d_o_b.setText(response.getJSONObject("details").getString("dob"));
            gender = response.getJSONObject("details").getString("gender");
            childId = String.valueOf(response.getJSONObject("details").getInt("id"));


            if (gender == "Male") {
                gender_spin.setSelection(0);
            } else {
                gender_spin.setSelection(1);
            }

            vaccines[0] = response.getJSONObject("vaccines").getJSONObject("OPV1").getInt("administered");
            vaccines[1] = response.getJSONObject("vaccines").getJSONObject("BCG1").getInt("administered");
            vaccines[2] = response.getJSONObject("vaccines").getJSONObject("HEPB1").getInt("administered");
            vaccines[3] = response.getJSONObject("vaccines").getJSONObject("DPT1").getInt("administered");
            vaccines[4] = response.getJSONObject("vaccines").getJSONObject("HIBB1").getInt("administered");
            vaccines[5] = response.getJSONObject("vaccines").getJSONObject("HEPB2").getInt("administered");
            vaccines[6] = response.getJSONObject("vaccines").getJSONObject("OPV2").getInt("administered");
            vaccines[7] = response.getJSONObject("vaccines").getJSONObject("PNEU").getInt("administered");
            vaccines[8] = response.getJSONObject("vaccines").getJSONObject("ROTA1").getInt("administered");
            vaccines[9] = response.getJSONObject("vaccines").getJSONObject("DPT2").getInt("administered");
            vaccines[10] = response.getJSONObject("vaccines").getJSONObject("HIBB2").getInt("administered");
            vaccines[11] = response.getJSONObject("vaccines").getJSONObject("HEPB3").getInt("administered");
            vaccines[12] = response.getJSONObject("vaccines").getJSONObject("OPV3").getInt("administered");
            vaccines[13] = response.getJSONObject("vaccines").getJSONObject("VITA1").getInt("administered");
            vaccines[14] = response.getJSONObject("vaccines").getJSONObject("VOTA2").getInt("administered");
            vaccines[15] = response.getJSONObject("vaccines").getJSONObject("VITA2").getInt("administered");
            vaccines[16] = response.getJSONObject("vaccines").getJSONObject("MEAS").getInt("administered");
            vaccines[17] = response.getJSONObject("vaccines").getJSONObject("YELLOW").getInt("administered");

            linkedVaccine.clear();
            List<String> vaccineList = new ArrayList<String>();
            for (int i = 0; i < 18; i++) {

                if (vaccines[i] == 1) {

                    linkedVaccine.put(Const.VACCINE_LIST[i], true);
                    vaccineList.add(Const.VACCINE_LIST[i]);
                    adminVaccineIndex.add(i);

                } else if (vaccines[i] == 0) {

                    linkedVaccine.put(Const.VACCINE_LIST[i], false);
                }
            }

            simpleSpinner.displayItems(vaccineList);
            setVaccineSpinner(linkedVaccine);


        } catch (JSONException jsonE) {

            showSnackBar("Something went wrong! Try again later");
            afterSnackBarAction(2);


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

    private void uploadChildToServer(String id, String fName, String lName, String dOB, String gender, String vaccines) {
        add_btn.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);
        SoftKeyBoard.hideSoftKeyBoard(ChildActivity.this);
        if (action == 1)
            new NetWorker().uploadChild(ChildActivity.this, 1, guardianId, fName, lName, dOB, gender, vaccines);
        if (action == 2 || action == 3)
            new NetWorker().uploadChild(ChildActivity.this, 2, childId , fName, lName, dOB, gender, vaccines);
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
                afterSnackBarAction(1);
                break;
            case 3:
                responseAlerter(2, msg);
                break;

            default:
                responseAlerter(type, msg);
                break;

        }

    }

    private void responseAlerter(int type, String msg) {

        if (type == 0) {
            message.setText(msg);
            message.setVisibility(View.VISIBLE);
        } else if (type == 1)
            showSnackBar(msg);
        else if (type == 2) {
            showSnackBar(msg);
            afterSnackBarAction(1);
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

        dialogSpinKitView = dialogView.findViewById(R.id.dialog_child_spin_kit);
        Button cancelButton = (Button) dialogView.findViewById(R.id.child_dialog_cancel);
        Button proceedButton = (Button) dialogView.findViewById(R.id.child_dialog_proceed);
        final EditText editText = (EditText) dialogView.findViewById(R.id.text_child_id);

        if (action == 3) {
            editText.setText(childId);
        }
        message = (TextView) dialogView.findViewById(R.id.text_child_id_alert);
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

                    message.setText("Invalid Child ID");
                    message.setVisibility(View.VISIBLE);

                } else if (!Mtandao.checkInternet(getApplicationContext())) {

                    showSnackBar("Check Internet Connection and try again");

                } else {

                    dialogSpinKitView.setVisibility(View.VISIBLE);
                    SoftKeyBoard.hideSoftKeyBoard(ChildActivity.this);
                    fetchChildDetails(id);

                }
            }

        });
    }

    private void goToPreviousActivity() {

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    @Override
    public void onLoadValidResponse(JSONObject response) {
        showSnackBar("Your request has been accepted");
        afterSnackBarAction(2);

    }

    @Override
    public void onLoadErrorResponse(Pair response) {
        onLoadWaitComplete();
        errorChecker(1, response);
    }

    private void onLoadWaitComplete() {
        if (action == 2 || action == 3) {
            dialogSpinKitView.setVisibility(View.GONE);


        }

        spinKitView.setVisibility(View.GONE);
        add_btn.setVisibility(View.VISIBLE);


    }

    private void setVaccineSpinner(LinkedHashMap vaccines) {


        simpleSpinner.setItems(vaccines, new MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {

                selectedVaccines = "";
                List<Integer> positions = new ArrayList<>();


                for (int i = 0; i < selected.length; i++) {

                    if (selected[i]) {
                        positions.add(i);

                    }
                }

                if (action == 2 || action == 3) {

                    if (!(positions.size() == 0)) {

                        for (int x : adminVaccineIndex) {

                            positions.remove(Integer.valueOf(x));
                        }

                        int count = 0;

                        for (int x : positions) {

                            if (count == 0)
                                selectedVaccines = selectedVaccines.concat(Const.VACCINE_LIST[x]);
                            else
                                selectedVaccines = selectedVaccines.concat("#" + Const.VACCINE_LIST[x]);

                            count++;

                        }

                    }

                }
            }
        });
    }

    private void afterSnackBarAction(final int operation) {

        new CountDownTimer(1000, 2000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                if (operation == 1)
                    startActivity(new Intent(ChildActivity.this, LoginActivity.class));
                else
                    finish();
            }

        }.start();
    }

    @Override

    protected void onStart() {
        SoftKeyBoard.hideSoftKeyBoard(ChildActivity.this);
        super.onStart();
    }

    private void showConfirmationMessage(final int operation) {

        String msg  = "";

        switch (operation){
            case 1:
                msg = "Are you want to add Child?";
                break;
            case 3:
                msg = "Are you want to delete Child?";
                break;
            case 2:
                msg = "Are you want to update Child?";
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

                        if(operation == 1 || operation ==2){

                            uploadChildToServer(id , fName, lName, dOB, gender, vaccines);
                        }else if(operation == 3){

                            new NetWorker().deleteUser(ChildActivity.this, 1, childId);
                        }
                    }

                }).show();

    }
    private String getResponseMessage(JSONObject response){

       String msg = "";

        try {

            msg =  response.getString("message");
        }catch (JSONException jsonE){

            moveToHomeActivity();
        }

        return msg;
    }
}