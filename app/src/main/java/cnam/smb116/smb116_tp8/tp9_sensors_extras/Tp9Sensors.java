package cnam.smb116.smb116_tp8.tp9_sensors_extras;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

public class Tp9Sensors{

    private final Context context;
    private StatusAndLocationService mService;
    @SuppressLint("StaticFieldLeak")
    private static Tp9Sensors instance;
    private boolean mBound = false;

    public static Tp9Sensors getInstance(Context context){
        if (instance == null) {
            instance = new Tp9Sensors(context);
        }
        return instance;
    }

    private Tp9Sensors(Context context){
        this.context = context;
        Intent intent = new Intent(context, StatusAndLocationService.class);
        context.bindService(intent, connection2, Context.BIND_AUTO_CREATE);
    }

    public void getStatus(String number, Messenger messenger){
        if (mBound){
            mService.getStatus(number, messenger);
        }
    }

    public void bindForPeriodicStatus(String number, Messenger messenger, int period){
        if (mBound){
            mService.getPeriodicStatus(number, messenger, period);
        }
    }

    public void unBindForPeriodicStatus(){
        if (mBound){
            mService.removeHandler();
        }
    }

    public void stopService(){
        context.unbindService(connection2);
        mBound = false;
    }

    private final ServiceConnection connection2 = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            StatusAndLocationService.LocalBinder binder = (StatusAndLocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
