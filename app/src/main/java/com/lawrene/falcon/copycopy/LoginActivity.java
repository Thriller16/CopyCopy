package com.lawrene.falcon.copycopy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mEmail;
    EditText mPassword;
    Button mLoginBtn;
    FirebaseAuth mFireAuth;
    ProgressDialog mProgressDialog;
    TextView mGotoreg;
    DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFireAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mEmail = (EditText) findViewById(R.id.email_login);
        mPassword = (EditText) findViewById(R.id.passw_login);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mGotoreg = (TextView) findViewById(R.id.go_to_reg);
    }
}
