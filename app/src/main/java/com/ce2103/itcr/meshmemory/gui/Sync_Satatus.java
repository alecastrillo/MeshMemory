package com.ce2103.itcr.meshmemory.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import com.ce2103.itcr.meshmemory.*;
import android.telephony.SmsManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.widget.Toast;


public class Sync_Satatus extends AppCompatActivity {
    public static IntentFilter intentfilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync__satatus);
        intentfilter = new IntentFilter();  //este mae esta declarado arriba porque se ocupa que sea "global"
        intentfilter.addAction("SMS_RECEIVED_ACTION");


    }
    private BroadcastReceiver intentreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //displaymsg
            String str = intent.getExtras().getString("sms");
            /*if (str.contains("Add")){
                String[] strarr = str.split(",");
                addItem(strarr[1],strarr[2],strarr[3],strarr[4]);
            }
            if (str.contains("Remove")){
                deleteItem(pos);
            }
            if (str.contains("Burp")){
                burp();
            }*/
        }
    };
    private void sendMsg(String num, String msg) {

        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";


        PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

        /////////////////////////////////////////////////////////////////////
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),"Synchronizing",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),"Generic Failure",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(),"No Service",Toast.LENGTH_LONG).show();
                        break;
                }

            }
        }, new IntentFilter(SENT));

        //////////////////////////////////////////////////////////////////////////

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),"Sms delivered",Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(),"Sms not delivered",Toast.LENGTH_LONG).show();
                        break;

                }

            }
        }, new IntentFilter(DELIVERED));

        //////////////////////////////////////////////////////////////////////////

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(num,null,msg,sentPI,deliveredPI);
    }
    @Override
    protected void onResume (){
        //register the receiver
        registerReceiver(intentreceiver,intentfilter);
        super.onResume();

    }

    @Override
    protected void onPause () {
        //unregister the receiver
        unregisterReceiver(intentreceiver);
        super.onPause();
    }



}
