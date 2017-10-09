package ru.goodibunakov.testaximedia;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by GooDi on 07.10.2017.
 */

public class DrawActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_draw);

        File file = new File(getIntent().getStringExtra("file_path"));

        ImageView editImage = (ImageView) findViewById(R.id.edit_image);

        Glide.with(this)
                .load(Uri.fromFile(file))
                .into(editImage);
    }
}
