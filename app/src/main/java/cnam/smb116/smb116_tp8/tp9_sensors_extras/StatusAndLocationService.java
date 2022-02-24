package cnam.smb116.smb116_tp8.tp9_sensors_extras;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;

public class StatusAndLocationService extends Service  {
    private static final String TAG = "Status&LocationService";

    private final IBinder binder = new LocalBinder();
    private Handler handler = new Handler();
    private Runnable runnable;
    private Intent mIntent;
    private String number;
    private Messenger messenger;
    private BroadcastReceiver batteryInfoReceiver;
    private LocationManager locationManager;

    public class LocalBinder extends Binder {
        StatusAndLocationService getService() {
            return StatusAndLocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        removeHandler();
        return super.onUnbind(intent);
    }

    public void removeHandler(){
        handler.removeCallbacks(runnable);
        Log.i(TAG, "handler removed");
    }

    public void getStatus(String number, Messenger messenger){
        this.number = number;
        this.messenger = messenger;
        configureRegister();
        loadBatterySection();
        getLocation();
    }

    public void getPeriodicStatus(String number, Messenger messenger, int period){
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, period);
                getStatus(number, messenger);
            }
        }, period);
    }

    private void configureRegister(){
        batteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mIntent = intent;
                String mess = getBatteryLevel();
                SmsManager.getDefault().sendTextMessage(number, null, mess, null, null);
                Log.i(TAG,mess );
                context.unregisterReceiver(batteryInfoReceiver);
            }
        };
    }

    private void loadBatterySection() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryInfoReceiver, intentFilter);
    }

    public String getBatteryLevel(){
        String batteryString = "";

        if (mIntent != null){
            int level = mIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = mIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                batteryString = ("Level : " + batteryPct + " %");
            }
        }
        return batteryString;
    }

    @SuppressLint("MissingPermission")
    public void getLocation(){
        if (mIntent != null){
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        String longitude = String.valueOf(location.getLongitude());
                        String latitude = String.valueOf(location.getLatitude());
                        String locationString = "http://www.google.com/maps/place/"+latitude+","+longitude;
                        Log.i(TAG, locationString);
                        SmsManager.getDefault().sendTextMessage(number, null, locationString, null, null);
                    }
                });
            }
            catch(SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}