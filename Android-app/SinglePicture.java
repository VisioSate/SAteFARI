package com.visiosate.test0909;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class SinglePicture extends AppCompatActivity {
    private ImageView mImageView;
    private ImageView mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_single_picture);

        Bundle b = getIntent().getExtras();
        int value = 0;
        if (b!=null)
            value = b.getInt("key");

        Toast.makeText(getApplicationContext(), String.valueOf(value), Toast.LENGTH_SHORT).show();

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/visiosate/test"+ String.valueOf(value)+ ".jpg"));

        mail = (ImageView) findViewById(R.id.mail);
        mail.setX(getPx(16)); // je sais pas pourquoi mais ce que je fais dans le xml est ignoré donc je définis la position ici
        mail.setY(getPx(16));



        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Envoi du MMS", Toast.LENGTH_SHORT).show();
            }
        });





    }

    public int getPx(int dp){ // cette fonction sert à "convertir" les dp en px car setX/Y ne prennent que des pixels, or nous voudrions des valeurs absolues
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,   getResources().getDisplayMetrics());
    }

}
