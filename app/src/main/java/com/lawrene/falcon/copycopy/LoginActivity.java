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

public class LoginActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mEmail;
    EditText mPassword;
    Button mLoginBtn;
    FirebaseAuth mFireAuth;
    ProgressDialog mProgressDialog;
    TextView mGotoreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFireAuth = FirebaseAuth.getInstance();
//        mToolbar = (Toolbar)findViewById(R.id.login_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Sign In");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmail = (EditText)findViewById(R.id.email_login);
        mPassword = (EditText)findViewById(R.id.passw_login);
        mLoginBtn =(Button)findViewById(R.id.login_btn);
        mGotoreg = (TextView)findViewById(R.id.go_to_reg);

        mEmail.setText("admin");
        mPassword.setText("admin");

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                logintheuser(email, password);
            }
        });

        mGotoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    public void logintheuser(String email, String password) {
        if(email.equals("admin") && password.equals("admin")){
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        }

        else{
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Please wait");
            mProgressDialog.setMessage("Logging you in");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            mFireAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mProgressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Cant login due to " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
