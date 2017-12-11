package com.example.jgh76.myproject_jjh.function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.activity.ChatActivity;
import com.example.jgh76.myproject_jjh.activity.ZoomActivity;
import com.example.jgh76.myproject_jjh.model.Product;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by jgh76 on 2017-11-27.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>  {

    private List<Product> mProducts;
    private Context mContext;
    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();

    // Pass in the contact array into the constructor
    public ProductsAdapter(Context context, List<Product> products) {
        mProducts = products;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, productView, this);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        Product product = mProducts.get(position);
        final String title = product.getTitle();
        final String price = product.getPrice();
        final String desc = product.getDesc();
        final String location = product.getLocation();
        final String time = product.getElapsedTime();
        final String imageUrl = product.getImageUrl();
        final String uid = product.getUid();

        // Set item views based on your views and data model
        TextView tv_name = viewHolder.tv_pro_name;
        tv_name.setText(title);
        tv_name.setPaintFlags(tv_name.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        TextView tv_price = viewHolder.tv_pro_price;
        tv_price.setText(price+" 원");
        tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        TextView tv_desc = viewHolder.tv_pro_desc;
        tv_desc.setText(desc);

        TextView tv_location = viewHolder.tv_pro_location;
        tv_location.setText(location);

        TextView tv_time = viewHolder.tv_pro_time;
        tv_time.setText(time);

        final ImageView iv_product = viewHolder.imageView;
        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl(imageUrl);

        // Firebase UI 사용
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(iv_product);

        Button btn_contact = viewHolder.btn_contact;
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("price",price+" 원");
                intent.putExtra("imageUrl",imageUrl);
                intent.putExtra("destinationUid", uid);
                mContext.startActivity(intent);
            }
        });

        /*  기존 방법
        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv_product.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Product Adapter","getBytes Failed");
            }
        });
        */
    }



    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void removeItem(int p) {
        mProducts.remove(p);
        notifyItemRemoved(p);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_pro_name;
        TextView tv_pro_price;
        TextView tv_pro_location;
        TextView tv_pro_time;
        TextView tv_pro_desc;
        Button btn_contact;

        private ProductsAdapter mProducts;
        private Context context;

        public ViewHolder(final Context context, View itemView, ProductsAdapter mProducts) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.imageView2);
            tv_pro_name = (TextView)itemView.findViewById(R.id.tv_pro_name);
            tv_pro_price = (TextView)itemView.findViewById(R.id.tv_pro_price);
            tv_pro_desc = (TextView)itemView.findViewById(R.id.tv_pro_desc);
            tv_pro_location = (TextView)itemView.findViewById(R.id.tv_pro_location);
            tv_pro_time = (TextView)itemView.findViewById(R.id.tv_pro_time);
            btn_contact = (Button)itemView.findViewById(R.id.btn_contact);

            this.context = context;
            this.mProducts = mProducts;

        }

    }

}
