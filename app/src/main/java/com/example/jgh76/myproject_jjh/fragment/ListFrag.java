package com.example.jgh76.myproject_jjh.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jgh76.myproject_jjh.function.DateElapseCalculater;
import com.example.jgh76.myproject_jjh.function.ProductsAdapter;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by jgh76 on 2017-11-27.
 */

public class ListFrag extends Fragment {

    ArrayList<Product> arrayList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProductsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.frag_list, container, false);

        RecyclerView rvProducts = (RecyclerView)myView.findViewById(R.id.recycler);
        arrayList = new ArrayList<Product>();

        // Create adapter passing in the sample user data
        adapter = new ProductsAdapter(this.getContext(), arrayList);

        // Attach the adapter to the recyclerview to populate items
        rvProducts.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvProducts.setLayoutManager(linearLayoutManager);

        // optimizations if all item views are of the same height and width for significantly smoother scrolling:
        rvProducts.setHasFixedSize(true);

        addReadEventListener();

        return myView;
    }

    public void addReadEventListener() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("posts");


        Query products = myRef.limitToFirst(10);
        products.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = "";
                    time = product.getElapsedTime();
                    product.setElapsedTime("2분 전");
//                    Product product = new Product(title, desc, price, fileName, DateElapseCalculater.calculate(time));
                    arrayList.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
