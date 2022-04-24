package cnam.smb116.smb116_tp8;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.telephony.SmsMessage;
import android.util.Log;

import cnam.smb116.smb116_tp8.CoR.*;
import cnam.smb116.smb116_tp8.tp9_sensors_extras.Tp9Sensors;

public class SMSServiceAtBoot extends BroadcastReceiver
{
    private static final String TAG = "SMSServiceAtBoot";
    private final Messenger messenger;
    private final String filter;

    public SMSServiceAtBoot(Context context, Messenger messenger, String filter){
        this.messenger = messenger;
        this.filter = filter;
        /* Question TP9*/
        Tp9Sensors.getInstance(context);
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String value = "";
        String number = "";

        if (bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                number = msgs[i].getOriginatingAddress();
                value = msgs[i].getMessageBody();
            }
            Log.i(TAG, "value = "+value+" number = "+number);

            ChainHandler chainHandler =
                    new PSWHandler(context,
                            new ConfigureAccessHandler(context,
                                    new RequestAccessHandler(context,
                                            new DeleteAccessHandler(context,
                                                    new RequestStatutHandler(context,
                                                            new GetStatutOnHandler(context,
                                                                    new GetStatutOffHandler(context,
                                                                            new GetPositionOnHandler(context,
                                                                                    new GetPositionOffHandler(context, null)))))))));

            chainHandler.handleRequest(value, number, messenger, filter);
        }
    }
}