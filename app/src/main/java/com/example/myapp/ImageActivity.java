package com.example.myapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    Button submit = findViewById(R.id.submit);
    ImageView image = findViewById(R.id.imageView);
    EditText text = findViewById(R.id.editText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view ) {
                //System.out.println(text.getText().toString());
            }
        });

    }

}
