package com.example.jgh76.myproject_jjh.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jgh76.myproject_jjh.R;
import com.example.jgh76.myproject_jjh.function.ProductsAdapter;
import com.example.jgh76.myproject_jjh.model.ChatModel;
import com.example.jgh76.myproject_jjh.model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    private FirebaseStorage mFirebaseStorage;
    private RecyclerView rv_chat;

    private String imageUrl;
    private String title;
    private String price;
    private String uid ;
    private String destinationUid;
    private String chatRoomUid;

    private User destinationUser;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @BindView(R.id.iv_chat_product)
    ImageView iv_chat_product;

    @BindView(R.id.tv_chat_title)
    TextView tv_chat_title;

    @BindView(R.id.tv_chat_price)
    TextView tv_chat_price;

    @BindView(R.id.et_message)
    EditText et_message;

    @BindView(R.id.btn_send)
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        title = intent.getStringExtra("title");
        price = intent.getStringExtra("price");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationUid = intent.getStringExtra("destinationUid");

        tv_chat_title.setText(title);
        tv_chat_price.setText(price);

        mFirebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl(imageUrl);
        // Firebase UI 사용
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(iv_chat_product);

        checkChatRoom();

        rv_chat = (RecyclerView)findViewById(R.id.rv_chat);
        rv_chat.setHasFixedSize(true);

    }

    @OnClick(R.id.btn_send)
    public void onSendClicked(){

        ChatModel chatModel = new ChatModel();
        chatModel.users.put(uid,true);
        chatModel.users.put(destinationUid,true);


        if(chatRoomUid == null){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    checkChatRoom();
                }
            });

        }else {

            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = uid;
            comment.message = et_message.getText().toString();
            comment.timestamp = ServerValue.TIMESTAMP;
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    et_message.setText("");
                }
            });

        }
    }


    public void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destinationUid)){
                        chatRoomUid = item.getKey();
                        rv_chat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        rv_chat.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    destinationUser = dataSnapshot.getValue(User.class);
                    getMessageList();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    //메세지가 갱신
                    notifyDataSetChanged();
                    rv_chat.scrollToPosition(comments.size() - 1);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new MessageViewHolder(view, parent.getContext(), this);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            //내가보낸 메세지
            if(comments.get(position).uid.equals(uid)){
                messageViewHolder.tv_nickname.setText("나");
                messageViewHolder.tv_content.setText(comments.get(position).message);

//                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
//                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
//                messageViewHolder.tv_content.setTextSize(25);
//                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                //상대방이 보낸 메세지
            }else {

                String profileUrl = destinationUser.getProfileUrl();
                Glide.with(holder.itemView.getContext())
                        .using(new FirebaseImageLoader())
                        .load(FirebaseStorage.getInstance().getReferenceFromUrl(profileUrl))
                        .into(messageViewHolder.iv_chat_profile);
                messageViewHolder.tv_nickname.setText(destinationUser.getName());
//                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
//                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.tv_content.setText(comments.get(position).message);
//                messageViewHolder.textView_message.setTextSize(25);
//                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);


            }
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.tv_time.setText(time);


        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_content;
            public TextView tv_nickname;
            public ImageView iv_chat_profile;
            public TextView tv_time;

            private RecyclerViewAdapter rvAdapter;
            private Context context;

            public MessageViewHolder(View view, Context context, RecyclerViewAdapter rvAdapter) {
                super(view);
                tv_content = (TextView) view.findViewById(R.id.tv_content);
                tv_nickname = (TextView)view.findViewById(R.id.tv_nickname);
                iv_chat_profile = (ImageView)view.findViewById(R.id.iv_chat_profile);
                tv_time = (TextView)view.findViewById(R.id.tv_time);

                this.rvAdapter = rvAdapter;
                this.context = context;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}

