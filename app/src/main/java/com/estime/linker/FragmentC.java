package com.estime.linker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FragmentC extends Fragment  {
    ToggleButton BTT;
    ToggleButton nfcButton;
    ToggleButton MControl;
    boolean BTchecked= false;



    //retreive bluetooth device


    public FragmentC() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View rootView = inflater.inflate(R.layout.fragment_c, container, false);

        BTT = (ToggleButton) rootView.findViewById(R.id.BTToggle); //Bluetooth toggle object creation

        BTT.setChecked(((MainActivity)getActivity()).BTstatus());

        MControl= (ToggleButton) rootView.findViewById(R.id.MotionToggle); //Create toggle object for motion controls


        BTT.setOnClickListener(new View.OnClickListener() { //Bluetooth Toggle button.
            @Override
            public void onClick(View v) {

                    if(BTT.isChecked() && !((MainActivity)getActivity()).BTstatus()){
                        Log.w("myApp", "BUTTON IS NOW CHECKED");
                        if (((MainActivity)getActivity()).TurnBTon()) {
                            Log.w("myApp", "BlueTooth Enabled");
                            Toast.makeText(getActivity(),"Bluetooth Enabled",Toast.LENGTH_SHORT).show();
                            BTchecked=true;
                        } else {
                            BTT.setChecked(false);
                            BTchecked=false;
                        }
                        //toast to show that bluetooth is connected
                    }
                    else if (!BTT.isChecked() && ((MainActivity)getActivity()).BTstatus()){
                        Log.w("myApp", "BUTTON IS NOW unCHECKED");
                        if(((MainActivity)getActivity()).TurnOffBT()){
                            Log.w("myApp", "BlueTooth disabled");
                            Toast.makeText(getActivity(),"Bluetooth Disabled",Toast.LENGTH_SHORT).show();
                            BTchecked=false;

                        }
                        else{
                            BTT.setChecked(true);
                            BTchecked=true;
                        }
                        //toast to show that bluetooth is disabled
                    }
                    else{
                        Log.w("myApp", "ELSE "+BTT.isChecked());
                        BTT.setChecked(((MainActivity)getActivity()).BTstatus());
                    }
                    //TURN OFF BLUETOOTH
                }
        });

        MControl.setOnClickListener(new View.OnClickListener() { //Toggle for motion control
            @Override
            public void onClick(View v) {

                if(MControl.isChecked()){
                    ((MainActivity)getActivity()).setMControl(true);
                    ((MainActivity) getActivity()).setUIMode(2);

                }else{
                    ((MainActivity)getActivity()).setMControl(false);
                    ((MainActivity) getActivity()).setUIMode(1);

                }
            }
        });


        //switch listener
        return rootView;
    }

}
