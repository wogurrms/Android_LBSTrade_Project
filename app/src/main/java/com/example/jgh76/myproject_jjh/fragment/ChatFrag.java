package com.example.jgh76.myproject_jjh.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.activity.ChatActivity;
import com.example.jgh76.myproject_jjh.function.ChatAdapter;
import com.example.jgh76.myproject_jjh.function.DateElapseCalculater;
import com.example.jgh76.myproject_jjh.function.ProductsAdapter;
import com.example.jgh76.myproject_jjh.model.Chat;
import com.example.jgh76.myproject_jjh.model.Product;
import com.example.jgh76.myproject_jjh.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jgh76 on 2017-12-10.
 */

public class ChatFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_chat, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rv_friend);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

        return view;
    }


    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<User> userModels;

        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();

                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){


                        User user = snapshot.getValue(User.class);

                        if(user.getUid().equals(myUid)){
                            continue;
                        }
                        userModels.add(user);
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


            Glide.with
                    (holder.itemView.getContext())
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReferenceFromUrl(userModels.get(position).getProfileUrl()))
                    .into(((CustomViewHolder)holder).iv_chat_profile);
            ((CustomViewHolder)holder).tv_nickname.setText(userModels.get(position).getName());
            ((CustomViewHolder)holder).tv_location.setText(userModels.get(position).getLocation());



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("title",userModels.get(position).getName());
                    intent.putExtra("price",userModels.get(position).getLocation());
                    intent.putExtra("imageUrl",userModels.get(position).getProfileUrl());
                    intent.putExtra("destinationUid",userModels.get(position).getUid());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView iv_chat_profile;
            public TextView tv_nickname;
            public TextView tv_location;

            public CustomViewHolder(View view) {
                super(view);
                iv_chat_profile = (ImageView) view.findViewById(R.id.iv_chat_profile);
                tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
                tv_location = (TextView)view.findViewById(R.id.tv_content);
                tv_location.setTextColor(Color.parseColor("#ffff8800"));
            }
        }
    }
}
