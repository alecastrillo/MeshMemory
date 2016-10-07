package com.ce2103.itcr.meshmemory.server;

import android.telephony.SmsManager;

/**
 * Created by estape11 on 07/10/16.
 */

public class NodeSMS {
    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+506"+phoneNumber, null, message, null, null);
    }
}
