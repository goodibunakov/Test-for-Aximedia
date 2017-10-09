package ru.goodibunakov.testaximedia;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by GooDi on 08.10.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<File> photos = Collections.emptyList();
    Context context;

    public RecyclerAdapter(List<File> photos) {
        this.photos = photos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView iv;

        public ViewHolder(View v) {
            super(v);
            iv = (ImageView) v.findViewById(R.id.iv_photo);
            iv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Onclick", "Onclick");
            Cursor cursor = null;
            String choosenPhotoPath;

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                File file = photos.get(position);
                Intent intent = new Intent(v.getContext(), DrawActivity.class);
                intent.putExtra("file", file);
                v.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        Log.d("onCreateViewHolder", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_images, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        ImageView imageView = holder.iv;
        Glide.with(context)
                .load(photos.get(position))
                .into(imageView);

        Log.d("Uri.fromFile", Uri.fromFile(photos.get(position)).toString());
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}