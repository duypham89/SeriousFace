package com.example.seriousface;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


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
	ImageView mContentImage;
	Uri imageUri;
	
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
        
        for(int i : items) {
        	findViewById(i).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(int j = 0; j < 8; ++j) {
						if(v.getId() == items[j]) {
							//TODO: abc
							Log.i("index", j + "");
						} 
					}
				}
			});
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onClick(android.view.View v) {
    	switch (v.getId()) {
		case R.id.camera_btn:
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File photo = new File(Environment.getExternalStorageDirectory(),  "camera_capture.jpg");
		    intent.putExtra(MediaStore.EXTRA_OUTPUT,
		            Uri.fromFile(photo));
		    imageUri = Uri.fromFile(photo);
		    startActivityForResult(intent, 0);
			break;
			
		case R.id.show_menu_btn:
			mShowMenuButton.setVisibility(View.INVISIBLE);
			mBottomMenu.setVisibility(View.VISIBLE);
//			Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
//			mBottomMenu.setAnimation(animation);
//			mBottomMenu.animate();
			break;
			
		case R.id.content_image:
			mShowMenuButton.setVisibility(View.VISIBLE);
			mBottomMenu.setVisibility(View.INVISIBLE);
			break;
			
		case R.id.btn_random:
			break;
		default:
			break;
		}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Activity.RESULT_OK && requestCode == 0) {
    		Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);            
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;
            try {
                 bitmap = android.provider.MediaStore.Images.Media
                 .getBitmap(cr, selectedImage);

                mContentImage.setImageBitmap(bitmap);
                Toast.makeText(this, selectedImage.toString(),
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                        .show();
                Log.e("Camera", e.toString());
            }
    	  }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// automatically handle clicks on the Home/Up button, so long
        // Handle action bar item clicks here. The action bar will
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
