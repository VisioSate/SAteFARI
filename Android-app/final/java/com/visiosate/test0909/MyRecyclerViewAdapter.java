package com.visiosate.test0909;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * MyRecyclerViewAdapter provides a RecyclerViewAdapter that will fit our images in a grid according to the file list
 * This approach is supposed to provide us with a relatively fast and responsive grid
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private String[] mData = new String[0];
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever(); /* used to create video preview */

    /**
     *
     * @param context
     * @param data our file list
     */
    public MyRecyclerViewAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    /**
     * inflates the cell layout
     * @param parent
     * @param viewType the type of view we want to insert in our grid (always an image)
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * binds each cell to one of our images
     * @param holder our view holder
     * @param position the position of the file in the list
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData[position];
        if(animal.toLowerCase().endsWith(".jpg")) /* if image file */
        {
            //holder.myImageView.setLayoutParams(new RecyclerView.LayoutParams(250,250));
            holder.myImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //holder.myImageView.setPadding(10,10,10,10);
            holder.myImageView.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/satefari/" + animal));
        }
        else if (animal.toLowerCase().endsWith(".mp4")){ /* if video file, create thumbnail from a frame in the video */
            retriever.setDataSource(mContext, Uri.parse(Environment.getExternalStorageDirectory() +  "/satefari/" + animal));
            Bitmap bmp1 = Bitmap.createBitmap(retriever.getFrameAtTime(10000));
            holder.myImageView.setImageBitmap(bmp1);
        }
    }

    /**
     * Convenience function to get the number of cells
     * @return total number of cells
     */
    @Override
    public int getItemCount() {
        return mData.length;
    }

    /**
     * Stores and recycles views when they go out of the screen - improves performance
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView myImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    /**
     * Used to catch the click events
     * @param itemClickListener our click listener
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Used by the parent activity to detect click events
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}