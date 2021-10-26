package com.AppValley.TaskMet.Home.HomeFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.AppValley.TaskMet.Chat_Module.User;
import com.AppValley.TaskMet.Chat_Module.UsersAdapter;
import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.Registration.Activity3_SignUp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends Fragment {

    FirebaseDatabase database;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    ProgressDialog dialog;

    User user;
    RecyclerView recyclerView;
    Context context;
    Chat chatFragmet;

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View chatView = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = chatView.findViewById(R.id.recyclerViewForUserList);

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        context = getContext();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();


        chatView.setFocusableInTouchMode(true);
        chatView.requestFocus();


        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        usersAdapter = new UsersAdapter(context, users,chatFragmet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView.setAdapter(usersAdapter);



        database.getReference().child("chatList").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        users.add(user);
                    }
                }

                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatView;
    }

    @Override
    public void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    @Override
    public void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
