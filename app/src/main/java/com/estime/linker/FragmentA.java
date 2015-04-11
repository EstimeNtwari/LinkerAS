package com.estime.linker;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FragmentA extends android.support.v4.app.Fragment {
	
	ProgressBar Spinner;
    Button ConnectButton, ListButton;
    ListView DeviceList;
    TextView StatusB;
    boolean Test=false;
    ArrayAdapter<String> mAdapter;


	public FragmentA() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
	    Bundle savedInstanceState) {
		View rootView= inflater.inflate(R.layout.fragment_a, container, false);
        mAdapter = ((MainActivity)getActivity()).getAdapter();
        DeviceList = (ListView)rootView.findViewById(R.id.DeviceList);
        DeviceList.setAdapter(mAdapter);
        ConnectButton = (Button) rootView.findViewById(R.id.ConnectButton);
        Spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);
        ListButton= (Button)rootView.findViewById(R.id.testConnect);

        if(((MainActivity)getActivity()).getConnected()){
            ConnectButton.setText("Disconnect");
        }else{
            ConnectButton.setText("Connect");
        }

        Spinner.setVisibility(View.GONE);

		ListButton.setOnClickListener(new OnClickListener() //LIST BUTTON ONCLICK LISTENER
		{
			@Override
			public void onClick(View v){
                ((MainActivity)getActivity()).clearList();
                Spinner.setVisibility(View.VISIBLE);

                ((MainActivity)getActivity()).discover();

			}
			
		});

        //RESPOND TO CONNECT BUTTON EVENT EVENT
        ConnectButton.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((MainActivity)getActivity()).getConnected()){

                    try {
                        ((MainActivity)getActivity()).connectDevice();

                        ConnectButton.setText("Disconnect");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Unable to find HC06 Device",Toast.LENGTH_SHORT).show();

                    }

                }else{
                    ((MainActivity)getActivity()).setConnected(false);
                    try {
                        ((MainActivity)getActivity()).disconnect();
                        ConnectButton.setText("Connect");
                    } catch (IOException e) {
                        Toast.makeText(getActivity(),"Unable to Disconnect",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });



		// Inflate the layout for this fragment
		return rootView;
	}



}
