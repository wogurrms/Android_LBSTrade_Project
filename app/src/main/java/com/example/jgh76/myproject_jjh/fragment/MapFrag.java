package com.example.jgh76.myproject_jjh.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.activity.ZoomActivity;
import com.example.jgh76.myproject_jjh.model.Product;
import com.example.jgh76.myproject_jjh.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by jgh76 on 2017-11-27.
 */

public class MapFrag extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String myLocation;
    private ArrayList<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_map, container, false);
        mapView = (MapView)layout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        geocoder = new Geocoder(container.getContext(), Locale.KOREA);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this); // mMap 지도 객체에 마커 클릭 리스너 설정
        mMap.setOnInfoWindowClickListener(this);
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myLocation = dataSnapshot.getValue(String.class);
                moveMyLocation(myLocation, "내 위치");
                getProductsOnMap();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        LatLng SEOUL = new LatLng(37.56, 126.97);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(SEOUL);
//        markerOptions.title("서울");
//        markerOptions.snippet("수도");
//        mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    public void getProductsOnMap(){
        FirebaseDatabase.getInstance().getReference("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){


                    Product product = snapshot.getValue(Product.class);

                    if(product.getUid().equals(uid)){
                        continue;
                    }
                    Log.d("Location", product.getLocation());
                    toLatLng(product.getLocation(), product.getTitle(), product.getPrice());
                    products.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }



    public void moveCamera(double latitude, double longitude, String markMsg){
        LatLng currentLoc = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
        mMap.addMarker(new MarkerOptions().position(currentLoc).title(markMsg));
        CircleOptions circle = new CircleOptions().center(currentLoc) //원점
                .radius(500)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#46c5c1ff")); //배경색

        mMap.addCircle(circle);


    }


    public void moveMyLocation(String myLocation, String markerMsg)  {
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(myLocation, 1);
            if(addresses.size() > 0){
                Address bestResult = (Address) addresses.get(0);
                moveCamera(bestResult.getLatitude(), bestResult.getLongitude(), markerMsg);
            }
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using GeoCoder", e);
            return ;
        }
    }

    public void toLatLng(String myLocation, String markerMsg, String price)  {
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(myLocation, 1);
            if(addresses.size() > 0){
                Address bestResult = (Address) addresses.get(0);
                LatLng currentLoc = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(currentLoc).title(markerMsg).snippet(price+" 원"));
                marker.showInfoWindow();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using GeoCoder", e);
            return ;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for(Product product : products){
            if(marker.getTitle().equals(product.getTitle())){
                Intent intent = new Intent(getContext(), ZoomActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

        }
    }
}
