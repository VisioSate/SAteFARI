package ensicaencorp.satefari2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Clément on 12/09/2017.
 */

public class SettingsActivity extends MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ImageButton myButton6 = (ImageButton) findViewById(R.id.imageButton6);

        myButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, ControllerActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
