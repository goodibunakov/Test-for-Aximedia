package ru.goodibunakov.testaximedia;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by GooDi on 07.10.2017.
 */

public class DrawActivity extends AppCompatActivity {

    static File file;
    RectDrawView rectDrawView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_draw);

        file = new File(getIntent().getStringExtra("file_path"));
        rectDrawView = (RectDrawView) findViewById(R.id.rects_draw);
        rectDrawView.setDrawingCacheEnabled(true);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(DrawActivity.this);
        ad.setTitle(DrawActivity.this.getResources().getString(R.string.dialog_title_save));  // заголовок
        ad.setMessage(DrawActivity.this.getResources().getString(R.string.dialog_message_save)); // сообщение
        ad.setPositiveButton(DrawActivity.this.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/myAppImages/");
                File newFile = new File(path, "_" + System.currentTimeMillis() / 1000 + ".jpg");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    Bitmap bm = rectDrawView.getDrawingCache(true);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(DrawActivity.this,
                            "Изображение сохранено",
                            Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(DrawActivity.this,
                            "Что-то не так: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(DrawActivity.this,
                            "Что-то не так: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                DrawActivity.this.finish();
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
        ad.create().show();
    }
}