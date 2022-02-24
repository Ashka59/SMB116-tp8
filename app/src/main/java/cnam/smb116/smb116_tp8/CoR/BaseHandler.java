package cnam.smb116.smb116_tp8.CoR;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BaseHandler extends ChainHandler<String, String, Messenger, String> {

    private static final String TAG = "BaseHandler";
    private Context context;

    public BaseHandler(Context context, ChainHandler<String, String, Messenger, String> successor){
        super(successor);
        this.context = context;
    }

    public boolean handleRequest(String value, String number, Messenger messenger, String filter){
        return super.handleRequest(value, number, messenger, filter);
    }

    protected String readConfiguration(String name){
        try {
            FileInputStream fis = context.openFileInput(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String value = (String) ois.readObject();
            ois.close();
            Log.i(TAG,"readConfiguration");
            return value;
        }catch (IOException | ClassNotFoundException ioException){
            Log.d(TAG, ioException.getMessage());
            return null;
        }
    }

    protected Boolean writeConfiguration(String name, String value){
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(value);
            oos.close();
            Log.i(TAG,"writeConfiguration");
            return true;
        }catch (IOException ioException){
            Log.d(TAG, ioException.getMessage());
            return false;
        }
    }

    protected String getDate(){
        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        DateFormat dt = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE);
        return df.format(c.getTime()) + "-" + dt.format(c.getTime());
    }

    protected Boolean filterOK(String filter){
        Boolean ok = null;
        if (filter != null){
            ok = filter.contains(getClass().getSimpleName());
        }else{
            ok = true;
        }
        return ok;
    }

    public void sendMessage(Messenger messenger, String mess){
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("mess", mess);
        msg.setData(bundle);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
