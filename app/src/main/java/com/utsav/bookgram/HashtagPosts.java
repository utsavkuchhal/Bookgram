package com.utsav.bookgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsav.bookgram.Adapter.HashtagPostsAdapter;
import com.utsav.bookgram.Model.Post;

import java.util.ArrayList;

public class HashtagPosts extends AppCompatActivity {

    RecyclerView recyclerView;
    HashtagPostsAdapter hashtagPostsAdapter;
    ArrayList<Post> mPosts;
    ArrayList<String> postIds;

    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_posts);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mPosts = new ArrayList<>();
        postIds = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        hashtagPostsAdapter = new HashtagPostsAdapter(this, mPosts);
        tag = getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("tag", "none");


        recyclerView.setAdapter(hashtagPostsAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("#" + tag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        readPosts();

    }


    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("HashTags").child(tag).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postIds.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    postIds.add(dataSnapshot.getKey());
                }

                Log.d("Avhvusdfadsdas",postIds.toString());

                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        mPosts.clear();

                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                            Post post = snapshot1.getValue(Post.class);

                            for (String id : postIds) {
                                if (post.getPostid().equals(id)) {
                                    mPosts.add(post);
                                }
                            }
                        }

                        hashtagPostsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}