package ensicaencorp.satefari2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;

/**
 * Created by Clément on 11/09/2017.
 */

public class ControllerActivity extends Activity {

    private final static int REQUEST_ENABLE_BT = 1;
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_next);

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        //countdown : after 7 seconds, the ps4 controller is selected
        final CountDownTimer controllerTimer = new CountDownTimer(7000, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                ImageButton chosen = (ImageButton) findViewById(R.id.imageButton2);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                chosen.startAnimation(anim);

                if (mBluetoothAdapter.isEnabled()) {
                    Intent myIntent = new Intent(ControllerActivity.this, CamActivity.class);
                    startActivity(myIntent);
                }
            }
        }.start();

        //activate when MYO is selected
        ImageButton myButton = (ImageButton) findViewById(R.id.imageButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerTimer.cancel();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                ImageButton chosen = (ImageButton) findViewById(R.id.imageButton );
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                chosen.startAnimation(anim);

                if (mBluetoothAdapter.isEnabled()) {
                    Intent myIntent = new Intent(ControllerActivity.this, CamActivity.class);
                    startActivity(myIntent);
                }
            }
        });

        //activate when the ps4 controller is selected
        ImageButton myButton2 = (ImageButton) findViewById(R.id.imageButton2);
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerTimer.cancel();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                ImageButton chosen = (ImageButton) findViewById(R.id.imageButton2 );
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                chosen.startAnimation(anim);

                if (mBluetoothAdapter.isEnabled()) {
                    Intent myIntent = new Intent(ControllerActivity.this, CamActivity.class);
                    startActivity(myIntent);
                }
            }
        });

        //open the settings menu
        ImageButton myButton7 = (ImageButton) findViewById(R.id.imageButton7);
        myButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerTimer.cancel();
                Intent myIntent = new Intent(ControllerActivity.this, SettingsActivity.class);
                startActivity(myIntent);
            }
        });


    }
}
