package com.visiosate.test0909;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
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
import java.util.Date;



import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.R.attr.bitmap;


public class StreamActivity extends AppCompatActivity{

    private ImageView mImageView;
    private VideoView mVideoView;
    private WebView webview;
    private Button picFolder;
    private Button takePicButton;
    private Context mContext;
    //public static final String URL_SNAP = "http://10.0.0.1:8080/snapshot?topic=/raspicam_node/image_raw";
    //public static final String IMAGE_URL = "http://10.0.0.1:8080/stream?topic=/raspicam_node/image_raw";
    public static final String URL_SNAP = "http://www.normandie-heritage.com/IMG/jpg/vache_normande_000.jpg";
    public static final String IMAGE_URL = "http://www.insecam.org/en/view/557619/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        mImageView = (ImageView) findViewById(R.id.image);

        webview = (WebView) findViewById(R.id.webview);
        webview.setPadding(0, 0, 0, 0);
        webview.setInitialScale(getScale());

        webview.loadUrl(IMAGE_URL);


        /*takePicButton = (Button) findViewById(R.id.btn_takepic); // take a picture
        assert takePicButton != null;
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFolder();
            }
        });
        //picFolder = (Button) findViewById(R.id.btn);*/


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
}


//takevid

