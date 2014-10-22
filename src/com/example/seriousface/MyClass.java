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
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import com.example.util.ImageHelper;

public class MyClass {

    private Activity mActivity;
    private ImageView mImageView;

    private String mImagePath;
    private FaceDetector mFaceDetector;
    private FaceDetector.Face[] mFaces;

    private Paint mPaint;

    private static final int TAKE_PHOTO = 1;
    private static final int MAX_NUM_OF_FACES = 10;

    public MyClass(Activity activity, ImageView imageView) {
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
                    Bitmap myBm = convert(bm, Bitmap.Config.RGB_565);

                    mFaces = new FaceDetector.Face[MAX_NUM_OF_FACES];
                    mFaceDetector = new FaceDetector(myBm.getWidth(), myBm.getHeight(), MAX_NUM_OF_FACES);
                    int numOfFacesDetected = mFaceDetector.findFaces(myBm, mFaces);
                    drawRects(myBm, numOfFacesDetected, mFaces);
                    mImageView.setImageBitmap(myBm);
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

    private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
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