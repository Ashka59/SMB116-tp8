package cnam.smb116.smb116_tp8;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public final int REQUEST_CODE = 1;
    private static Handler handler;
    private Messenger messager;
    private BroadcastService mService;
    private boolean mBound = false;
    private final ServiceConnection connection = this.configureConnection();

    private LinearLayout containerLayout;
    private TextView infosTxt;

    private String mess ="";

    IBroadcastService iBroadcastService;

    @Override
    protected void onStart() {
        super.onStart();

        /* Question 2 */
        bindBroadcastService();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.containerLayout = findViewById(R.id.container_layout);
        this.infosTxt = findViewById(R.id.infos_txt);
//        new Tp9Sensors(this).bindForPeriodicStatus("",messager, 10000);
        configureHandler();
        checkForPermission();
    }

    public void onClickStart(View view) throws RemoteException {
        /* Question 1 */
//        bindBroadcastService();
        /* Question 2 */
        iBroadcastService.startAIDLSMSReceiver();

        Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Service started");
    }

    public void onClickStop(View view) throws RemoteException {
        /* Question 1 */
//        if (mBound) unbindService(connection);
        /* Question 2 */
        iBroadcastService.stopAIDLSMSReceiver();

        Toast.makeText(getApplicationContext(),"Service stopped",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Service stopped");
        mBound = false;
    }

    public void bindBroadcastService(){
        Intent intent = new Intent(this,BroadcastService.class);
        messager = new Messenger(handler);
        intent.putExtra("messager", messager);
        String filter = "PSWHandler , " +
                "ConfigureAccessHandler , " +
                "RequestAccessHandler , " +
                "DeleteAccessHandler , " +
                "RequestStatutHandler , " +
                "GetStatutOnHandler , " +
                "GetStatutOffHandler , " +
                "GetPositionOnHandler , " +
                "GetPositionOffHandler ";
        intent.putExtra("filter", filter);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkForPermission(){
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.RECEIVE_SMS) ==
                PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
        {
            this.containerLayout.setVisibility(View.VISIBLE);
        } else {
            requestPermissions(new String[] { Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.containerLayout.setVisibility(View.VISIBLE);
            } else {
                Log.i(TAG, "Permission denied");
            }
        }
    }

    @SuppressLint("HandlerLeak")
    public void configureHandler(){
        handler = new Handler() {
            public void handleMessage(Message message) {
                Bundle extras = message.getData();
                if (extras != null) {
                    mess += extras.getString("mess")+"\n";
                    infosTxt.setText(mess);
                }
            }
        };
    }

    public ServiceConnection configureConnection(){
        return new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                /* Question 1 */
//                BroadcastService.LocalBinder binder = (BroadcastService.LocalBinder) service;
//                mService = binder.getService();
                /* Question 2 */
                iBroadcastService = IBroadcastService.Stub.asInterface(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
            }
        };
    }
}