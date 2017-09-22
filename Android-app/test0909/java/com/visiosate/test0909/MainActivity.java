package com.visiosate.test0909;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity {

  Button connect;
  TextView textViewState, textViewRx;

  UdpClientHandler udpClientHandler;
  UdpClientThread udpClientThread;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
      setContentView(R.layout.activity_main);

      checkAndRequestPermissions();

      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);

    connect = (Button) findViewById(R.id.connect);
    connect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        connect.setVisibility(INVISIBLE); /* to prevent start the next activity several times */

        ImageView myText3 = (ImageView) findViewById(R.id.imageView6);
        myText3.setAlpha(1.0f);
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setStartOffset(0);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        myText3.startAnimation(anim);

        final CountDownTimer controllerTimer = new CountDownTimer(4000, 100) {

          public void onTick(long millisUntilFinished) {

          }

          public void onFinish() {

            Intent myIntent = new Intent(MainActivity.this, ControllerActivity.class);
            startActivity(myIntent);
              finish();


          }
        }.start();
      }

    });

    udpClientHandler = new UdpClientHandler(this);
  }

  private void updateState(String state){
    textViewState.setText(state);
  }

  private void updateRxMsg(String rxmsg){
    textViewRx.append(rxmsg + "\n");
  }

  /*private void clientEnd(){
    udpClientThread = null;
    textViewState.setText("clientEnd()");
    buttonConnect.setEnabled(true);

  }*/

  public static class UdpClientHandler extends Handler {
    public static final int UPDATE_STATE = 0;
    public static final int UPDATE_MSG = 1;
    public static final int UPDATE_END = 2;
    private MainActivity parent;

    public UdpClientHandler(MainActivity parent) {
      super();
      this.parent = parent;
    }

    @Override
    public void handleMessage(Message msg) {

      switch (msg.what){
        case UPDATE_STATE:
          parent.updateState((String)msg.obj);
          break;
        case UPDATE_MSG:
          parent.updateRxMsg((String)msg.obj);
          break;
        case UPDATE_END:
          //parent.clientEnd();
          break;
        default:
          super.handleMessage(msg);
      }

    }
  }

  private  boolean checkAndRequestPermissions() {
    int contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
    //int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
    int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    int storage2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
    int wakelock = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK);
    int internet = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
    int bluetooth = ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH);
    int change_network = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_NETWORK_STATE);
    int access_network = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE);
    int write_contacts = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS);
    int bluetooth_admin  = ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN);
    List<String> listPermissionsNeeded = new ArrayList<>();


    if (contacts != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
    }
    if (storage != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    if (storage2 != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    if (wakelock != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.WAKE_LOCK);
    }
    if (internet != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
    }
    if (bluetooth != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.BLUETOOTH);
    }
    if (change_network != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.CHANGE_NETWORK_STATE);
    }
    if (access_network != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
    }
    if (write_contacts != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.WRITE_CONTACTS);
    }
    if (bluetooth_admin != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(android.Manifest.permission.BLUETOOTH_ADMIN);
    }
    if (!listPermissionsNeeded.isEmpty())
    {
      ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
              (new String[listPermissionsNeeded.size()]),1);
      return false;
    }
    return true;
  }

}