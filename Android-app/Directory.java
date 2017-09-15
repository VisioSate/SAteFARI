package com.visiosate.test0909;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;


public class Directory extends AppCompatActivity {
    private ImageView mImageView;
    int i;
    ImageView image;
    private Context mContext;
    private String[] fileList;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        gridView = (GridView) findViewById(R.id.gridview);

        gridView.setAdapter(new ImageAdapter(this));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        String.valueOf(position), Toast.LENGTH_SHORT).show(); // renvoie le bon numéro (commence par 0), à transmettre vers SinglePicture et obtenir l'adresse correspondante pour l'afficher
            }
        });
        String path = Environment.getExternalStorageDirectory()+"/visiosate/";
        File directory = new File(path);
        File[] files = directory.listFiles();
        /*for (int i = 0; i < files.length; i++) {
            fileList[i] = files[i].getName();
            Toast.makeText(this.getApplicationContext(), "Fichiers : " + fileList[i], Toast.LENGTH_SHORT).show();
        }*/
    }


/*    public int getPx(int dp){ // cette fonction sert à "convertir" les dp en px car setX/Y ne prennent que des pixels, or nous voudrions des valeurs absolues
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,   getResources().getDisplayMetrics());
    }*/

    public static class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(250,250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }

            /*for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
            }*/



            imageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +  mThumbIds[position]));
            //mImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/visiosate/test"+ String.valueOf(value)+ ".jpg"));

            return imageView;
        }

        // references to our images
        private String[] mThumbIds = {
                "/visiosate/test1.jpg",
                "/visiosate/test2.jpg",
                "/visiosate/test3.jpg",
                "/visiosate/test4.jpg",
                "/visiosate/test5.jpg",
                "/visiosate/test6.jpg",
                "/visiosate/test7.jpg",
                "/visiosate/test8.jpg",
                "/visiosate/test9.jpg",
                "/visiosate/test10.jpg",
                "/visiosate/test11.jpg",
                "/visiosate/test12.jpg"
        };
    }
}

 /*LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayout);
        for(i=1;i<=3;i++)
        {
            image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(320,180));
            //image.setMaxHeight(180);
            //image.setMaxWidth(320);
            image.setX(i*161);
            image.setY(i*91);
            image.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/visiosate/test" + String.valueOf(i) + ".jpg"));
            image.setId(i);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Directory.this, SinglePicture.class);
                    Bundle b = new Bundle();
                    b.putInt("key", v.getId()); // permet de récupérer l'id de la photo pour l'afficher en grand
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            // Ajouter l'image au layout
            layout.addView(image);
        }*/


//for(i=1;i<=12;i++) {
//  image = new ImageView(this);
//image.setLayoutParams(new android.view.ViewGroup.LayoutParams(320, 180));
//image.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/visiosate/test" + String.valueOf(i) + ".jpg"));
//image.setId(i);
            /*image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Directory.this, SinglePicture.class);
                    Bundle b = new Bundle();
                    b.putInt("key", v.getId()); // permet de récupérer l'id de la photo pour l'afficher en grand
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });*/