package com.visiosate.test0909;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListActivity extends StreamActivity {
    GridView gridView;
    ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);
        gridView = (GridView) findViewById(R.id.gridview);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                gridView.setAdapter(new ImageAdapter(getApplicationContext()));
            }
        });
        thread.start();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            Intent intent = new Intent(ListActivity.this, SinglePicture.class);
            Bundle b = new Bundle();
            b.putInt("key", position);
            intent.putExtras(b);
            startActivity(intent);
               }
        });

        //Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //int width = display.getWidth();

        closeButton=(ImageButton) findViewById(R.id.imgClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });


    }

    public static class ImageAdapter extends BaseAdapter {
        String path = Environment.getExternalStorageDirectory()+"/satefari/";
        File f = new File(path);
        public ArrayList<String> filesList = new ArrayList<String>(Arrays.asList(f.list()));
        private Context mContext;
        ImageView imageView;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever(); /* used to create video preview */

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return filesList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        /* create ImageViews for everything in */
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(250,250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10,10,10,10);
            } else {
                imageView = (ImageView) convertView;
            }

            if (filesList.get(position).toLowerCase().endsWith(".jpg")) //if image file
                imageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/satefari/"+filesList.get(position)));
            else if (filesList.get(position).toLowerCase().endsWith(".mp4")) {
                retriever.setDataSource(mContext, Uri.parse(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));
                Bitmap bmp1 = Bitmap.createBitmap(retriever.getFrameAtTime(10000)); // our thumbnail
                imageView.setImageBitmap(bmp1);
            }



            /*
            if(filesList.get(position).toLowerCase().endsWith(".jpg")) //if image file
                        imageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +  "/satefari/" + filesList.get(position)));
            else if(filesList.get(position).toLowerCase().endsWith(".mp4")){
                    retriever.setDataSource(mContext, Uri.parse(Environment.getExternalStorageDirectory() +  "/satefari/" + filesList.get(position)));
                    Bitmap bmp1 = Bitmap.createBitmap(retriever.getFrameAtTime(10000)); // our thumbnail
                    imageView.setImageBitmap(bmp1);

                }
            */


            return imageView;
        }
    }
}
