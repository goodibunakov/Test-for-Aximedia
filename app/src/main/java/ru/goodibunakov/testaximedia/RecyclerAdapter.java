package ru.goodibunakov.testaximedia;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView iv;

        public ViewHolder(View v) {
            super(v);
            iv = (ImageView) v.findViewById(R.id.iv_photo);
            iv.setOnClickListener(this);
            iv.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                File file = photos.get(position);
                Intent intent = new Intent(v.getContext(), DrawActivity.class);
                intent.putExtra("file_path", file.getAbsolutePath());
                v.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle(context.getResources().getString(R.string.dialog_title));  // заголовок
                ad.setMessage(context.getResources().getString(R.string.dialog_message)); // сообщение
                ad.setPositiveButton(context.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        File fileForDelete = photos.get(position);
                        File deleteFrom = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/myAppImages/");
                        File[] files = deleteFrom.listFiles();
                        for (File file : files) {
                            //удаляем файл с диска (с телефона, из памяти, совсем)
                            if (file.equals(fileForDelete)) {
                                file.delete();
                            }
                        }
                        //удаляем фотку из List, из которого берет данные адаптер
                        photos.remove(position);
                        //говорим адаптеру что фотка удалена
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, photos.size());

                        Toast.makeText(context, "Изображение удалено",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                ad.setNegativeButton(context.getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Toast.makeText(context, "Пусть останется ;)", Toast.LENGTH_LONG)
                                .show();
                    }
                });
                ad.setCancelable(false);
                AlertDialog deleteDialog = ad.create();
                deleteDialog.show();
            }
            return false;
        }
    }
}