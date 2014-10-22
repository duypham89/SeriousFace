package com.example.view;

import java.util.Random;

import com.example.seriousface.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SFImageView extends ImageView {

	int mFaceIndex = -1;
	FaceDetector.Face[] mFaces;
	int mNumOfFacesDetected;
	Bitmap mBitmap;
	Bitmap mCurrentBitmap;
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

//	@Override
//	public boolean onTouchEvent(android.view.MotionEvent event) {
//
//		if (mFaces != null && mBitmap != null) {
//			
//		} else {
//			return false;
//		}
//		
//		if(event.getAction() == MotionEvent.ACTION_DOWN) {
//			
//			Rect r = getDrawable().getBounds();
//			int bW = r.width();
//			int bH = r.height();
//			
//			
//			float scaleX = (float) mBitmap.getWidth() / bW;
//			float scaleY = (float) mBitmap.getHeight() / bH;
//			
//			
//			int offSetX = (int)event.getX() - r.left;
//			int offSetY = (int)event.getY() - r.top;
//			
//			int x = (int)(offSetX * scaleX);
//			int y = (int)(offSetY * scaleY);
////			
////			float x = event.getX() * scaleX + scaledOffSetX;
////			float y = event.getY() * scaleY + scaledOffSetY;
//			Log.i("x and y", x + " , " + y);
//			
//			for (int i = 0; i < mNumOfFacesDetected; ++i) {
//
//				FaceDetector.Face f = mFaces[i];
//				if (f == null) {
//					break;
//				}
//				
//				Canvas rCanvas = new Canvas(mCurrentBitmap);
//				rCanvas.drawRect(getRoundingRect1(f), mPaint);
//				rCanvas.drawCircle(x, y, 10, mPaint);
//				
//				if(contain(f, x, y)) {						
//					Bitmap bm = Bitmap.createBitmap(mCurrentBitmap);
//					Canvas canvas = new Canvas(bm);
//					canvas.drawBitmap(mBitmap, getRoundingRect1(f), getRoundingRect1(f), mPaint);
//					
//					int index = -1;
//					Bitmap face = null;
//					Random r = new Random();
//					index = 1 + r.nextInt(6);
//
//					PointF mid = new PointF();
//					f.getMidPoint(mid);
//					int faceSize = (int) (3.4 * f.eyesDistance());
//					face = BitmapFactory.decodeResource(getResources(),
//							Constants.TROLL_FACE_LIST[index]);
//					face = Bitmap.createScaledBitmap(face, faceSize, faceSize,
//							false);
//					canvas.drawBitmap(face, mid.x - face.getWidth() / 2, mid.y
//							- face.getHeight() / 2, mPaint);
//					
//					mCurrentBitmap = bm;
//					this.setImageBitmap(bm);
//					break;
//				}
//				
//				this.setImageBitmap(mCurrentBitmap);
//			}
//		}
//		return true;
//	}

	private Rect getRoundingRect(FaceDetector.Face face) {
		PointF midPoint = new PointF();
		face.getMidPoint(midPoint);
		float eyesDistance = face.eyesDistance();
		
		return new Rect(
				 (int)(midPoint.x - eyesDistance),
				 (int)(midPoint.y - eyesDistance),
				 (int)(midPoint.x + eyesDistance),
				 (int)(midPoint.y + eyesDistance));
	}
	
	private Rect getRoundingRect1(FaceDetector.Face face) {
		PointF midPoint = new PointF();
		face.getMidPoint(midPoint);
		float eyesDistance = face.eyesDistance();
		
		return new Rect(
				 (int)(midPoint.x - eyesDistance * 1.7),
				 (int)(midPoint.y - eyesDistance * 1.7),
				 (int)(midPoint.x + eyesDistance * 1.7),
				 (int)(midPoint.y + eyesDistance * 1.7));
	}
	
	private boolean contain(FaceDetector.Face face, float x, float y) {
		Log.i("Rect contains face", getRoundingRect(face).contains((int)x, (int)y) + "");
        return getRoundingRect1(face).contains((int)x, (int)y);
	}

	public void setFace(FaceDetector.Face[] faces, int numOfFacesDetected,
			Bitmap bitmap) {

		if (faces == null || faces.length <= 0) {
			return;
		}

		mFaces = faces;
		mBitmap = bitmap;
		mCurrentBitmap = bitmap;
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
				face = BitmapFactory.decodeResource(getResources(),
						Constants.TROLL_FACE_LIST[index]);
				face = Bitmap.createScaledBitmap(face, faceSize, faceSize,
						false);
				canvas.drawBitmap(face, mid.x - face.getWidth() / 2, mid.y
						- face.getHeight() / 2, mPaint);
			}

			mCurrentBitmap = tmpBitmap;
			this.setImageBitmap(tmpBitmap);
		}
	}
}