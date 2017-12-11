package com.example.jgh76.myproject_jjh.activity;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jgh76.myproject_jjh.*;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SignActivity extends AppCompatActivity {

    private final String TAG = "SignActivity";
    private static final int RC_LOCATION = 1;
    private static final int RC_LOCATION_UPDATE = 2;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private Uri mImageCaptureUri;

    @BindView(com.example.jgh76.myproject_jjh.R.id.et_email2)
    EditText et_email2;

    @BindView(R.id.et_password2)
    EditText et_password2;

    @BindView(R.id.et_location)
    EditText et_location;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @OnClick(R.id.iv_profile)
    public void onProfileImageClicked(){
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소",cancelListener)
                .show();
    }

    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시 파일경로 생성
        String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction(){
        // 앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return ;
        switch(requestCode){
            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectX", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            case PICK_FROM_ALBUM:
                mImageCaptureUri = data.getData();

                Intent intent2 = new Intent("com.android.camera.action.CROP");
                intent2.setDataAndType(mImageCaptureUri, "image/*");

                intent2.putExtra("outputX", 200);
                intent2.putExtra("outputY", 200);
                intent2.putExtra("aspectX", 1);
                intent2.putExtra("aspectX", 1);
                intent2.putExtra("scale", true);
                intent2.putExtra("return-data", true);
                startActivityForResult(intent2, CROP_FROM_IMAGE);

                break;
            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK)
                    return;
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tmp/"+System.currentTimeMillis()+".jpg";
                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                    iv_profile.setImageBitmap(photo);

                    break;
                }
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                    f.delete();
                break;
        }
    }

    @OnClick(R.id.btn_signup2)
    public void onSign2Clicked(){
        if(isEmptyEditField()){
            Toast.makeText(getApplicationContext(), "You should fill field", Toast.LENGTH_SHORT).show();
        }else {

            final String email = et_email2.getText().toString();
            String password = et_password2.getText().toString();
            final String location = et_location.getText().toString();
            final String name = et_name.getText().toString();
            final String phone = et_phone.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sign Up Failed",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Sign Up Success",Toast.LENGTH_SHORT).show();
                        final String uid = task.getResult().getUser().getUid();

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        final String profilename = mAuth.getCurrentUser().getEmail()+"'s profile.jpg";
                        //storage 주소와 폴더 파일명을 지정해 준다.
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://jjh-project-1b666.appspot.com").child("profiles/" + profilename);
                        //올라가거라...
                        storageRef.putFile(mImageCaptureUri)
                                //성공시
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

                                        String key = uid;
                                        User user = new User(uid,phone,location,name,taskSnapshot.getDownloadUrl().toString());
                                        database.getReference("users").child(key).setValue(user);
                                    }
                                })
                                //실패시
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        finish();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_location)
    public void onLocationClicked(){
        getLastLocation();
    }

    public void toAddress(Location mLastLocation){
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    mLastLocation.getLatitude(),
                    mLastLocation.getLongitude(),1);

            if (addresses.size() >0) {
                Address address = addresses.get(0);
                et_location.setText(String.format("%s %s %s",
                        address.getLocality(),
                        address.getThoroughfare(),
                        address.getFeatureName()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION) // automatically re-invoked after getting the permission
    public void getLastLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already Have permissions.
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            mLastLocation = task.getResult();
                            toAddress(mLastLocation);
                        }
                    });
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,
                    "위치 정보를 필요로 하는 서비스입니다.",
                    RC_LOCATION, perms);
        }
    }

    public boolean isEmptyEditField(){
        if(et_email2.getText().toString().isEmpty() || et_password2.getText().toString().isEmpty()
                || et_location.getText().toString().isEmpty() || et_name.getText().toString().isEmpty() || et_phone.getText().toString().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
