package com.example.card_game_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

public class ScannerActivity extends CameraActivity {

    CameraBridgeViewBase cameraBridgeViewBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        cameraBridgeViewBase = findViewById(R.id.cameraView);

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                Mat imageOutput = inputFrame.rgba();
                Imgproc.cvtColor(imageOutput, imageOutput, Imgproc.COLOR_BGR2GRAY);

                Mat blurredImage = new Mat();
                Size kernelSize = new Size(15, 15);
                Imgproc.GaussianBlur(imageOutput, blurredImage, kernelSize, 0);

                int img_w = imageOutput.cols(); // Get image width
                int img_h = imageOutput.rows(); // Get image height
                int bkg_level = (int)imageOutput.get(img_h / 100, img_w / 2)[0]; // Get pixel value at 1% of image height and center of image width
                int BKG_THRESH = 50; // Define threshold value
                int thresh_level = bkg_level + BKG_THRESH; // Calculate threshold level


                Mat thresh = new Mat();
                Core.inRange(blurredImage, new Scalar(thresh_level), new Scalar(255), thresh);



                return getImageThreshold(inputFrame);
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

    Mat getImageThreshold(CameraBridgeViewBase.CvCameraViewFrame frame)
    {
        int BACKGROUND_THRESHOLD = 50;


        Mat grayImage = new Mat();
        Imgproc.cvtColor(frame.rgba(), grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat blurredImage = new Mat();
        Size kernelSize = new Size(15, 15);
        Imgproc.GaussianBlur(grayImage, blurredImage, kernelSize, 0);

        int imageWidth = grayImage.cols();
        int imageHeight = grayImage.rows();
        int backgroundLevel = (int) grayImage.get(imageHeight / 100, imageWidth / 2)[0];
        int thresholdValue = backgroundLevel + BACKGROUND_THRESHOLD;

        Mat thresholdedImage = new Mat();
        Imgproc.threshold(blurredImage, thresholdedImage, thresholdValue, 255, Imgproc.THRESH_BINARY);

        return thresholdedImage;
    }
}