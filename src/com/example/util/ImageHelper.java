package com.example.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {

    public static Bitmap decodeImagePath(String imagePath, int targetW, int targetH) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            float ratio = ((float) photoW) / photoH;
            int w;

            int tmpW = (int) (targetH * ratio);
            int tmpH = (int) (targetW / ratio);

            if (tmpH > targetH) {
                w = tmpW;
            } else {
                w = targetW;
            }

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = photoW/w;
            bmOptions.inPurgeable = true;
            bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(imagePath, bmOptions);
        }

        return null;
    }

    @SuppressLint("SimpleDateFormat") 
    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(new Date());
        String imageFileName = "Photo " + timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Cogini HackDay 2014");

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                throw new IOException();
            }
        } else if (!storageDir.isDirectory()){
            throw new IOException();
        }

        // mPath = image.getAbsolutePath();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static void addImageToSystemGallery(String imagePath, Activity activity) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        mediaScanIntent.setData(Uri.fromFile(f));
        activity.sendBroadcast(mediaScanIntent);
    }

    public static String getImagePathFromUri(Activity activity, Uri contentUri) {

        Cursor cursor = activity.getContentResolver()
                .query(contentUri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }
}