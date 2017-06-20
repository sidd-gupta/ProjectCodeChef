package com.md.projectcodechef;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName,etRegEmail,etRegPassword,etRegRePassword;
    Button btRegister;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.sec_logo);
        actionBar.setTitle("  Registration");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        etFullName = (EditText) findViewById(R.id.etFullName);
        etRegPassword = (EditText) findViewById(R.id.etRegPassword);
        etRegEmail = (EditText) findViewById(R.id.etRegEmail);
        etRegRePassword = (EditText) findViewById(R.id.etRegRePassword);
        btRegister = (Button) findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String regEmail = etRegEmail.getText().toString().trim();
                String regFullname = etFullName.getText().toString().trim();
                String regPassword = etRegPassword.getText().toString().trim();
                String regRetypePswrd = etRegRePassword.getText().toString().trim();

                if(regEmail.equals("")||regEmail.trim().length()==0||
                        regFullname.equals("")||regFullname.trim().length()==0
                        ||regPassword.equals("")||regPassword.trim().length()==0||
                        regRetypePswrd.equals("")||regRetypePswrd.trim().length()==0){

                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

                }


                else{
                    if(regPassword != regRetypePswrd){
                        Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                    else {


                        //Firebase Authentication
                        progressDialog.setMessage("Registering User...");
                        progressDialog.show();

                        firebaseAuth.createUserWithEmailAndPassword(regEmail,regPassword)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            intent.putExtra("email",regEmail);
                                            startActivity(intent);
                                        }

                                    }
                                });
                    }

                }

            }
        });


    }
}
