package ensicaencorp.satefari2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity {


    static EditText editTextAddress, editTextPort;
    ImageButton buttonConnect;
    TextView textViewState, textViewRx;

    UdpClientHandler udpClientHandler;
    UdpClientThread udpClientThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        textViewState = (TextView)findViewById(R.id.state);
        textViewRx = (TextView)findViewById(R.id.received);
        buttonConnect = (ImageButton) findViewById(R.id.connect);

        TextView myText3 = (TextView) findViewById(R.id.textView3 );
        TextView myText4 = (TextView) findViewById(R.id.textView4 );
        myText3.setAlpha(0);
        myText4.setAlpha(1);



        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                buttonConnect.setEnabled(false);



                // Makes "Tap to start" disappear
                TextView myText4 = (TextView) findViewById(R.id.textView4 );
                myText4.setAlpha(0);

                // Makes textView "Connecting to the BlackBox" blink
                TextView myText3 = (TextView) findViewById(R.id.textView3 );
                myText3.setAlpha(1);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                myText3.startAnimation(anim);

                Intent myIntent = new Intent(MainActivity.this, ControllerActivity.class);
                startActivity(myIntent);
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

    private void clientEnd(){
        udpClientThread = null;
        textViewState.setText("clientEnd()");
        buttonConnect.setEnabled(true);

    }

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
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

}