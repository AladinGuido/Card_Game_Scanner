package com.example.card_game_scanner;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button_ScanButton);

    }
}