package com.md.projectcodechef;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {

    EditText etMessage;
    EditText etNumber;
    int MY_PERMISSIONS_REQUEST_SEND_SMS =1;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver sentReceiver,deliveredReceiver;
    FirebaseAuth firebaseAuth;
    TextView tvLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.sec_logo);
        actionBar.setTitle("  Home");

        etMessage = (EditText) findViewById(R.id.etMessage);
        etNumber = (EditText) findViewById(R.id.etNumber);
        tvLogOut = (TextView) findViewById(R.id.tvLogOut);

        sentPI = PendingIntent.getBroadcast(this,0, new Intent(SENT),0);
        deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
        }

    }

    public void setTvLogOut(View v)
    {
        firebaseAuth.signOut();
        startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        sentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(HomePageActivity.this, "SMS SENT SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(HomePageActivity.this, "GENERIC FAILURE!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(HomePageActivity.this, "NO SERVICE!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(HomePageActivity.this, "NULL PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(HomePageActivity.this, "RADIO OFF!", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };

        deliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(HomePageActivity.this, "SMS DELIVERED!", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(HomePageActivity.this, "SMS NOT DELIVERED!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        registerReceiver(sentReceiver, new IntentFilter(SENT));
        registerReceiver(deliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(deliveredReceiver);
        unregisterReceiver(sentReceiver);

    }

    public void btSend(View v)
    {
        String message = etMessage.getText().toString();
        String number = etNumber.getText().toString();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number,null,message,sentPI,deliveredPI);
        }
    }

    public void sentToAll(View v)
    {
        String message = etMessage.getText().toString();
        String[] number = {"8870855940","8823949403","7530005573","9920820171","8797894230","9899967771","7585955911","7530009227","8220247204",
        "9629219208","8790238429"};

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
            !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            for(int i=0;i<number.length;i++)
            {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number[i],null,message,sentPI,deliveredPI);
            }
        }

    }


}


