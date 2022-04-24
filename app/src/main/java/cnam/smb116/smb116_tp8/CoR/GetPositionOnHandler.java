package cnam.smb116.smb116_tp8.CoR;

import android.content.Context;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.util.Log;
import cnam.smb116.smb116_tp8.tp9_sensors_extras.Tp9Sensors;


public class GetPositionOnHandler extends BaseHandler {

    private final Context context;

    public GetPositionOnHandler(Context context, ChainHandler<String, String, Messenger, String> successor){
        super(context, successor);
        this.context = context;
    }

    public boolean handleRequest(String value, String number, Messenger messenger, String filter){

        String[] strTab = value.split("#");

        String TAG = "GetPositionOnHandler";
        if( strTab[0].equals("66") && filterOK(filter) ) {

            if ( strTab.length == 2){ // Ajouter condition: si strTab[1] est un entier cast string

                /*Activation position*/
                String accessNumber = readConfiguration("188");
                if (accessNumber == null){
                    Log.i(TAG, "OK!");
                    /*Question TP8 */
//                SmsManager.getDefault().sendTextMessage(number, null, "OK!", null, null);
                    /*Question TP9 */
                    Tp9Sensors.getInstance(context).bindForPeriodicStatus(number,messenger, Integer.parseInt(strTab[1]));
                    sendMessage(messenger, TAG +": "+getDate());
                }else if (accessNumber.equals(number)
                        || accessNumber.equals("null")){
                    Log.i(TAG, "OK!");
                    /*Question TP8 */
//                SmsManager.getDefault().sendTextMessage(number, null, "OK!", null, null);
                    /*Question TP9 */
                    Tp9Sensors.getInstance(context).bindForPeriodicStatus(number,messenger, Integer.parseInt(strTab[1]));
                    sendMessage(messenger, TAG +": "+getDate());

                }else{
                    Log.i(TAG, "wrong number! try again");
                    SmsManager.getDefault().sendTextMessage(number, null, "wrong number! try again", null, null);
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
