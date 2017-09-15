package ensicaencorp.satefari2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton myButton8 = (ImageButton) findViewById(R.id.imageButton8);
        TextView myText3 = (TextView) findViewById(R.id.textView3 );
        TextView myText4 = (TextView) findViewById(R.id.textView4 );
        myText3.setAlpha(0);
        myText4.setAlpha(1);

        myButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                //tries connecting to blackbox here

                Intent myIntent = new Intent(MainActivity.this, ControllerActivity.class);
                startActivity(myIntent);
            }
        });
    }
}