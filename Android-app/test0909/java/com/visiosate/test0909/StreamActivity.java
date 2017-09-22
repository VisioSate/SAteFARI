package com.visiosate.test0909;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
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
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.R.attr.bitmap;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class StreamActivity extends MainActivity {
    int direction = 0;
    int speed = 0;
    float acceleration;
    public String speedSup = "";
    public String directionSup = "";
    public String signdirection = "";
    public String signspeed = "";
    private ImageView mImageView;
    private VideoView mVideoView;
    private WebView webview;
    private ImageButton picFolder;
    private ImageButton takePicButton;
    private Context mContext;
    private ImageView recordIcon;
    private ImageView flash;
    private Animation anim;

    public static final String URL_SNAP = "http://10.0.0.1:8080/snapshot?topic=/raspicam_node/image_raw";
    public static final String IMAGE_URL = "http://10.0.0.1:8080/stream?topic=/raspicam_node/image_raw";

    private static final String TAG = "StreamActivity";
    private static final int REQUEST_CODE = 1000;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 1280;
    private static final int DISPLAY_HEIGHT = 720;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_PERMISSIONS = 10;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=getApplicationContext();

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stream);
        mImageView = (ImageView) findViewById(R.id.image);
        udpClientHandler = new UdpClientHandler(this);
        webview = (WebView) findViewById(R.id.webview);
        webview.setPadding(0, 0, 0, 0);
        webview.setInitialScale(getScale());

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        webview.loadUrl(IMAGE_URL);
        recordIcon = (ImageView) findViewById(R.id.rec);
        flash = (ImageView) findViewById(R.id.flash);

        udpClientHandler = new UdpClientHandler(this);

        anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setStartOffset(1);
        anim.setRepeatMode(Animation.ABSOLUTE);
        anim.setRepeatCount(Animation.INFINITE);

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

                      @Override
                      public void run() {


                          if (acceleration > 0){
                              if (speed < 0){
                                  speed = speed + (int) (acceleration * 200);
                              }
                              else if (speed >= 0) {
                                  if (speed + acceleration * 25 < 1023) {
                                      speed = speed + (int) (acceleration * 25);
                                  } else {
                                      speed = 1023;
                                  }
                              }
                          } else if (acceleration < 0) {
                              if (speed>=0) {
                                  speed = speed + (int) (acceleration * 200);
                              }
                              //delete here
                              /*if(speed<0){
                                  speed=0;
                              }*/
                              else if (speed < 0){
                                  if (speed + acceleration * 25 > - 1023){
                                      speed = speed +(int) (acceleration * 25);
                                  } else {speed = -1023; }
                              }
                          }


                          if (Integer.toString(Math.abs(speed)).length()<4){
                              if (Integer.toString(Math.abs(speed)).length()==1){
                                  speedSup = "000";
                              }
                              if (Integer.toString(Math.abs(speed)).length()==2){
                                  speedSup = "00";
                              }
                              if (Integer.toString(Math.abs(speed)).length()==3){
                                  speedSup = "0";
                              }
                          } else {speedSup = "";}

                          if (Integer.toString(Math.abs(direction)).length()<3){
                              if (Integer.toString(Math.abs(direction)).length()==1){
                                  directionSup = "00";
                              }
                              if (Integer.toString(Math.abs(direction)).length()==2){
                                  directionSup = "0";
                              }
                          } else {directionSup = "";}

                          if(direction<0){
                              signdirection = "-";
                          } else { signdirection = "+"; }

                          if(speed<0){
                              signspeed = "-";
                          } else { signspeed = "+"; }

                          udpClientThread = new UdpClientThread(
                                  "10.0.0.1",
                                  Integer.parseInt("12345"),
                                  udpClientHandler,
                                  signspeed + speedSup + Math.abs(speed) + " " + signdirection + directionSup + Math.abs(direction));
                          udpClientThread.start();

                      }
                  },
                /*Set how long before to start calling the TimerTask (in milliseconds)*/
                0,
                /*Set the amount of time between each execution (in milliseconds)*/
                100);

        //take a picture
        takePicButton = (ImageButton) findViewById(R.id.btn_takepic); // take a picture
        assert takePicButton != null;
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mToggleButton.isChecked()) {
                    flash.setVisibility(VISIBLE);
                    final CountDownTimer testTimer = new CountDownTimer(150, 50) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            flash.setVisibility(INVISIBLE);
                        }
                    }.start();
                }
                new ImageDownloadAndSave().execute("");
            }
        });

        /* create directory for recordings */
        String sdCard = Environment.getExternalStorageDirectory()+"/satefari/";
        File myDir = new File(sdCard);

        /* if directory doesn't exist, create it */
        if(!myDir.exists())
        {
            myDir.mkdir();
        }

        /* video recording */

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaRecorder = new MediaRecorder();

        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);

        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(StreamActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                        .checkSelfPermission(StreamActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (StreamActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale
                                    (StreamActivity.this, Manifest.permission.RECORD_AUDIO)) {
                        mToggleButton.setChecked(false);
                        Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(StreamActivity.this,
                                                new String[]{Manifest.permission
                                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                                REQUEST_PERMISSIONS);
                                    }
                                }).show();
                    } else {
                        ActivityCompat.requestPermissions(StreamActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    onToggleScreenShare(v);
                    recordIcon.setAlpha(1.0f);
                    recordIcon.startAnimation(anim);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mToggleButton.setVisibility(View.VISIBLE);
                    }
                }, 1500);
                if(mToggleButton.isChecked())
                    picFolder.setVisibility(INVISIBLE);
                else {
                    picFolder.setVisibility(VISIBLE);
                    recordIcon.setAlpha(0.0f);
                    recordIcon.clearAnimation();
                }
            }
        });

        picFolder = (ImageButton) findViewById(R.id.picfolder); // view directory
        assert picFolder != null;
        picFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mToggleButton.isChecked()) {
                    Intent myIntent = new Intent(mContext, ListActivity.class);
                    startActivity(myIntent);
                }
            }
        });
        //TIMER : send direction and speed data every 0.1s

        //button to close the application
        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
                //finish();
            }
        });
    }

    //exit activity if a controller is disconnected
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                System.exit(0);
            }
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
        mMediaProjectionCallback = new MediaProjectionCallback();
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    public void onToggleScreenShare(View view) {
        if (((ToggleButton) view).isChecked()) {
            initRecorder();
            shareScreen();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Stopping Recording");
            stopScreenSharing();
        }
    }

    private void shareScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("StreamActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void initRecorder() {
        try {

            //Date timestamp = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateAndTime = sdf.format(new Date());

            //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/satefari/" + currentDateAndTime + ".mp4");
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(512 * 2000);
            mMediaRecorder.setVideoFrameRate(10);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyMediaProjection();
    }

    private void destroyMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "MediaProjection Stopped");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if ((grantResults.length > 0) && (grantResults[0] +
                        grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                    onToggleScreenShare(mToggleButton);
                } else {
                    mToggleButton.setChecked(false);
                    Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                return;
            }
        }
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (mToggleButton.isChecked()) {
                mToggleButton.setChecked(false);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                Log.v(TAG, "Recording Stopped");
            }
            mMediaProjection = null;
            stopScreenSharing();
        }
    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = display.getHeight();
        Double val = new Double(height)/new Double(360);
        val = val * 100d;
        return val.intValue();
    }

    private class ImageDownloadAndSave extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... arg0)
        {
            downloadImagesToSdCard("","");
            return null;
        }

        private void downloadImagesToSdCard(String downloadUrl,String imageName)
        {
            Date timestamp = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", timestamp);

            try
            {
                URL url = new URL(URL_SNAP);
                /* creates directory */
                String sdCard = Environment.getExternalStorageDirectory()+"/satefari/";
                //String sdCard = Environment.getExternalStorageDirectory()+"/DCIM/Satefari";
                File myDir = new File(sdCard,timestamp+".jpg");

                /* if directory doesn't exist, create it */
                if(!myDir.exists())
                {
                    myDir.mkdir();
                }

                /* checks if the file exists and delete it if this is the case (if user presses too quickly on the pic button, it's no use to store two times the same pic) */
                String fname = imageName;
                File file = new File (myDir, fname);
                if (file.exists ())
                    file.delete ();

                /* Opens a connection */
                URLConnection ucon = url.openConnection();
                InputStream inputStream = null;
                HttpURLConnection httpConn = (HttpURLConnection)ucon;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    inputStream = httpConn.getInputStream();
                }

                FileOutputStream fos = new FileOutputStream(file);
                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ( (bufferLength = inputStream.read(buffer)) >0 )
                {
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                }
                fos.close();
            }
            catch(IOException io)
            {
                io.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //set the speed requirement sent to the car (0 to 1023)
    public int getSpeed(InputEvent event){
        if (event instanceof MotionEvent) {

            // Use the hat axis value to find the D-pad direction as well as the triggers axis value
            MotionEvent motionEvent = (MotionEvent) event;
            float acctrigger = motionEvent.getAxisValue(MotionEvent.AXIS_BRAKE);
            float dcctrigger = motionEvent.getAxisValue(MotionEvent.AXIS_THROTTLE);
            float yaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);
            float yPadAxis = motionEvent.getAxisValue(MotionEvent.AXIS_RZ);

            if (Float.compare(yaxis, -1.0f) == 0) {
                acceleration = 1;
            } else if (Float.compare(yaxis, 1.0f) == 0) {
                acceleration = -1;
            } /*else if (dcctrigger != 0){
                accfloat = - dcctrigger;
                acceleration = (int) accfloat;
            } else if (acctrigger != 0) {
                accfloat = acctrigger;
                acceleration = (int) accfloat;
            } */
            else if (Math.abs(yPadAxis) > 0.1 ){
                acceleration = - yPadAxis;
            }
            else {acceleration = 0; }

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
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        KeyEvent keyEvent = (KeyEvent) event;
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_C) {
            System.exit(0);
        }
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BUTTON_B) {
            speed = -100;
        }
        return true;
    }
}