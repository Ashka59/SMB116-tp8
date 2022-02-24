package cnam.smb116.smb116_tp8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import cnam.smb116.smb116_tp8.CoR.ChainHandler;
import cnam.smb116.smb116_tp8.CoR.ConfigureAccessHandler;
import cnam.smb116.smb116_tp8.CoR.DeleteAccessHandler;
import cnam.smb116.smb116_tp8.CoR.GetPositionOffHandler;
import cnam.smb116.smb116_tp8.CoR.GetPositionOnHandler;
import cnam.smb116.smb116_tp8.CoR.GetStatutOffHandler;
import cnam.smb116.smb116_tp8.CoR.GetStatutOnHandler;
import cnam.smb116.smb116_tp8.CoR.PSWHandler;
import cnam.smb116.smb116_tp8.CoR.RequestAccessHandler;
import cnam.smb116.smb116_tp8.CoR.RequestStatutHandler;
import cnam.smb116.smb116_tp8.tp9_sensors_extras.Tp9Sensors;

public class SMSReceiver extends BroadcastReceiver
{
    private static final String TAG = "SMSReceiver";
    private Context context;
    private Messenger messenger;
    private String filter;

    public SMSReceiver(Context context, Messenger messenger, String filter){
        this.context = context;
        this.messenger = messenger;
        this.filter = filter;
        /** Question TP9*/
        Tp9Sensors.getInstance(context);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String value = "";
        String number = "";

        if (bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                number = msgs[i].getOriginatingAddress();
                value = msgs[i].getMessageBody().toString();
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