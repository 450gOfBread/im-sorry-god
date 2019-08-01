package com.example.reaky.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.ArraySet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter BA;

    private Set<BluetoothDevice> pairedDevices;

    private BluetoothDevice rasp;
    private BluetoothSocket socket;

    public static ConnectThread connectThread;

    private String macAddress;

    private UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    public static boolean connected;
    public static Set<String> blacklist;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blacklist = new ArraySet<>();
        addToBlacklist("Charging cable", "Select keyboard");

        BA = BluetoothAdapter.getDefaultAdapter();

        if(BA==null){
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        context = this;
        final CardView connect = findViewById(R.id.connect);
        final ConstraintLayout.LayoutParams connectLayout = (ConstraintLayout.LayoutParams) connect.getLayoutParams();

        final TextView connectText = findViewById(R.id.connectText);

        connect.setCardElevation(16);


        final CardView disconnect = findViewById(R.id.disconnect);
        final ConstraintLayout.LayoutParams disconnectLayout = (ConstraintLayout.LayoutParams) disconnect.getLayoutParams();

        final TextView disconnectText = findViewById(R.id.disconnectText);

        disconnect.setCardElevation(16);


        final CardView shutdown = findViewById(R.id.shutdown);
        final ConstraintLayout.LayoutParams shutdownLayout = (ConstraintLayout.LayoutParams) shutdown.getLayoutParams();

        final TextView shutdownText = findViewById(R.id.shutdownText);

        shutdown.setCardElevation(16);

/*        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.setCardElevation(0);
                connectLayout.height =  connectLayout.height - 2;
                connectLayout.width = connectLayout.width - 2;
                connectLayout.leftMargin = connectLayout.leftMargin + 1;
                connectLayout.topMargin = connectLayout.topMargin + 1;

                connectText.setTextSize(12);

                startBluetooth();
            }
            });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect.setCardElevation(0);
                disconnectLayout.height = disconnectLayout.height - 2;
                disconnectLayout.width = disconnectLayout.width - 2;
                disconnectLayout.leftMargin = disconnectLayout.rightMargin + 1;
                disconnectLayout.topMargin = disconnectLayout.topMargin + 1;
                disconnectText.setTextSize(12);

                connectThread.write("quit".getBytes());
                connectThread.cancel();
            }
        }); */

        connect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    connect.setCardElevation(0);
                    connectLayout.height =  connectLayout.height - 8;
                    connectLayout.width = connectLayout.width - 8;
                    connectLayout.leftMargin = connectLayout.leftMargin + 4;
                    connectLayout.topMargin = connectLayout.topMargin + 4;

                    connectText.setTextSize(13.5f);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    connect.setCardElevation(16);
                    connectLayout.height =  connectLayout.height + 8;
                    connectLayout.width = connectLayout.width + 8;
                    connectLayout.leftMargin = connectLayout.leftMargin - 4;
                    connectLayout.topMargin = connectLayout.topMargin - 4;

                    connectText.setTextSize(14);

                    startBluetooth();
                }
                return true;
            }
        });

        disconnect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    disconnect.setCardElevation(0);
                    disconnectLayout.height = disconnectLayout.height - 8;
                    disconnectLayout.width = disconnectLayout.width - 8;
                    disconnectLayout.leftMargin = disconnectLayout.rightMargin + 4;
                    disconnectLayout.topMargin = disconnectLayout.topMargin + 4;
                    disconnectText.setTextSize(13.5f);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    disconnect.setCardElevation(16);
                    disconnectLayout.height = disconnectLayout.height + 8;
                    disconnectLayout.width = disconnectLayout.width + 8;
                    disconnectLayout.leftMargin = disconnectLayout.rightMargin - 4;
                    disconnectLayout.topMargin = disconnectLayout.topMargin - 4;
                    disconnectText.setTextSize(14);

                    connectThread.write("quit".getBytes());
                    connectThread.cancel();
                }
                return true;
            }
        });

        shutdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    shutdown.setCardElevation(0);
                    shutdownLayout.height =  shutdownLayout.height - 8;
                    //shutdownLayout.width = shutdownLayout.width - 8;
                    shutdownLayout.leftMargin = shutdownLayout.leftMargin + 4;
                    shutdownLayout.bottomMargin = shutdownLayout.bottomMargin + 4;
                    shutdownLayout.rightMargin = shutdownLayout.rightMargin + 4;

                    shutdownText.setTextSize(13.5f);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    shutdown.setCardElevation(16);
                    shutdownLayout.height =  shutdownLayout.height + 8;
                    //shutdownLayout.width = shutdownLayout.width + 8;
                    shutdownLayout.leftMargin = shutdownLayout.leftMargin - 4;
                    shutdownLayout.bottomMargin = shutdownLayout.bottomMargin - 4;
                    shutdownLayout.rightMargin = shutdownLayout.rightMargin - 4;

                    shutdownText.setTextSize(14);

                    try {
                        connectThread.write("shutdown".getBytes());
                        connectThread.cancel();
                    }catch (Exception e){

                    }
                }
                return true;
            }
        });

        ConstraintLayout background = findViewById(R.id.background);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected) connectThread.write("test".getBytes());

            }
        });
    }

    public void addToBlacklist(String... strings){
        blacklist.addAll(Arrays.asList(strings));
    }

    public void startBluetooth(){

        pairedDevices = BA.getBondedDevices();



        for(BluetoothDevice bt: pairedDevices){
            if (bt.getName().equalsIgnoreCase("raspberrypi")){
                rasp = bt;
                macAddress = rasp.getAddress();



            }
        }

        connectThread = new ConnectThread();

        connectThread.run();







    }

    private class ConnectThread extends Thread{

        public final OutputStream mmOut;

        public ConnectThread() {

            OutputStream tmpOut = null;

            try {
                socket = rasp.createRfcommSocketToServiceRecord(uuid);
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("ERROR", "socket : failed rfcomm", e);
            }

            mmOut = tmpOut;
        }

        public void run(){
            try{
                socket.connect();
                connected = true;

                Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();

            }catch(IOException e){
                Log.e("ERROR", "socket : Could not connect", e);
                Toast.makeText(context, "Could not connect", Toast.LENGTH_LONG).show();
                try{
                    socket.close();
                    connected = false;
                }catch(IOException ee){
                    Log.e("ERROR", "socket : failed to close on run", ee);
                }
                return;
            }


        }

        public void write(byte[] bytes){
            try{
                mmOut.write(bytes);

            }catch(IOException e){
                Log.e("ERROR", "write : Failed to write",e);
            }
        }

        public void cancel(){
            try{

                socket.close();
                connected = false;
            }catch(IOException e){
                Log.e("ERROR", "socket : failed to close on cancel", e);
            }
        }


    }


    public static class NLService extends NotificationListenerService {

        public NLService(){}

        @Override
        public void onCreate(){
            super.onCreate();

        }

        @Override
        public void onDestroy() {
            super.onDestroy();

        }

        @Override
        public void onNotificationPosted(StatusBarNotification sbn) {
            String title = sbn.getNotification().extras.getString("android.title");
            Log.e("APP", title);
            Log.e("APP", sbn.getPackageName());

            if(connected && (sbn.getPackageName().equalsIgnoreCase("com.samsung.android.messaging") || sbn.getPackageName().equalsIgnoreCase("com.snapchat.android"))){
                connectThread.write("allon".getBytes());
            }
            /*else if(connected && !title.contains("Cable charging") && !title.contains("Select keyboard")) {
                connectThread.write("greenon".getBytes());
            }*/
        }

        @Override
        public void onNotificationRemoved(StatusBarNotification sbn) {

            if(connected) {
                connectThread.write("alloff".getBytes());
            }
        }

    }



}


