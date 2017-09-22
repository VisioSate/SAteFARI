package com.visiosate.test0909;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class will create and display a grid with all of our pictures and videos; it uses the MyRecyclerViewAdapter class in order to do so
 */
public class ListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);

        /* get the file list */
        String path = Environment.getExternalStorageDirectory()+"/satefari/";
        File f = new File(path);
        ArrayList<String> filesList = new ArrayList<String>(Arrays.asList(f.list()));
        String[] data = filesList.toArray(new String[filesList.size()]);

        /* create the grid by calling the MyRecyclerViewAdapter class */
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int numberOfColumns = (int) (new Double(width)/new Double(250));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        /* close button to exit the view */
        closeButton=(ImageButton) findViewById(R.id.imgClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }

    /**
     * Calls the SinglePicture activity to show the picture/video
     * @param view the selected image
     * @param position the number of the image
     */
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ListActivity.this, SinglePicture.class);
        Bundle b = new Bundle();
        b.putInt("key", position);
        intent.putExtras(b);
        startActivity(intent);    }
}


