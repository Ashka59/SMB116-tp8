package cnam.smb116.smb116_tp8.CoR;

import android.content.Context;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.util.Log;

public class DeleteAccessHandler extends BaseHandler {

    public DeleteAccessHandler(Context context, ChainHandler<String, String, Messenger, String> successor){
        super(context, successor);
    }

    public boolean handleRequest(String value, String number, Messenger messenger, String filter){

        String[] strTab = value.split("#");

        String TAG = "DeleteAccessHandler";
        if( strTab[0].equals("388") && filterOK(filter) ) {

            if ( strTab.length == 2){

                /*Effacement*/
                if (readConfiguration("088") == null){
                    Log.i(TAG, "Define the password first!");
                    SmsManager.getDefault().sendTextMessage(number, null, "Define the password first!", null, null);
                }else if (readConfiguration("088").equals(strTab[1])){
                    if (writeConfiguration("188", "null")){
                        Log.i(TAG, "OK!");
                        SmsManager.getDefault().sendTextMessage(number, null, "OK!", null, null);
                        sendMessage(messenger, TAG +": "+getDate());
                    }else{
                        Log.i(TAG, "something wrong! try again");
                        SmsManager.getDefault().sendTextMessage(number, null, "something wrong! try again", null, null);
                    }
                }else{
                    Log.i(TAG, "wrong password! try again");
                    SmsManager.getDefault().sendTextMessage(number, null, "wrong password! try again", null, null);
                }

            }else{
                Log.i(TAG, "informations manquantes");
                SmsManager.getDefault().sendTextMessage(number, null, "informations manquantes", null, null);
            }
        }else{
            Log.i(TAG, "filter used!");
        }

        return super.handleRequest(value, number, messenger, filter);
    }
}
