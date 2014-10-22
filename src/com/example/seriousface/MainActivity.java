package com.example.seriousface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener{

	LinearLayout mBottomMenu;
	RelativeLayout mShowMenuButton;
	ImageView mContentImage;
	
	private MyClass myObject;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.camera_btn).setOnClickListener(this);
        mShowMenuButton =(RelativeLayout) findViewById(R.id.show_menu_btn);
        mBottomMenu = (LinearLayout) findViewById(R.id.bottom_menu);
        mShowMenuButton.setOnClickListener(this);
        mContentImage = (ImageView) findViewById(R.id.content_image);
        mContentImage.setOnClickListener(this);
        
        myObject = new MyClass(this, mContentImage);
    }
    
    @Override
    public void onClick(android.view.View v) {
    	switch (v.getId()) {
		case R.id.camera_btn:
			myObject.sendCaptureIntent();
			break;
			
		case R.id.show_menu_btn:
			mShowMenuButton.setVisibility(View.GONE);
			mBottomMenu.setVisibility(View.VISIBLE);
			break;
			
		case R.id.content_image:
			mShowMenuButton.setVisibility(View.VISIBLE);
			mBottomMenu.setVisibility(View.GONE);
			break;
		default:
			break;
		}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	myObject.handleIntent(requestCode, resultCode, data);
    }
}