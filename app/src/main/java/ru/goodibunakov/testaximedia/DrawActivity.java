package ru.goodibunakov.testaximedia;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by GooDi on 07.10.2017.
 */

public class DrawActivity extends AppCompatActivity {

    static Boolean wasDrawing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_draw);

        File file = new File(getIntent().getStringExtra("file_path"));

        ImageView editImage = (ImageView) findViewById(R.id.edit_image);
        RectDrawView rectDrawView = (RectDrawView) findViewById(R.id.rects_draw);

        Glide.with(this)
                .load(Uri.fromFile(file))
                .into(editImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wasDrawing) {
            AlertDialog.Builder ad = new AlertDialog.Builder(DrawActivity.this);
            ad.setTitle(DrawActivity.this.getResources().getString(R.string.dialog_title_save));  // заголовок
            ad.setMessage(DrawActivity.this.getResources().getString(R.string.dialog_message_save)); // сообщение
            ad.setPositiveButton(DrawActivity.this.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

//                File fileForDelete = photos.get(position);
//                File deleteFrom = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/myAppImages/");
//                File[] files = deleteFrom.listFiles();
//                for (File file : files) {
//                    //удаляем файл с диска (с телефона, из памяти, совсем)
//                    if (file.equals(fileForDelete)) {
//                        file.delete();
//                    }
//                }
//                //удаляем фотку из List, из которого берет данные адаптер
//                photos.remove(position);
//                //говорим адаптеру что фотка удалена
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, photos.size());

                    Toast.makeText(DrawActivity.this, "Изображение сохранено",
                            Toast.LENGTH_SHORT).show();
                }
            });
            ad.setNegativeButton(DrawActivity.this.getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                    try {
                        DrawActivity.this.finish();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
            ad.setCancelable(false);
            AlertDialog deleteDialog = ad.create();
            deleteDialog.show();
        }
    }
}