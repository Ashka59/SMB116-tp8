package cnam.smb116.smb116_tp8.CoR;

import android.content.Context;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.util.Log;

public class GetStatutOffHandler extends BaseHandler {

    public GetStatutOffHandler(Context context, ChainHandler<String, String, Messenger, String> successor){
        super(context, successor);
    }

    public boolean handleRequest(String value, String number, Messenger messenger, String filter){

        String[] strTab = value.split("#");

        String TAG = "GetStatutOffHandler";
        if( strTab[0].equals("044") && filterOK(filter) ) {

            /*Désactivation statut*/
            String accessNumber = readConfiguration("188");
            if (accessNumber == null){
                Log.i(TAG, "OK!");
                SmsManager.getDefault().sendTextMessage(number, null, "OK!", null, null);
                sendMessage(messenger, TAG +": "+getDate());
            }else if (accessNumber.equals(number)
                    || accessNumber.equals("null")){
                Log.i(TAG, "OK!");
                SmsManager.getDefault().sendTextMessage(number, null, "OK!", null, null);
                sendMessage(messenger, TAG +": "+getDate());

            }else{
                Log.i(TAG, "wrong number! try again");
                SmsManager.getDefault().sendTextMessage(number, null, "wrong number! try again", null, null);
            }
        }else{
            Log.i(TAG, "filter used!");
        }

        return super.handleRequest(value, number, messenger, filter);
    }
}

