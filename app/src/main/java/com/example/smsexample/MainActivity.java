package com.example.smsexample;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etMessage;
    EditText etTelNr;
    int MY_PERMISSION_REQUEST_SEND_SMS=1;

    String SENT="SEND_SMS";
    String DELIVERED="DELIVERED_SMS";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage=findViewById(R.id.etMessage);
        etTelNr=findViewById(R.id.etTelNr);

        sentPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);


    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {

                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS SENT",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this,"GENERIC FAILURE",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this,"NO_SERVICE",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this,"NULL_PDU",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this,"RADIO_OFF",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        smsDeliveredReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS Delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this,"SMS NOT DELIVERED",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver,new IntentFilter(DELIVERED));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);
    }

    public void btn_sendSMS_onClick(View v){

        String message=etMessage.getText().toString();
        String telNr=etTelNr.getText().toString();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SEND_SMS);

        }
        else {
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(telNr,null,message,sentPI,deliveredPI);
        }


    }
}
