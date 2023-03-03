package com.example.myapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button submit;
    ImageView image;
    FloatingActionButton camera;
    EditText text;
    Double lat, lon;
    FusedLocationProviderClient fusedLocationClient;
    Bitmap result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        checkAndRequestPermissions();
        submit = (Button) findViewById(R.id.submit);
        image = (ImageView) findViewById(R.id.imageView);
        text = (EditText) findViewById(R.id.editText);
        camera = (FloatingActionButton) findViewById(R.id.actionButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getLocation();
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text.length() == 0){
                    text.setError("This field is required");
                    return;
                }else{
                    File path = new File(Environment.getExternalStorageDirectory(), "/myFolder");
                    File file = new File (path, text.getText().toString()+".jpg");
                    if (!path.exists()) {
                        path.mkdirs();
                        System.out.println("path created");
                    }else{
                        System.out.println("path exists");
                        Log.d("path", file.toString());
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            result.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

    }

    public void getLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            }
        });
    }

    private  boolean checkAndRequestPermissions() {

        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int tel = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int foreground = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (tel != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.READ_PHONE_STATE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.CAMERA);
        }
        if (foreground != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add( Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray (new String[listPermissionsNeeded.size()]), REQUEST_IMAGE_CAPTURE);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = sdf.format(Calendar.getInstance().getTime());
            Bundle extras = data.getExtras();
            Bitmap src = (Bitmap) extras.get("data");
            int w = src.getWidth();
            int h = src.getHeight();
            result = Bitmap.createBitmap(w, h, src.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.drawText(dateTime , 0, 15, new Paint());
            canvas.drawText("latitude: "+lat, 0, h, new Paint());
            canvas.drawText("longitude: "+lon , 0, h-15, new Paint());
            image.setImageBitmap(result);
        }
    }
}
