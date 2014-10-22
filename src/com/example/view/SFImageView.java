package com.example.view;

import java.util.Random;

import com.example.seriousface.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SFImageView extends ImageView {

	int mFaceIndex = -1;
	FaceDetector.Face[] mFaces;
	int mNumOfFacesDetected;
	Bitmap mBitmap;
	Paint mPaint;
	
	public SFImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SFImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SFImageView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(0xffff0000);
		mPaint.setStrokeWidth(3);
		
		mNumOfFacesDetected = 0;
	}
	
	public void setFace(FaceDetector.Face[] faces, int numOfFacesDetected, Bitmap bitmap) {
		
		if (faces == null || faces.length <= 0) {
			return;
		}
		
		mFaces = faces;
		mBitmap = bitmap;
		mNumOfFacesDetected = numOfFacesDetected;
	}

	public void drawFace(int index) {
		mFaceIndex = index;
		drawMySelf();
	}
	
	private void drawMySelf() {
		
		if (mFaceIndex != -1 && mFaces != null && mBitmap != null) {
			
			Bitmap tmpBitmap = Bitmap.createBitmap(mBitmap);
			Canvas canvas = new Canvas(tmpBitmap);
			
			for (int i = 0; i < mNumOfFacesDetected; ++i) {
			
				FaceDetector.Face f = mFaces[i];
				if (f == null) {
					break;
				}
				
				int index = -1;
				Bitmap face = null;
				if (mFaceIndex != 0) {
					index = mFaceIndex;
				} else {
					Random r = new Random();
					index = 1 + r.nextInt(6);
				}
				
				PointF mid = new PointF();
				f.getMidPoint(mid);
				int faceSize = (int) (3.4 * f.eyesDistance());
				face = BitmapFactory.decodeResource(getResources(), Constants.TROLL_FACE_LIST[index]);
				face = Bitmap.createScaledBitmap(face, faceSize, faceSize, false);
				canvas.drawBitmap(face, mid.x - face.getWidth()/2, mid.y - face.getHeight()/2, mPaint);
			}
			
			this.setImageBitmap(tmpBitmap);
		}
	}	
}