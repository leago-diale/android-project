package com.example.myapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class OCRActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageview;
    Button btnProcess, cameraBtn;
    TextView txtView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_acrivity);

        imageview = findViewById(R.id.image_view);
        btnProcess = findViewById(R.id.btnProcess);
        cameraBtn = findViewById(R.id.cameraBtn);
        txtView = findViewById(R.id.txtView);
        imageview.setImageBitmap(bitmap);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!txtRecognizer.isOperational()) {
                    txtView.setText("Detector dependencies are not yet available");
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray items = txtRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append("/");
                        for (Text line : item.getComponents()) {
                            //extract scanned text lines here
                            Log.v("lines", line.getValue());
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                Log.v("element", element.getValue());

                            }
                        }
                    }
                    txtView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap src = (Bitmap) extras.get("data");
            bitmap = src;
            imageview.setImageBitmap(src);
        }
    }

}
