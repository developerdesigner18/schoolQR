package com.newmysmileQR.Teacher.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.ClassModel;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StudentAddActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    public TextView mTitle;
    public ImageView mIcon;
    public EditText  etFirstName, etDate, etMobile, etEmail, etLastName, etFatherName, etMotherName;
    //Division
    public FrameLayout flSpinner;
    private Button btnSubmit;
    private SpinnerAdapter mAdapter;
    private Spinner spDivision;
    private TextView tvSpinner;

    private Spinner spCountry;
    private SpinnerAdapter mCountryAdapter;
    private String selectCountry;

    //Date
    private Calendar calendar = Calendar.getInstance();
    private int mYear = calendar.get(Calendar.YEAR);
    private int mMonth = calendar.get(Calendar.MONTH);
    private int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    private String selectedDate = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        initToolbar();
        initView();
        btnSubmit.setOnClickListener(this);
        etDate.setOnTouchListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
//        etICNumber = findViewById(R.id.etICNumber);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etDate = findViewById(R.id.etDate);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        spCountry = findViewById(R.id.spCountry);

        ArrayList<String> list = new ArrayList<>();
        list.add("+65");
        list.add("+60");
        list.add("+1");

        mCountryAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spCountry.setAdapter(mCountryAdapter);
        spCountry.setSelection(0);
        selectCountry = spCountry.getSelectedItem() + "";
        selectCountry = selectCountry.replace("+", "");
        Log.d("selectCountry", selectCountry);

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCountry = spCountry.getSelectedItem().toString();
                selectCountry = selectCountry.replace("+", "");
                Log.d("selectCountry", selectCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        etICNumber.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                etICNumber.setError("Last 5 alphanumeric of student IC and year the child is born (eg. 1234H2012)");
//                return false;
//            }
//        });
    }

    private void addStudent() {
        if (Preference.isNetworkAvailable(this)) {
//            String ICNumber = etICNumber.getText().toString();
            String code = Preference.getUser(this).getSchool().getCode();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String fatherName = etFatherName.getText().toString();
            String motherName = etMotherName.getText().toString();
            String birthDate = selectedDate;
            String phone = etMobile.getText().toString();
            String email = etEmail.getText().toString();
            String standardId = Preference.getClassId(this) + "";
            TeacherHome.addSingleStudent(this , code, standardId, email, birthDate, firstName, lastName, fatherName, motherName, phone, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    btnSubmit.setEnabled(true);
                    setResult(RESULT_OK);
                    if (response.isSuccess()) {
                        Toast.makeText(StudentAddActivity.this, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    btnSubmit.setEnabled(true);
                    Toast.makeText(StudentAddActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
//        if (TextUtils.isEmpty(etICNumber.getText())) {
//            Toast.makeText(this, "Enter student reference", Toast.LENGTH_SHORT).show();
//            etICNumber.requestFocus();
//            return false;
//        }
        if (TextUtils.isEmpty(etFirstName.getText())) {
            Toast.makeText(this, "Enter first name", Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etLastName.getText())) {
            Toast.makeText(this, "Enter last name", Toast.LENGTH_SHORT).show();
            etLastName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etFatherName.getText())) {
            Toast.makeText(this, "Enter father name", Toast.LENGTH_SHORT).show();
            etFatherName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etMotherName.getText())) {
            Toast.makeText(this, "Enter mother name", Toast.LENGTH_SHORT).show();
            etMotherName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etDate.getText())) {
            Toast.makeText(this, "Select date of birth", Toast.LENGTH_SHORT).show();
            etDate.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etMobile.getText())) {
            Toast.makeText(this, "Enter mobile", Toast.LENGTH_SHORT).show();
            etMobile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        if (!Preference.isValidEmail(etEmail.getText())) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void initToolbar() {
        Toolbar topToolbar = findViewById(R.id.topToolbar);
        mTitle = topToolbar.findViewById(R.id.mTitle);
        mIcon = topToolbar.findViewById(R.id.mIcon);
        spDivision = topToolbar.findViewById(R.id.spDivision);
        flSpinner = topToolbar.findViewById(R.id.flSpinner);
        tvSpinner = topToolbar.findViewById(R.id.tvSpinner);
        mTitle.setText("Add Student");
        spDivision.setVisibility(View.VISIBLE);
        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (arg1 != null)
                    ((TextView) arg1).setText(null);
                ClassModel classModel = (ClassModel) spDivision.getItemAtPosition(position);
                tvSpinner.setText(classModel.getStandardData().getTitle());
                Preference.setClassId(StudentAddActivity.this, classModel.getStandardId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mIcon.setImageResource(R.drawable.ic_back);
        int padding = (int) getResources().getDimension(R.dimen._8sdp);
        mIcon.setPadding(padding, padding, padding, padding);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getClassList();
    }

    private void getClassList() {
        if (Preference.isNetworkAvailable(this)) {
            String code = Preference.getUser(this).getSchool().getCode();
            String teacherId = Preference.getUser(this).getId() + "";
            TeacherHome.classList(this, code, teacherId, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.isSuccess()) {
                        if (response.getClassList() != null && response.getClassList().size() == 0) {
                            Preference.setClassId(StudentAddActivity.this, -1);
                            tvSpinner.setText("");
                        }
                        mAdapter = new ArrayAdapter<ClassModel>(StudentAddActivity.this, R.layout.support_simple_spinner_dropdown_item, response.getClassList());
                        spDivision.setAdapter(mAdapter);
                    } else {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(StudentAddActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Preference.setClassId(StudentAddActivity.this, -1);
                    tvSpinner.setText("");
                }
            });
        } else {
            Toast.makeText(this, "Check your Network", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            showDateDialog();
        }
        return false;
    }

    private void showDateDialog() {
        DatePickerDialog dialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        mDay = dayOfMonth;
                        selectedDate = String.format(Locale.ENGLISH, "%04d-%02d-%02d", year, mMonth, dayOfMonth);
                        etDate.setText(String.format(Locale.ENGLISH, "%02d-%02d-%04d", dayOfMonth, mMonth, year));
                    }
                }, mYear, mMonth - 1, mDay);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.YEAR, mYear);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                btnSubmit.setEnabled(false);
                if (isValid()) {
                    addStudent();
                } else {
                    btnSubmit.setEnabled(true);
                }
                break;
            }
        }
    }
}
