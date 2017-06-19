package com.md.projectcodechef;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail,etPassword;
    Button btLogin,btRegister,btReset;
    NotificationCompat.Builder notification;
    private static final int uniqueId = 3123;
    ProgressDialog progressdialog;
    ProgressBar progressbar;

    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setIcon(R.mipmap.sec_logo);
        actionbar.setTitle("  ProjectCodeChef");
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);



        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btRegister = (Button) findViewById(R.id.btRegister);
        btReset = (Button) findViewById(R.id.btReset);

        progressdialog = new ProgressDialog(this);
        progressbar = new ProgressBar(this);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan

            }
        } else {
            // not connected to the internet
            Toast.makeText(this, "Internt not connected !", Toast.LENGTH_SHORT).show();
        }

        firebaseauth = FirebaseAuth.getInstance();
        if(firebaseauth.getCurrentUser()!=null)
        {
            finish();
            Intent intent = new Intent(LoginActivity.this,HomePageActivity.class);
            startActivity(intent);
        }

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);




        String email = getIntent().getStringExtra("email");
        if(email !=null) {

            Toast.makeText(LoginActivity.this, email + " has been registered", Toast.LENGTH_SHORT).show();
        }

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.equals("")||email.trim().length()==0||
                        password.equals("")||password.trim().length()==0)
                {
                    Toast.makeText(LoginActivity.this, "Enter the credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressdialog.setMessage("Logging in please wait...");
                progressdialog.show();

                firebaseauth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               progressdialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(),HomePageActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Email/password is wrong !", Toast.LENGTH_SHORT).show();
                                    notification.setSmallIcon(R.mipmap.sec_logo)
                                            .setVibrate(new long[]{1000,1000,1000,1000,1000})
                                            .setContentTitle("Register")
                                            .setContentText("Click here to register first !")
                                            .setTicker("Register first !");

                                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                                    PendingIntent pendingintent = PendingIntent.getActivity(LoginActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                    notification.setContentIntent(pendingintent);

                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    nm.notify(uniqueId,notification.build());

                                }
                            }
                        });
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                if(email.equals("")||email.trim().length()==0||email.equals(null))
                {
                    Toast.makeText(LoginActivity.this, "Enter the valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                firebaseauth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(LoginActivity.this, "Password reset mail sent, please check your email", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Failed to send the reset email", Toast.LENGTH_SHORT).show();
                                }
                                progressbar.setVisibility(View.GONE);
                            }
                        });


            }
        });
    }
}
