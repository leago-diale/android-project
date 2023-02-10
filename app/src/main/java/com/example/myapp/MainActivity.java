package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addItem = findViewById(R.id.button);
        TextInputEditText name = (TextInputEditText) findViewById(R.id.nameField);
        ListView listView = (ListView) findViewById(R.id.mobile_list);

        ArrayList<String> mobileArray = new ArrayList<String>();
        mobileArray.add("test3");
        mobileArray.add("test4");
        mobileArray.add("test5");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listview_activity, mobileArray);
        listView.setAdapter(adapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println(name.getText().toString());
                mobileArray.add(0, name.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent);
                //Intent intent = new Intent(context, SendMessage.class);
                //String message = "abcpqr";
                //intent.putExtra(EXTRA_MESSAGE, message);
                //startActivity(intent);
            }
        });
    }
}