package com.example.seriousface;

import com.example.view.SFImageView;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener{

	public int [] items = {
			R.id.btn_random, 
			R.id.troll_face_1, 
			R.id.troll_face_2,
			R.id.troll_face_3,
			R.id.troll_face_4,
			R.id.troll_face_5,
			R.id.troll_face_6,
			R.id.troll_face_7};
	
	LinearLayout mBottomMenu;
	RelativeLayout mShowMenuButton;
	SFImageView mContentImage;
	
	private MyClass myObject;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.camera_btn).setOnClickListener(this);
        mShowMenuButton =(RelativeLayout) findViewById(R.id.show_menu_btn);
        mBottomMenu = (LinearLayout) findViewById(R.id.bottom_menu);
        mShowMenuButton.setOnClickListener(this);
        mContentImage = (SFImageView) findViewById(R.id.content_image);
        mContentImage.setOnClickListener(this);
        
        myObject = new MyClass(this, mContentImage);
        
        for(int i : items) {
        	findViewById(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(int j = 0; j < 8; ++j)
						if(v.getId() == items[j]) {
							mContentImage.drawFace(j);
							toggleBottomMenu();
						}
					
				}
			});
        }
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
    
    @Override
    public void onClick(android.view.View v) {
    	switch (v.getId()) {
		case R.id.camera_btn:
			myObject.sendCaptureIntent();
			break;
			
		case R.id.show_menu_btn:
			toggleBottomMenu();
			break;
			
		case R.id.content_image:
			
			toggleBottomMenu();
			break;
		default:
			break;
		}
    }
    
    private void toggleBottomMenu() {
    	mShowMenuButton.setVisibility(mShowMenuButton.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
		mBottomMenu.setVisibility(mBottomMenu.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	myObject.handleIntent(requestCode, resultCode, data);
    }
}