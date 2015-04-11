package com.estime.linker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created by Desktop on 12/15/2014.
 */
public class BlueTooth extends Activity{

    private final static int REQUEST_ENABLE_BT = 1;
    protected static BluetoothAdapter myRadio;
    boolean connected=false;

    public BlueTooth (){
        myRadio= BluetoothAdapter.getDefaultAdapter();
    }

    public boolean BlueCapable(){
        if (myRadio == null) {
            return false;
            // Device does not support Bluetooth
        }
        return true;
    }

    public void TurnOnBT(){
        if (!myRadio.isEnabled()) {
            myRadio.enable();
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //return connected;
    }

    public void TurnOffBT(){

        if(myRadio.isEnabled()){
            //run code to disable bluetooth
            myRadio.disable();
        }

    }

    public void discoverMode(){

        Intent discoverableIntent = new
        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

    }

    public static void connectThread(){

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                connected=true;
                // Do something with the contact here (bigger example below)
            }
        }
    }

}
