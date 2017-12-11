package com.example.jgh76.myproject_jjh.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.jgh76.myproject_jjh.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ZoomActivity extends AppCompatActivity {
    String filename;
    ImageView iv_pro_zoom;
    FirebaseStorage mFirebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

//        Intent intent = getIntent();
//        filename = intent.getStringExtra("filename");

        iv_pro_zoom = (ImageView)findViewById(R.id.iv_pro_zoom);
//        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl("gs://jjh-project-1b666.appspot.com").child("images/"+filename);
//
//        // Firebase UI 사용
//        Glide.with(this)
//                .using(new FirebaseImageLoader())
//                .load(storageRef)
//                .into(iv_pro_zoom);
    }
}
