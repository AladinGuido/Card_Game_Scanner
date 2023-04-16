package com.example.card_game_scanner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

        getPermissions();
    }

    void getPermissions()
    {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED){
            getPermissions();
        }
    }
}