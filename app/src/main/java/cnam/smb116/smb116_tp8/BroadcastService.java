package cnam.smb116.smb116_tp8;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class BroadcastService extends Service {
    public BroadcastService() {
    }

    private final IBinder binder = new LocalBinder();

    private static final String TAG = "BroadcastService";
    private Messenger messenger;
    private String filter;
    private SMSServiceAtBoot smsReceiver;
    private Intent intent;

    public class LocalBinder extends Binder {
        BroadcastService getService() {
            return BroadcastService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        /* Question 1 */
//        startSMSReceiver();
//        return binder;
        /* Question 2 */
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        /* Question 1 */
//        stopSMSReceiver();
        return super.onUnbind(intent);
    }

    public void startSMSReceiver(){
        Log.i(TAG, "startSMSReceiver");

        if (intent != null) {
            this.filter = intent.getStringExtra("filter");
            this.messenger = (Messenger) intent.getExtras().get("messager");
            smsReceiver = new SMSServiceAtBoot(getApplicationContext(), messenger, filter);

            registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }

    public void stopSMSReceiver(){
        Log.i(TAG, "stopSM  SReceiver");
        unregisterReceiver(smsReceiver);
    }

    /** Question 2 */
    IBroadcastService.Stub mBinder = new IBroadcastService.Stub() {
        @Override
        public void startAIDLSMSReceiver() {
            Log.i(TAG, "startAIDLSMSReceiver");

            if (intent != null) {
                filter = intent.getStringExtra("filter");
                messenger = (Messenger) intent.getExtras().get("messager");
                smsReceiver = new SMSServiceAtBoot(getApplicationContext(), messenger, filter);

                registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
            }
        }

        @Override
        public void stopAIDLSMSReceiver() {
            Log.i(TAG, "stopAIDLSMSReceiver");
            unregisterReceiver(smsReceiver);
        }
    };
}


