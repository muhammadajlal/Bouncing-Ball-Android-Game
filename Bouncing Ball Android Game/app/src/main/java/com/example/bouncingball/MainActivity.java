package com.example.bouncingball;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyCanvas C = new MyCanvas(getApplicationContext());
        //Setting up acccelerometer

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float [] v = event.values;
                if(v[0] < -2){
                    if(C.tabx + C.tabw < C.canvas_width)
                        C.tab_delta_x = 10;
                    else
                        C.tab_delta_x = 0;
                }
                if(v[0] > 2){
                    if(C.tabx > 10)
                        C.tab_delta_x = -10;
                    else
                        C.tab_delta_x = 0;
                }

                if(v[0] > -2 && v[0] < 2)
                    C.tab_delta_x = 0;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        } , accel, sm.SENSOR_DELAY_GAME);



        setContentView(C);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {
                        Thread.sleep(42); // we are using 42 because we want 24 frames per miliseconds 1000/24 = 42
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // redraw the canvas

                    //changing the position of tab
                    C.tabx = C.tabx + C.tab_delta_x;

                    //changing the x and y of the ball
                    C.ballx = C.ballx+ C.ball_delta_x; // continues towards right
                    //setting condition so that the ball do not get out of the canvas screen
                    if (C.canvas_width < C.ballx + C.ballw/2)  //C.ballw/2 == radius of the ball
                        C.ball_delta_x = C.ball_delta_x * -1;  // if the canvas width limit reached it will change the direction
                    if(C.ballx - C.ballw /2 < 0)  // handling the left direction as well
                        C.ball_delta_x = C.ball_delta_x * -1;

                    //height
                    C.bally = C.bally+ C.ball_delta_y; // continues falling
                    if (C.canvas_height < C.bally + C.ballh/2)  //C.ballh/2 == radius of the ball
                        //C.ball_delta_y = C.ball_delta_y * -1;  // if the canvas height limit reached it will change the direction
                        C.gamover = true;
                    else
                        C.gamover = false;
                    if(C.bally - C.ballh /2 < 0)  // handling the upwards direction as well
                        C.ball_delta_y = C.ball_delta_y * -1;

                    if(C.bally + C.ballh/ 2 >= C.taby && C.bally - C.ballh/2 < C.taby + C.tabh) //checking the ball collision with tab
                    {
                        if(C.ballx + C.ballw/2 >= C.tabx && C.ballx - C.ballw < C.tabx + C.tabw)
                        {
                            C.ball_delta_y = C.ball_delta_y * -1;
                        }

                    }

                    if(C.bally + C.ballh/ 2 >= C.bricky && C.bally - C.ballh/2 < C.bricky + C.brickh) //checking the ball collision with brick
                    {
                        if(C.ballx + C.ballw/2 >= C.brickx && C.ballx - C.ballw < C.brickx + C.brickw)
                        {
                            if(C.show_brick1 == true)
                                C.score = 10;
                            C.show_brick1 = false;
                        }

                    }



                    C.invalidate();



                }
            }
        });
        t.start();
    }

}