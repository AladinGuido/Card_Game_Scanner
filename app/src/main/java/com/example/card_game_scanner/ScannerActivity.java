package com.example.card_game_scanner;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class ScannerActivity extends CameraActivity {

    CameraBridgeViewBase cameraBridgeViewBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        cameraBridgeViewBase = findViewById(R.id.cameraView);
        final CardImageProcessing cardImageProcessing = new CardImageProcessing();

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {





            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                Mat outputImage = inputFrame.rgba();
                Mat preProcessImage = cardImageProcessing.preProcessImage(inputFrame);
                MatOfPoint2f largestContour = cardImageProcessing.getContours(preProcessImage);
                if(largestContour != null)
                {
                    Mat toSave = cardImageProcessing.warpImage(inputFrame.rgba(), largestContour, 661, 1028);

                    // Get the directory where you want to save the file
                    //File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    //Imgcodecs.imwrite(directory + "/output.png", toSave);
                    //Log.d("test", "filepath: " + directory + "output.png");

                    MatOfPoint contours = new MatOfPoint();
                    largestContour.convertTo(contours, CvType.CV_32S);

                    Mat drawing = new Mat(outputImage.rows(), outputImage.cols(), CvType.CV_8UC3);
                    drawing.setTo(new Scalar(0, 0, 0)); // Set the drawing color to black
                    Imgproc.drawContours(drawing, Arrays.asList(contours), -1, new Scalar(0, 255, 0), 2); // Draw the contours in green with a thickness of 2

                    Core.addWeighted(outputImage, 1, drawing, 0.5, 0, outputImage);
                }


                return outputImage;
            }
        });

        if(OpenCVLoader.initDebug())
        {
            cameraBridgeViewBase.enableView();
        }

    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraBridgeViewBase.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraBridgeViewBase.disableView();
    }



}