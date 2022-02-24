package cnam.smb116.smb116_tp8.CoR;

import android.content.Context;
import android.os.Messenger;
import android.util.Log;

public class TraceHandler extends BaseHandler {

    private final String TAG = "TraceHandler";
    private Context context;

    public TraceHandler(Context context, ChainHandler<String, String, Messenger, String> successor){
        super(context, successor);
        this.context = context;
    }

    public boolean handleRequest(String value, String number, Messenger messenger, String filter){
//        SmsManager.getDefault().sendTextMessage();
        String statut = "";

        if( filterOK(filter) ) {
            if (writeConfiguration("test1", "blabla"))statut = readConfiguration("test1");
            Log.i(TAG, String.valueOf(readConfiguration("bla")));
            Log.i(TAG, getDate() + " handleRequest: " + value+ " statut = "+ statut);
        }
        return super.handleRequest(value, number, messenger, filter);
    }
}
