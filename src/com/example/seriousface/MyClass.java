package com.example.seriousface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;
import java.io.IOException;

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
                    Bitmap bm = ImageHelper.decodeImagePath(mImagePath, mImageView.getWidth(), mImageView.getHeight());

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
}