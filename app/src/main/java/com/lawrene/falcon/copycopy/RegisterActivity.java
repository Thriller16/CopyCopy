package com.lawrene.falcon.copycopy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    //---------------------All arrays-----------------------------//
    String [] allschools = {"University of Uyo", "University of Port Harcourt", "University of Lagos"};
    String [] allfaculties = {"Engineering", "Science", "Medicine", "Law"};
    String [] alldepartments = {};
    String [] alldepartmentsEng = {"Mechanical", "Electrical", "Computer", "Chemical"};
    String [] alldepartmentsSci = {"Physics", "Biochemistry", "Anatomy", "Maths and Stats"};
    String [] alllevels = {};
    String [] alllevelsEng = {"100", "200", "300", "400", "500"};
    String [] alllevelsSci = {"100", "200", "300", "400"};


//    ---------------------Declaration of the views--------------------
    Spinner mSchoolSpinner;
    Spinner mFacultySpinner;
    Spinner mDepartmentSpinner;
    Spinner mLevelSpinner;
    TextView mGotologin;
    ProgressDialog mProgressDialog;
    Toolbar mToolbar;
    FirebaseUser mCurrentUser;
//    DatabaseReference mSchoolsDatabase;
//    DatabaseReference mFacultiesDatabase;
//    DatabaseReference mDepartmentsDatabase;
//    DatabaseReference mLevelsDatabase;
    DatabaseReference mUserDatabase;

    String mEmail, mPassword, mSchool, mFaculty, mDepartment, mlevel;
    Button mRegBtn;
    FirebaseAuth mFireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //-------------------------Setting up  firebase---------------------
        mFireAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFireAuth.getCurrentUser();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //-----------------------------Setting up the views-----------------------
        final EditText EmailEdt = (EditText)findViewById(R.id.email_reg);
        final EditText PasswordEdt = (EditText)findViewById(R.id.passw_reg);
        mRegBtn = (Button)findViewById(R.id.create_account_btn);
        mGotologin = (TextView)findViewById(R.id.go_to_login);
        mSchoolSpinner = (Spinner)findViewById(R.id.schoolspinner);
        mFacultySpinner = (Spinner)findViewById(R.id.facultyspinner);
        mDepartmentSpinner = (Spinner)findViewById(R.id.dept_spinner);
        mLevelSpinner = (Spinner)findViewById(R.id.level_spinner);



        //---------------------Setting up the arrayadapter to use for the spinners------------------
        ArrayAdapter school_array_adapter = new ArrayAdapter(this,R.layout.spinner_look, allschools);
        school_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSchoolSpinner.setAdapter(school_array_adapter);

        ArrayAdapter faculty_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allfaculties);
        faculty_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFacultySpinner.setAdapter(faculty_array_adapter);

        ArrayAdapter dept_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alldepartments);
        dept_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartmentSpinner.setAdapter(dept_array_adapter);

        ArrayAdapter level_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alllevels);
        level_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelSpinner.setAdapter(level_array_adapter);


//        -----------------------Onclick listener for the views------------------
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmail = EmailEdt.getText().toString();
                mPassword = PasswordEdt.getText().toString();
                registernewuser(mEmail , mPassword, mSchool, mFaculty, mDepartment, mlevel);
            }
        });

        mGotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mSchool = allschools[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mFaculty = allfaculties[position];
                switch (mFaculty) {
                    case "Engineering":
                        alldepartments = alldepartmentsEng;
                        alllevels = alllevelsEng;

                        ArrayAdapter eng_depts_adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, alldepartments);
                        eng_depts_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mDepartmentSpinner.setAdapter(eng_depts_adapter);

                        ArrayAdapter eng_levels_adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, alllevels);
                        eng_levels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mLevelSpinner.setAdapter(eng_levels_adapter);
                        break;


                    case "Science":
                        alldepartments = alldepartmentsSci;
                        alllevels = alllevelsSci;

                        ArrayAdapter science_adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, alldepartments);
                        science_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mDepartmentSpinner.setAdapter(science_adapter);

                        ArrayAdapter sci_levels_adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, alllevels);
                        sci_levels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mLevelSpinner.setAdapter(sci_levels_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(getApplicationContext(), allschools[position], Toast.LENGTH_LONG).show();
                mDepartment = alldepartments[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(getApplicationContext(), allschools[position], Toast.LENGTH_LONG).show();
                mlevel = alllevels[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void registernewuser(String email, String password, final String school, final String faculty, final String department, final String level) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Creating your account");
        mProgressDialog.show();

        //---------------------------First step authentication--------------------------
        mFireAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("device_token", deviceToken);
                    userMap.put("can_upload","true");
                    userMap.put("earnings", "0");
                    userMap.put("school", school);
                    userMap.put("faculty", faculty);
                    userMap.put("department", department);
                    userMap.put("level", level);

                    FirebaseUser currentuser = mFireAuth.getCurrentUser();

                    //------------------------Second step pushing uservalues to db---------------------------
                    mUserDatabase.child(currentuser.getUid()).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                            }else{
                                Toast.makeText(RegisterActivity.this, "Error during registration", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    mProgressDialog.dismiss();
                }
            }
        });
    }
}
