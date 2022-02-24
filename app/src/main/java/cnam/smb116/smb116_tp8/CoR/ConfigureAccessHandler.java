package cnam.smb116.smb116_tp8.CoR;

import android.content.Context;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.util.Log;

public class ConfigureAccessHandler extends BaseHandler {

    private final String TAG = "ConfigureAccessHandler";
    private Context context;

    public ConfigureAccessHandler(Context context, ChainHandler<String, String, Messenger, String> successor){
        super(context, successor);
        this.context = context;
    }

    public boolean handleRequest(String value, String number, Messenger messenger, String filter){

        String statut = "";
        String[] strTab = value.split("#");

        if( strTab[0].equals("188") && filterOK(filter) ) {

            if ( strTab.length == 3){

                /**Modifier le numéro d' accès*/
                if (readConfiguration("088") == null ) {
                    Log.i(TAG, "Define the password first!");
                    SmsManager.getDefault().sendTextMessage(number, null, "Define the password first!", null, null);
                }else if (readConfiguration("088").equals(strTab[1])){
                    if (writeConfiguration("188", strTab[2])){
                        Log.i(TAG, "OK!");
                        SmsManager.getDefault().sendTextMessage(number, null, "OK!", null, null);
                        sendMessage(messenger, TAG+": "+getDate());
                    }else{
                        Log.i(TAG, "someting wrong! try again");
                        SmsManager.getDefault().sendTextMessage(number, null, "someting wrong! try again", null, null);
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
