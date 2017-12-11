package com.example.jgh76.myproject_jjh.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.fragment.ChatFrag;
import com.example.jgh76.myproject_jjh.fragment.ListFrag;
import com.example.jgh76.myproject_jjh.fragment.MapFrag;
import com.example.jgh76.myproject_jjh.function.BackPressCloseHandler;
import com.example.jgh76.myproject_jjh.function.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NaviActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fm;
    private BackPressCloseHandler backPressCloseHandler;
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    fm.beginTransaction().replace(R.id.content, new MapFrag()).commit();
                    View view = getSupportActionBar().getCustomView();
                    TextView title = (TextView)view.findViewById(R.id.mytext);
                    title.setText("지도로 보기");
                    Toast.makeText(getApplicationContext(), "Map Fragment", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_list:
                    fm.beginTransaction().replace(R.id.content, new ListFrag()).commit();
                    view = getSupportActionBar().getCustomView();
                    title = (TextView)view.findViewById(R.id.mytext);
                    title.setText("리스트로 보기");

                    Toast.makeText(getApplicationContext(), "List Fragment", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_chat:
                    fm.beginTransaction().replace(R.id.content, new ChatFrag()).commit();
                    view = getSupportActionBar().getCustomView();
                    title = (TextView)view.findViewById(R.id.mytext);
                    title.setText("채팅");
                    return true;
                case R.id.navigation_post:
                    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_logout:
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        setContentView(R.layout.activity_navi);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(this,"Welcome "+user.getEmail(),Toast.LENGTH_SHORT).show();

        backPressCloseHandler = new BackPressCloseHandler(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        BottomNavigationViewHelper.removeTextLabel(navigation, R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MapFrag mapFrag = new MapFrag();
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content, mapFrag).commit();

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
