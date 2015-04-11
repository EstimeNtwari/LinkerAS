package com.estime.linker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FragmentB extends android.support.v4.app.Fragment implements SensorEventListener {

    TextView outText, speedo, status;
    ImageButton upButton, downButton, leftButton, rightButton;
    boolean upPressed,downPressed, mForward, mBackward;
    ProgressBar speedBar;
    private SensorManager sManager;
    speedThread mySpeedThread;
    private Activity mActivity;
    ImageView mArrowUp, mArrowDown;



	public FragmentB() {

	}


    private void motionPublish(float v, float h){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mArrowUp.setImageLevel();//CREATE MAP FROM -75,10 TO 0,10K
            }
        });
    }


    private void publishProgress() {
        //Log.v("thread", "reporting back from the speed Thread");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //updateResults(text);
                speedo.setText("Speed : "+ ((MainActivity)getActivity()).getSpeed());
                speedBar.setProgress(((MainActivity)getActivity()).getSpeed());

                int ui=((MainActivity) getActivity()).getUIMode();
                if(ui!=0){

                    if(ui==1){
                        buttonUI();
                    }else{
                        motionUI();
                    }

                    ((MainActivity) getActivity()).setUIMode(0);
                }

                //Log.w("myapp", "this:" +((MainActivity)getActivity()).getConnected());


            }
        });
    }






    class speedThread extends Thread{ //local class responsible for updating the main ui thread
        @Override
        public void run() {
            super.run();
            try{
                while(true){
                    int delay;
                    int currentspeed=((MainActivity)getActivity()).getSpeed();
                    if(upPressed||mForward){
                        delay= 1+currentspeed;
                        ((MainActivity)getActivity()).upSpeed();
                    }else if(downPressed||mBackward){
                        delay= 200-2*currentspeed;
                        ((MainActivity)getActivity()).downSpeed();
                    }else{
                        delay= 200-currentspeed;
                        ((MainActivity)getActivity()).downSpeed();
                    }



                    sleep(delay);

                    publishProgress(); //send data to main ui thread
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity= activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sManager = (SensorManager) mActivity.getSystemService(Activity.SENSOR_SERVICE);
    }


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_b, container, false);

        //pull ui elements from layout file
        //sManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        outText= (TextView) rootview.findViewById(R.id.fragBText);
        speedo= (TextView) rootview.findViewById(R.id.speedo);
        upButton= (ImageButton) rootview.findViewById(R.id.upButton);
        downButton= (ImageButton) rootview.findViewById(R.id.downButton);
        leftButton= (ImageButton) rootview.findViewById(R.id.leftButton);
        rightButton= (ImageButton) rootview.findViewById(R.id.rightButton);
        status = (TextView) rootview.findViewById(R.id.StatusText);
        speedBar = (ProgressBar) rootview.findViewById(R.id.speedBar);
        speedo.setText("Speed : "+ ((MainActivity)getActivity()).getSpeed());
        mArrowUp = (ImageView) rootview.findViewById(R.id.motionVerticalUP);
        mArrowDown = (ImageView) rootview.findViewById(R.id.motionVerticalDOWN);
        mArrowDown.setImageResource(R.drawable.motion_downdraw);
        mArrowUp.setImageResource(R.drawable.motion_updraw);

        if(((MainActivity)getActivity()).getConnected()){
            status.setText("Status: Connected");
            status.setTextColor(Color.GREEN);

        }else{
            status.setText("Status: Disconnected");
            status.setTextColor(Color.RED);
        }

        if(((MainActivity)getActivity()).getMControl()) {
            //SET ALL BUTTON INVISIBLE
            //motionUI();

        }else{



        }

            upButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    upPressed = true;
                    try {
                        ((MainActivity) getActivity()).write((byte) 4);
                    } catch (IOException e) {



                    }
                    outText.setText("SPEED UP!");
                    return false;
                }
            });
            upButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL && upPressed) {
                        upPressed = false;
                        try {
                            ((MainActivity) getActivity()).write((byte) 0);
                        } catch (IOException e) {

                        }
                    }
                    return false;
                }
            });


            downButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    downPressed = true;
                    outText.setText("SLOW DOWN!");
                    try {
                        ((MainActivity) getActivity()).write((byte) 0);
                    } catch (IOException e) {

                    }
                    return false;
                }
            });
            downButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL && downPressed) {
                        downPressed = false;
                        try {
                            ((MainActivity) getActivity()).write((byte) 0);
                        } catch (IOException e) {
                            //

                        }
                    }

                    return false;
                }
            });



            rightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    outText.setText("RIGHT!");
                    try {
                        ((MainActivity) getActivity()).write((byte) 1);
                    } catch (IOException e) {

                    }

                }
            });

            leftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    outText.setText("LEFT!");
                    try {
                        ((MainActivity) getActivity()).write((byte) 10);
                    } catch (IOException e) {

                    }


                }
            });

        return rootview;
	}

    @Override
    public void onResume() {
        super.onResume();
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {

        mySpeedThread.interrupt();

        sManager.unregisterListener(this);
        super.onStop();

    }

    @Override
    public void onStart() {
        mySpeedThread = new speedThread();
        mySpeedThread.start();


        super.onStart();
    }

    public void motionUI(){
        mArrowUp.setVisibility(View.VISIBLE);
        mArrowDown.setVisibility(View.VISIBLE);

        Log.w("myapp", "BUTTONS ARE DISABLED");
        upButton.setEnabled(false);
        upButton.setClickable(false);
        upButton.setVisibility(View.GONE);

        downButton.setEnabled(false);
        downButton.setClickable(false);
        downButton.setVisibility(View.GONE);

        leftButton.setEnabled(false);
        leftButton.setClickable(false);
        leftButton.setVisibility(View.GONE);

        rightButton.setEnabled(false);
        rightButton.setClickable(false);
        rightButton.setVisibility(View.GONE);

        outText.setEnabled(false);
        outText.setVisibility(View.GONE);
    }

    public void buttonUI(){
        mArrowUp.setVisibility(View.GONE);
        mArrowDown.setVisibility(View.GONE);

        upButton.setEnabled(true);
        upButton.setClickable(true);
        upButton.setVisibility(View.VISIBLE);

        downButton.setEnabled(true);
        downButton.setClickable(true);
        downButton.setVisibility(View.VISIBLE);

        rightButton.setEnabled(true);
        rightButton.setClickable(true);
        rightButton.setVisibility(View.VISIBLE);

        leftButton.setEnabled(true);
        leftButton.setClickable(true);
        leftButton.setVisibility(View.VISIBLE);


        outText.setEnabled(true);
        outText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) { // triggered whenever the sensor value has changed.

        if(((MainActivity)getActivity()).getMControl()){
            //Motion Control
            float angle=event.values[1];
            float yaw = event.values[0];

            motionPublish(angle, 0);

            if(angle<10 && angle>-60){ //SPEED UP
                if(((MainActivity) getActivity()).getConnected()){
                    try {
                        ((MainActivity) getActivity()).write((byte) 4);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mBackward=false;
                mForward=true;

            }else if(angle<-95) { //SlowDown
                if (((MainActivity) getActivity()).getConnected()){
                    try {
                        ((MainActivity) getActivity()).write((byte) 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mBackward=true;
                mForward=false;

            }else{
                mBackward=false;
                mForward=false;

            }
        }else{
            mBackward=false;
            mForward=false;
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}


