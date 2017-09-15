package ensicaencorp.satefari2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.VideoView;
/**
 * Created by Clément on 15/09/2017.
 */

public class CamMYOActivity extends MainActivity{

    int direction = 0;
    int speed = 0;
    private ImageView mImageView;
    private VideoView mVideoView;
    private WebView webview;
    private Button picFolder;
    private Button takePicButton;
    private Context mContext;
    //public static final String URL_SNAP = "http://10.0.0.1:8080/snapshot?topic=/raspicam_node/image_raw";
    public static final String IMAGE_URL = "http://10.0.0.1:8080/stream?topic=/raspicam_node/image_raw";
    public static final String URL_SNAP = "http://www.normandie-heritage.com/IMG/jpg/vache_normande_000.jpg";
    //public static final String IMAGE_URL = "http://www.insecam.org/en/view/557619/";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        mImageView = (ImageView) findViewById(R.id.image);

        webview = (WebView) findViewById(R.id.webview);
        webview.setPadding(0, 0, 0, 0);
        webview.setInitialScale(getScale());

        webview.loadUrl(IMAGE_URL);

        final CountDownTimer controllerTimer = new CountDownTimer(10000, 10) {

            public void onTick(long millisUntilFinished) {
                udpClientThread = new UdpClientThread(
                        "1.0.0.10",
                        Integer.parseInt("12345"),
                        udpClientHandler,
                        speed+" "+direction);
                udpClientThread.start();
            }
            public void onFinish() {

            }

        }.start();

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

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = display.getHeight();
        Double val = new Double(height)/new Double(240);
        val = val * 100d;
        return val.intValue();
    }

    protected void openFolder(){
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/visiosate/");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");

        if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
        {
            startActivity(intent);
        }
        else
        {
            // if you reach this place, it means there is no any file
            // explorer app installed on your device
        }
    }

    //part to change : works for PS4 but not MYO
    /*
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

    public int sendSpeed(int speed){
        return speed;
    }
    */
}
