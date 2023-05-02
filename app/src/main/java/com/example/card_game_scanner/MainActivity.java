package com.example.card_game_scanner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

        getCameraPermissions();
        getStoragePermissions();
    }

    void getCameraPermissions()
    {
        Log.d("Permissions", "Check Camera Permissions");
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
        else
        {
            Log.d("Permissions", "Camera Permissions Granted");
        }
    }

    void getStoragePermissions()
    {
        Log.d("Permissions", "Check Write Permissions");
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
        else
        {
            Log.d("Permissions", "Storage Write Permissions Granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED){
            if (requestCode == 101) {
                getCameraPermissions();
            } else if (requestCode == 102) {
                getStoragePermissions();
            }
        }
    }
}