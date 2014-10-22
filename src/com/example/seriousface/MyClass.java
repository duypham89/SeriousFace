package com.example.seriousface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.example.util.ImageHelper;
import com.example.view.SFImageView;

public class MyClass {

    private Activity mActivity;
    private SFImageView mImageView;

    private String mImagePath;
    private FaceDetector mFaceDetector;
    private FaceDetector.Face[] mFaces;

    private Paint mPaint;

    private static final int TAKE_PHOTO = 1;
    private static final int MAX_NUM_OF_FACES = 10;

    public MyClass(Activity activity, SFImageView imageView) {
        mActivity = activity;
        mImageView = imageView;
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    public void sendCaptureIntent() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = ImageHelper.createImageFile();
            if (photoFile != null) {
                mImagePath = photoFile.getAbsolutePath();
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                mActivity.startActivityForResult(takePhotoIntent, TAKE_PHOTO);
            }
        } catch (IOException ex) {
            // Handle error occurred while creating the File
        }
    }

    public void handleIntent(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    Bitmap tmpBm = ImageHelper.decodeImagePath(mImagePath, mImageView.getWidth(), mImageView.getHeight());
                    Bitmap bm = convertToMutable(tmpBm);
                    
                    mFaces = new FaceDetector.Face[MAX_NUM_OF_FACES];
                    mFaceDetector = new FaceDetector(bm.getWidth(), bm.getHeight(), MAX_NUM_OF_FACES);
                    int numOfFacesDetected = mFaceDetector.findFaces(bm, mFaces);
                    drawRects(bm, numOfFacesDetected, mFaces);
                    mImageView.setImageBitmap(bm);
                    mImageView.setFace(mFaces, numOfFacesDetected, bm);
                    break;
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    new File(mImagePath).delete();
                    break;
            }
        }
    }

    private void drawRects(Bitmap bm, int numOfFaces, FaceDetector.Face[] faces) {
        Canvas canvas = new Canvas(bm);

        for (int i = 0; i < numOfFaces; i++) {
            FaceDetector.Face face = faces[i];
            PointF midPoint = new PointF();

            face.getMidPoint(midPoint);
            float eyesDistance = face.eyesDistance();

            canvas.drawRect((int) (midPoint.x - eyesDistance),
                    (int) (midPoint.y - eyesDistance),
                    (int) (midPoint.x + eyesDistance),
                    (int) (midPoint.y + eyesDistance), mPaint);
        }
    }

    public FaceDetector.Face[] getFaces() {
        return mFaces;
    }
    
    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes. 
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available. 
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary 
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 

        return imgIn;
    }
}