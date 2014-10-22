package com.example.view;

import java.util.ArrayList;
import java.util.Random;

import com.example.model.SFFace;
import com.example.seriousface.Constants;

import android.annotation.SuppressLint;
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
	FaceDetector.Face[] mFaces = null;
	public SFImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SFImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SFImageView(Context context) {
		super(context);
	}
	
	public void setFace(FaceDetector.Face[] faces) {
		mFaces = faces;
	}

	public void drawFace(int index) {
		mFaceIndex = index;
		invalidate();
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mFaceIndex != -1 && mFaces != null) {
			Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setColor(0xffff0000);
			mPaint.setStrokeWidth(3);
			
//			canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/4, mPaint);
			for (FaceDetector.Face f : mFaces) {
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
				face = BitmapFactory.decodeResource(getResources(), Constants.TROLL_FACE_LIST[index]);
				float radius = f.eyesDistance();
				face = Bitmap.createScaledBitmap(face, (int) (radius*1.5), (int) (radius*1.7), true);
				canvas.drawBitmap(face, mid.x - face.getWidth()/2, mid.y -face.getHeight()/2, mPaint);
			}
		}
	}
	
}
