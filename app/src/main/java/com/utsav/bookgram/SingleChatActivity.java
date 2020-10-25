package com.utsav.bookgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.utsav.bookgram.Adapter.SingleChatAdpater;
import com.utsav.bookgram.Model.ChatModel;
import com.utsav.bookgram.Model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleChatActivity extends AppCompatActivity {

    private RecyclerView list_of_messages;
    private EditText input;
    private TextView fab;
    private String userID;
    private ArrayList<ChatModel> chats;
    private SingleChatAdpater singleChatAdpater;
    private TextView username;
    private TextView fullname;
    private ImageView image;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        list_of_messages = findViewById(R.id.list_of_messages);
        userID = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("userid", "none");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        input = findViewById(R.id.input);
        fab = findViewById(R.id.fab);
        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        image = findViewById(R.id.image_profile);


        chats = new ArrayList<>();
        singleChatAdpater = new SingleChatAdpater(this, chats);
        list_of_messages.setAdapter(singleChatAdpater);
        list_of_messages.setLayoutManager(new LinearLayoutManager(this));

        final String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getChat(myid, userID);
        setToolBar();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = input.getText().toString();
                if (message.length() > 0) {
                    sendMessage(message, myid, userID);
                    input.setText("");
                } else {
                    Toast.makeText(SingleChatActivity.this, "Please write a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getChat(final String myid, final String reciverid) {
        FirebaseDatabase.getInstance().getReference().child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if (chatModel.getReciever().equals(myid) && chatModel.getSender().equals(userID)
                            || chatModel.getReciever().equals(userID) && chatModel.getSender().equals(myid)) {
                        chats.add(chatModel);
                    }
                }
                singleChatAdpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String message, String sender, String reciver) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", sender);
        map.put("reciever", reciver);
        map.put("message", message);
        FirebaseDatabase.getInstance().getReference().child("chat").push().setValue(map);
    }

    private void setToolBar() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImageurl().equals("default")) {
                    image.setImageResource(R.drawable.profile);
                } else {
                    Picasso.get().load(user.getImageurl()).placeholder(R.drawable.profile).into(image);
                }
                username.setText(user.getUsername());
                fullname.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}