package com.example.gallusawa.clientaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.gallusawa.serveraidl.IMyAidlInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView textView;
    List<String> names;
    IMyAidlInterface aidl;
    AddServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();

        textView = (TextView) findViewById(R.id.textView);
    }

    private void initService() {
        connection = new AddServiceConnection();
        Intent i = new Intent()
                .setComponent(new ComponentName("com.example.gallusawa.serveraidl", "com.example.gallusawa.serveraidl.AIDLservice"));
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    class AddServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidl = IMyAidlInterface.Stub.asInterface(iBinder);

            try {
                textView.setText(aidl.names().toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                Log.d(TAG, "onServiceConnected: " + aidl.names().toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            aidl = null;
        }
    }
}
