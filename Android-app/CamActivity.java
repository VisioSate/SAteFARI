package ensicaencorp.satefari2;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.EventLog;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;


import java.io.File;

import static android.R.attr.controlX1;
import static android.R.attr.data;
import static android.R.attr.direction;
import static android.R.attr.path;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by Clément on 11/09/2017.
 */
public class CamActivity extends MainActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    int direction = 0;
    int speed = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        try{
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        //button to close the application
        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        ImageButton myButton4 = (ImageButton) findViewById(R.id.imageButton4);
        myButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView recordIcon = (ImageView) findViewById(R.id.imageView2);
                if (recordIcon.getAlpha() == 0.0f) {
                    recordIcon.setAlpha(1.0f);
                    Animation anim = new AlphaAnimation(1.0f, 0.0f);
                    anim.setDuration(1000);
                    anim.setStartOffset(1);
                    anim.setRepeatMode(Animation.ABSOLUTE);
                    anim.setRepeatCount(Animation.INFINITE);
                    recordIcon.startAnimation(anim);
                } else {
                    recordIcon.setAlpha(0.0f);
                }
            }
        });
    }

    //set the speed requirement sent to the car (0 to 1023)
    public int getSpeed(InputEvent event){
        if (event instanceof MotionEvent) {

            // Use the hat axis value to find the D-pad direction as well as the triggers axis value
            MotionEvent motionEvent = (MotionEvent) event;
            float yaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);
            float acctrigger = motionEvent.getAxisValue(MotionEvent.AXIS_LTRIGGER);
            float dcctrigger = motionEvent.getAxisValue(MotionEvent.AXIS_RTRIGGER);
            float speedfloat =0;

            if (Float.compare(yaxis, -1.0f) == 0) {
                speed = 1023;
            } else if (Float.compare(yaxis, 1.0f) == 0) {
                speed = 0;
            }


            if (dcctrigger != 0){
                Toast.makeText(this, "dcctrigger =" + dcctrigger, Toast.LENGTH_SHORT).show();
                if ((1 - dcctrigger) * 1023 < speed){
                    speedfloat = (1 - dcctrigger) * 1023;
                    speed = (int)speedfloat;
                }
            } else if(acctrigger != 0) {
                Toast.makeText(this, "acctrigger =" + acctrigger, Toast.LENGTH_SHORT).show();
                if (acctrigger * 1023 > speed) {
                    speedfloat = acctrigger * 1023;
                    speed = (int)speedfloat;
                }
            }
        }
        return speed;
    }

    //set the direction requirement to the car (-127 to 128)
    public int getDirection(InputEvent event) {

        // If the input event is a MotionEvent, check its hat axis values.
        if (event instanceof MotionEvent) {

            // Use the hat axis value or the pad value to find the D-pad direction
            MotionEvent motionEvent = (MotionEvent) event;
            float xaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
            float xpadaxis = motionEvent.getAxisValue(MotionEvent.AXIS_X);
            float direcfloat = 0;

            // Check if the AXIS_HAT_X value is -1 or 1, and set the D-pad
            // LEFT and RIGHT direction accordingly.
            if (Float.compare(xaxis, -1.0f) == 0) {
                direction =  -127;
            } else if (Float.compare(xaxis, 1.0f) == 0) {
                direction =  128;
            } else if (xpadaxis != 0) {
                direcfloat = xpadaxis*127;
                direction = (int) direcfloat;
            } else {
                direction = 0;
            }
        }
        return direction;
    }

    @Override
    public boolean dispatchGenericMotionEvent (MotionEvent event){
        direction = getDirection(event);
        speed = getSpeed(event);
        //Toast.makeText(this, "speed =" + speed, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        KeyEvent keyEvent = (KeyEvent) event;
        //Toast.makeText(this, "key", Toast.LENGTH_SHORT).show();
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_C) {
            System.exit(0);
        }
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_B) {
            Toast.makeText(this, "speed =" + speed, Toast.LENGTH_SHORT).show();
        }
        return true;
    }



}