package com.example.jgh76.myproject_jjh.function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.activity.ZoomActivity;
import com.example.jgh76.myproject_jjh.model.Chat;
import com.example.jgh76.myproject_jjh.model.Product;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by jgh76 on 2017-12-10.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> mChats;
    private Context mContext;
    FirebaseStorage mFirebaseStorage;

    // Pass in the contact array into the constructor
    public ChatAdapter(Context context, List<Chat> chats) {
        mChats = chats;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View chatView = inflater.inflate(R.layout.item_chat, parent, false);

        // Return a new holder instance
        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(context, chatView, this);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Chat chat = mChats.get(position);

        // Set item views based on your views and data model
        TextView tv_nickname = viewHolder.tv_nickname;
        tv_nickname.setText(chat.getNickname());
        tv_nickname.setPaintFlags(tv_nickname.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        TextView tv_content = viewHolder.tv_content;
        tv_content.setText(chat.getContent());
        tv_content.setPaintFlags(tv_content.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        TextView tv_time = viewHolder.tv_time;
        tv_time.setText(chat.getTime());

        final ImageView iv_chat_profile = viewHolder.iv_chat_profile;
        String profile = chat.getProfile();

        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl("gs://jjh-project-1b666.appspot.com").child("profile/"+profile);

        // Firebase UI 사용
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(iv_chat_profile);

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public void removeItem(int p) {
        mChats.remove(p);
        notifyItemRemoved(p);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_chat_profile;
        TextView tv_nickname;
        TextView tv_content;
        TextView tv_time;

        private ChatAdapter mChats;
        private Context context;

        public ViewHolder(final Context context, View itemView, ChatAdapter mChats) {
            super(itemView);

            iv_chat_profile = (ImageView)itemView.findViewById(R.id.iv_chat_profile);
            tv_nickname = (TextView)itemView.findViewById(R.id.tv_nickname);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);

            this.context = context;
            this.mChats = mChats;

        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Intent intent = new Intent(context, ZoomActivity.class);
            context.startActivity(intent);
            Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();
        }
    }
}
