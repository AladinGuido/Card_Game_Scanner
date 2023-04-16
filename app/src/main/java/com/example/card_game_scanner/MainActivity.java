package com.example.card_game_scanner;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    //Set views
    Button buttonStartScanning;

    //Set possible activities
    Intent scannerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get views
        buttonStartScanning = findViewById(R.id.button_ScanButton);

        //Get possible activities
        scannerActivity = new Intent(this, ScannerActivity.class);


        buttonStartScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(scannerActivity);
            }
        });
    }
}