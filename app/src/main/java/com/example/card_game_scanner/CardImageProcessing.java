package com.example.card_game_scanner;

import android.util.Log;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CardImageProcessing {

    public Mat preProcessImage(CameraBridgeViewBase.CvCameraViewFrame frame)
    {
        Mat thresholdImage = new Mat();
        Imgproc.cvtColor(frame.rgba(), thresholdImage, Imgproc.COLOR_BGR2GRAY);

        Size kernelSize = new Size(5, 5);
        Imgproc.GaussianBlur(thresholdImage, thresholdImage, kernelSize, 0);

        Imgproc.Canny(thresholdImage, thresholdImage, 100, 100);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(thresholdImage, thresholdImage, kernel, new Point(-1,-1), 2);

        return thresholdImage;
    }

    public static MatOfPoint2f reorder(MatOfPoint2f matOfPoints2f) {
        MatOfPoint2f returnMatOfPoints = new MatOfPoint2f();
        double sum = 0;

        TreeMap<Double, Point> treeMap = new TreeMap<Double, Point>();
        if (matOfPoints2f != null){
            for(Point xy: matOfPoints2f.toList())
            {
                treeMap.put((xy.x + xy.y), xy);
                sum += xy.x;
                sum += xy.y;
            }

            //Remove Highest value from the treemap
            Point highestVal = treeMap.get(treeMap.firstKey());
            treeMap.remove(treeMap.firstKey());


            //Remove Lowest value from the treemap
            Point lowestVal = treeMap.get(treeMap.lastKey());
            treeMap.remove(treeMap.lastKey());

            Point p2 = new Point();
            Point p3 = new Point();


            for (Double key: treeMap.keySet())
            {
                double xDiff = lowestVal.x - treeMap.get(key).x;
                double yDiff = lowestVal.x - treeMap.get(key).y;
                double newSum = xDiff + yDiff;

                if (newSum < sum)
                {
                    p3 = p2;
                    p2 = treeMap.get(key);
                    sum = newSum;
                }
                else
                {
                    p3 = treeMap.get(key);
                }
            }

            List<Point> toCreateMatofPoints = new ArrayList<>();
            toCreateMatofPoints.add(highestVal);
            toCreateMatofPoints.add(p2);
            toCreateMatofPoints.add(p3);
            toCreateMatofPoints.add(lowestVal);
            Log.d("test", "toCreateMatofPoints: " + toCreateMatofPoints);
            returnMatOfPoints.fromList(toCreateMatofPoints);

            }
        return returnMatOfPoints;
    }

    public MatOfPoint2f getContours(Mat image)
    {
        double maxArea = 0;
        MatOfPoint2f largestMatOfPoint = null;

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        for (MatOfPoint contour: contours)
        {
            double area = Imgproc.contourArea(contour);
            if (area > 500)
            {
                MatOfPoint2f contour2f = new MatOfPoint2f();
                contour.convertTo(contour2f, CvType.CV_32F);
                double arcLength = Imgproc.arcLength(contour2f, true);

                MatOfPoint2f approxCurve = new MatOfPoint2f();
                Imgproc.approxPolyDP(contour2f, approxCurve, (0.01 * arcLength), true);

                if(area > maxArea && approxCurve.toList().size() == 4) {
                    //Log.d("approx", "Size: " + approxCurve.toList());
                    largestMatOfPoint = approxCurve;
                    maxArea = area;
                }

            }
        }

        return  largestMatOfPoint;
    }

    public Mat warpImage(Mat unprocessedImage, MatOfPoint2f largestMatOfPoint)
    {
        Mat outputMat = Mat.zeros(unprocessedImage.rows(), unprocessedImage.cols(), unprocessedImage.type());;
        Point[] sourcePoints = largestMatOfPoint.toArray();

        Point[] destPoints = new Point[4];
        destPoints[0] = new Point(0, 0);
        destPoints[1] = new Point(unprocessedImage.cols(), 0);
        destPoints[2] = new Point(unprocessedImage.cols(), unprocessedImage.rows());
        destPoints[3] = new Point(0, unprocessedImage.rows());

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(new MatOfPoint2f(sourcePoints), new MatOfPoint2f(destPoints));

        // Warp the original image using the transformation matrix
        Imgproc.warpPerspective(unprocessedImage, outputMat, perspectiveTransform, outputMat.size());


        return outputMat;
    }

}
