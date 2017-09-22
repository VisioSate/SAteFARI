package com.visiosate.test0909;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.ImageView.ScaleType.FIT_XY;

public class SinglePicture extends AppCompatActivity {
    private ImageView mImageView;
    private ImageView mail;
    private float x1,x2;
    String num; // stores the phone number for the MMS
    static final int MIN_DISTANCE = 200;
    ArrayList<String> contactsNames = new ArrayList<String>();
    ArrayList<String> contactsNumbers = new ArrayList<String>();
    Spinner spinner;
    ImageButton send;
    ImageView leftArrow;
    ImageView rightArrow;
    VideoView mvideoView;
    ImageView closeButton;
    boolean videoPlaying = false;

    int position;
    ArrayList<String> filesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_single_picture);

        String path = Environment.getExternalStorageDirectory()+"/satefari/";
        File f = new File(path);
        filesList = new ArrayList<String>(Arrays.asList(f.list()));

        leftArrow = (ImageView) findViewById(R.id.left);
        rightArrow = (ImageView) findViewById(R.id.right);

        Bundle b = getIntent().getExtras(); // get id of the picture
        position=0; // default value in case no picture was selected for some reason
        if (b!=null)
            position = b.getInt("key");
        if(position==0)
            leftArrow.setVisibility(INVISIBLE);
        if(position==(filesList.size()-1))
            rightArrow.setVisibility(INVISIBLE);

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setScaleType(FIT_XY);
        mvideoView = (VideoView)findViewById(R.id.video);
        if(!isVideo()) { //if it's an image
            mvideoView.setVisibility(INVISIBLE);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));

        }
        if(isVideo()) { //if it's a video
            mImageView.setVisibility(INVISIBLE);
            mvideoView.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));
            mvideoView.start();
            mvideoView.setPadding(50,50,0,0);
            videoPlaying=true;
        }
        mail = (ImageView) findViewById(R.id.mail);

        /* retrieve contact info for the MMS */
        spinner = (Spinner) findViewById(R.id.contacts_spinner);
        send = (ImageButton) findViewById(R.id.sendbutton);
        send.setVisibility(INVISIBLE);
        spinner.setVisibility(INVISIBLE);
        ContentResolver cr = getApplicationContext().getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        contactsNumbers.add(contactNumber);
                        contactsNames.add(contactName);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contactsNames);
        //set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);

        // alphabetical order
        int length = contactsNumbers.size();
        sort(contactsNames);

        /* end of contact retrieving */



        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // faire un truc jeune et dynamique qui bouge
                spinner.setVisibility(VISIBLE);
                send.setVisibility(VISIBLE);
                spinner.animate().translationX(170);
                send.animate().translationX(695);

            }
        });

        closeButton=(ImageButton) findViewById(R.id.imgClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                String item = (String) adapterView.getItemAtPosition(position);
                int idPhone = contactsNames.indexOf(item);

                if (item != null) {
                    num = contactsNumbers.get(idPhone);
                    //send.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /* does nothing but I have to write it */

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f=new File(Environment.getExternalStorageDirectory()+"/satefari/"+ filesList.get(position));
                Intent sendIntent =new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra("address",num);
                sendIntent.putExtra("sms_body","");
                sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(f));
                if(!isVideo())
                    sendIntent.setType("image/jpg");
                else
                    sendIntent.setType("video/mp4");
                startActivity(sendIntent);

                spinner.setVisibility(INVISIBLE);
                send.setVisibility(INVISIBLE);
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvideoView.setVisibility(INVISIBLE);
                mImageView.setVisibility(INVISIBLE);
                if(videoPlaying) {
                    mvideoView.stopPlayback();
                    videoPlaying=false;
                }
                position-=1;
                if(!isVideo()) {
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));
                    mImageView.setVisibility(VISIBLE);
                }
                else{
                    mvideoView.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));
                    mvideoView.setVisibility(VISIBLE);
                    mvideoView.start();
                    videoPlaying=true;
                }
                if(position==0)
                    leftArrow.setVisibility(INVISIBLE);
                if(position<(filesList.size()-1))
                    rightArrow.setVisibility(VISIBLE);
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvideoView.setVisibility(INVISIBLE);
                mImageView.setVisibility(INVISIBLE);
                if(videoPlaying){
                    mvideoView.stopPlayback();
                    videoPlaying=false;
                }
                position+=1;
                if(!isVideo()) {
                    mImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));
                    mImageView.setVisibility(VISIBLE);
                }
                else{
                    mvideoView.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory() + "/satefari/" + filesList.get(position)));
                    mvideoView.setVisibility(VISIBLE);
                    mvideoView.start();
                    videoPlaying=true;
                }
                if(position==(filesList.size()-1))
                    rightArrow.setVisibility(INVISIBLE);
                if(position >0)
                    leftArrow.setVisibility(VISIBLE);
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            x1 = event.getX();
        if (event.getAction() == MotionEvent.ACTION_UP){
            x2 = event.getX();
            float dX = x2 - x1;
            if (dX > MIN_DISTANCE && position >0) {
                leftArrow.performClick();
            }
            else if (dX < - MIN_DISTANCE && position < (filesList.size()-1)) {
                rightArrow.performClick();
            }
        }
        return super.onTouchEvent(event);
    }

    public boolean isVideo(){
        if(filesList.get(position).toLowerCase().endsWith(".mp4"))
            return true;
        else
            return false;
    }

    public void sort(ArrayList<String> inputArr) {

        if (inputArr == null || inputArr.size() == 0) {
            return;
        }
        this.contactsNames = inputArr;
        int length = contactsNames.size();
        quickSort(0, length - 1);
    }

    private void quickSort(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        String pivot = contactsNames.get(lowerIndex+(higherIndex-lowerIndex)/2);
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (contactsNames.get(i).compareToIgnoreCase(pivot)<0) {
                i++;
            }
            while (contactsNames.get(j).compareToIgnoreCase(pivot)>0) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }

    private void exchangeNumbers(int i, int j) {
        String temp = contactsNames.get(i);
        contactsNames.set(i,contactsNames.get(j));
        contactsNames.set(j,temp);

        temp = contactsNumbers.get(i);
        contactsNumbers.set(i,contactsNumbers.get(j));
        contactsNumbers.set(j,temp);
    }

    public int getPx(int dp){ // cette fonction sert à "convertir" les dp en px car setX/Y ne prennent que des pixels, or nous voudrions des valeurs absolues
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,   getResources().getDisplayMetrics());
    }

}
