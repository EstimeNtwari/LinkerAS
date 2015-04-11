package com.estime.linker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends FragmentActivity implements TabListener {

	ActionBar actionBar;
	ViewPager viewPager;
    private ArrayAdapter<String> adapter;

    private final static int REQUEST_ENABLE_BT = 1;
    public static BluetoothAdapter myRadio;
    public boolean connected, ScanDone, mControl;
    private int speed, UIMode;
    ProgressBar spinner;
    BluetoothDevice device, HC06;
    private OutputStream outputStream;
    private InputStream inStream;
    BluetoothSocket socket = null;

    {
        connected = false;
        speed=1;
        mControl=false;
        UIMode=0;
    }

    public int getUIMode(){
        return UIMode;
    }
    public void setUIMode(int u){
        UIMode= u;
    }

    public boolean getMControl(){return mControl;}
    public void setMControl(boolean m){mControl=m;}

    public boolean BTstatus(){
        if (myRadio == null) {
            // Device does not support Bluetooth
            return false;
        }

        return myRadio.isEnabled();
    }
    public boolean discover(){
        return myRadio.startDiscovery();
    }
    public ArrayAdapter<String> getAdapter(){
        return adapter;
    }
    public void clearList(){
        adapter.clear();
    }

    public boolean getConnected(){return connected;}
    public void setConnected(boolean c){connected=c;}


    public boolean TurnBTon(){
        if (myRadio == null) {
            // Device does not support Bluetooth
            return false;
        }

        if (!myRadio.isEnabled()) {
            return myRadio.enable();
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return false;
    }

    public boolean TurnOffBT(){

        if(myRadio.isEnabled()){
            //run code to disable bluetooth
            return myRadio.disable();
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return false;
    }
    public void write(byte s) throws IOException {
        if(connected) {
            try {
                outputStream.write(s);
            } catch (IOException e) {
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                //adapter.clear();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //ScanDone=true;
                    // Get the BluetoothDevice object from the Intent
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if(device.getAddress().equals("30:14:12:12:10:85")){
                        Log.w("HC06 IF STATEMENT",""+device.getAddress());
                        HC06= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.w("MYAPP", "" + HC06.getAddress());
                        Log.w("MYAPP", ""+HC06.getName());
                    }
                    // Add the name and address to an array adapter to show in a ListView
                    if (adapter.getPosition((device.getName() + "\n" + device.getAddress())) == -1) {
                        adapter.add(device.getName() + "\n" + device.getAddress());
                        adapter.notifyDataSetChanged();
                    }
                    //Log.w("MYAPP", ""+device.getAddress());
                    //Log.w("MYAPP", ""+device.getName());

                }
                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    spinner = (ProgressBar) findViewById(R.id.progressBar1);
                    spinner.setVisibility(View.GONE);

                }
                /*if(BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)){
                    BluetoothDevice newDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Class<?> btDeviceInstance =  Class.forName(BluetoothDevice.class.getCanonicalName());

                    Method convert = btDeviceInstance.getMethod("convertPinToBytes", String.class);

                    byte[] pin = (byte[]) convert.invoke(newDevice, "1234");

                    Method setPin = btDeviceInstance.getMethod("setPin", byte[].class);
                    boolean success = (Boolean) setPin.invoke(newDevice, pin);
                }*/
            } catch (Exception e) {
                myRadio.disable();
            }
        }
    };

    public void connectDevice() throws IOException {
        int pin=1234;

        Log.w("MYAPP", "SOC"+HC06.getAddress());
        Log.w("MYAPP", "SOC"+HC06.getName());
        final UUID MY_UUID =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        ParcelUuid[] uuids = HC06.getUuids(); //create unique identifier for device

        //Log.w("MYAPP", "UUID"+uuids[1].getUuid());

        //create connection socket and set to blank
        socket = HC06.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        //socket = HC06.createRfcommSocketToServiceRecord(MY_UUID); //set socket to connected device
        socket.connect(); //establish connection
        outputStream = socket.getOutputStream();
        inStream = socket.getInputStream();
        connected=true;

    }
    public void disconnect() throws IOException {
        try {
            socket.close();
            connected=false;
        } catch (IOException e) {

        }

        //create subroutine to  disconnect socket
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        myRadio= BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);


        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);




        viewPager=(ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));



        viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
				
			}
        	
        });
        
        
        actionBar=getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab tab1= actionBar.newTab();
        tab1.setText("Connect");
        tab1.setTabListener(this);
        
        ActionBar.Tab tab2= actionBar.newTab();
        tab2.setText("Control");
        tab2.setTabListener(this);
        
        ActionBar.Tab tab3= actionBar.newTab();
        tab3.setText("Config");
        tab3.setTabListener(this);
        
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
       
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent intent= new Intent(this, AboutMessage.class);
        	startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


    public void setSpeed(int s){
        speed=s;
    }
    public int getSpeed(){return speed;}

    public void upSpeed(){if(speed<100){speed+=1;}}
    public void downSpeed(){if(speed>0){speed--;}}

}

class MyAdapter extends FragmentPagerAdapter{ //Handler for tab actions (creation and transitions)

	public MyAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment f = null;
		if(arg0==0){
			f= new FragmentA();
		}
		if(arg0==1){
			f= new FragmentB();


		}
		if(arg0==2){
			f= new FragmentC();
		}
		return f;
	}

	@Override
	public int getCount() {
		//TODO Auto-generated method stub

		return 3;
	}

	
	
}


