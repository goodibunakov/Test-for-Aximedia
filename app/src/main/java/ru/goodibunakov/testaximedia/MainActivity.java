package ru.goodibunakov.testaximedia;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 1;
    List<File> inFiles = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    File path = null; // для создания директории куда копируем выбранную фотку
    RecyclerView recyclerView;
    TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/myAppImages/");

        txtEmpty = (TextView) findViewById(R.id.txt_empty);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getAddedPhotos(path);
        Log.d("inFiles.size()", String.valueOf(inFiles.size()));
        recyclerAdapter = new RecyclerAdapter(inFiles);
        recyclerView.setAdapter(recyclerAdapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String realPathFromURI; //путь к выбранной фотке
        String pathTo;

        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                    //получаем URI выбранной фотки из интента
                    final Uri imageUri = data.getData();
                    Log.d("imageUri", imageUri.toString());
                    Log.d("getPath", imageUri.getPath());
                    Log.d("DIRECTORY_PICTURES", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
                    Cursor cursor = null;
                    //получаем путь к фотке из URI
                    try {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        cursor = getContentResolver().query(imageUri, proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        realPathFromURI = cursor.getString(column_index);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    Log.d("realPathFromURI", realPathFromURI);


                    if (isExternalStorageWritable()) {
                        //path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/myAppImages/");
                        if (!path.exists()) {
                            path.mkdir();
                        }
                        pathTo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                                + "/myAppImages/" + imageUri.getLastPathSegment();

                        //копируем фотку
                        Boolean isSuccess = copyFile(realPathFromURI, pathTo);

                        if (isSuccess)
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.copy_successful), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.sdcard_not), Toast.LENGTH_SHORT).show();
                    }

                    getAddedPhotos(path);
                    Log.d("getAddedPhotos", getAddedPhotos(path).toString());
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean copyFile(String from, String to) {
        try {
            File fileFrom = new File(from);
            File fileTo = new File(to);
            InputStream in = new FileInputStream(fileFrom); // Создаем потоки
            OutputStream out = new FileOutputStream(fileTo);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close(); // Закрываем потоки
            out.close();
        } catch (FileNotFoundException ex) {
            // Обработка ошибок
            ex.printStackTrace();
        } catch (IOException e) {
            // Обработка ошибок
            e.printStackTrace();
        }
        recyclerAdapter.notifyDataSetChanged();
        return true; // При удачной операции возвращаем true
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public List<File> getAddedPhotos(File parentDir) {
        inFiles.clear();

        File[] files = parentDir.listFiles();
        for (File file : files) {
            inFiles.add(file);
        }
        return inFiles;
    }

    @Override
    public void onBackPressed() {
    }
}