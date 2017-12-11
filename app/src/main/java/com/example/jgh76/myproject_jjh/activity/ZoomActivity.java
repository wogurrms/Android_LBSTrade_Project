package com.example.jgh76.myproject_jjh.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.model.Product;
import com.example.jgh76.myproject_jjh.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ZoomActivity extends AppCompatActivity {

    ImageView iv_zoom_product;
    TextView tv_zoom_title;
    TextView tv_zoom_desc;

    TextView tv_zoom_nickname;
    TextView tv_zoom_location;
    ImageView iv_zoom_profile;
    Button btn_contact;

    String uid;
    User user;
    Product product;

    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        Intent intent = getIntent();
        product = (Product)intent.getSerializableExtra("product");

        uid = product.getUid();
        Log.d("product : " , product.getTitle() + " , " + product.getUid() + " , " + product.getLocation());

        iv_zoom_product = (ImageView)findViewById(R.id.iv_zoom_product);
        tv_zoom_title = (TextView)findViewById(R.id.tv_zoom_title);
        tv_zoom_desc = (TextView)findViewById(R.id.tv_zoom_desc);

        iv_zoom_profile = (ImageView)findViewById(R.id.iv_zoom_profile);
        tv_zoom_nickname = (TextView)findViewById(R.id.tv_zoom_nickname);
        tv_zoom_location = (TextView)findViewById(R.id.tv_zoom_location);

        btn_contact = (Button)findViewById(R.id.btn_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("title", product.getTitle());
                intent.putExtra("price",product.getPrice()+" 원");
                intent.putExtra("imageUrl",product.getImageUrl());
                intent.putExtra("destinationUid", uid);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.d("user : " , user.getName() + " , " + user.getUid() + " , " + user.getLocation());

                tv_zoom_nickname.setText(user.getName());
                tv_zoom_location.setText(user.getLocation());
                StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl(user.getProfileUrl());
                // Firebase UI 사용
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(iv_zoom_profile);

                tv_zoom_title.setText(product.getTitle());
                tv_zoom_desc.setText(product.getDesc());
                storageRef = mFirebaseStorage.getReferenceFromUrl(product.getImageUrl());
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(iv_zoom_product);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
