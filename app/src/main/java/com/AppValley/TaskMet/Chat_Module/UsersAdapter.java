package com.AppValley.TaskMet.Chat_Module;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.databinding.RowConversationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    Context context;
    ArrayList<User> users;
    public static String name;
    public static String receiverId;
    public static String Image;
    Fragment fragment;
    FirebaseDatabase database;

    public UsersAdapter(Context context, ArrayList<User> users, Fragment fragment) {
        this.context = context;
        this.users = users;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);
        database = FirebaseDatabase.getInstance();

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);

        String senderId = FirebaseAuth.getInstance().getUid();

        String senderRoom = senderId + user.getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            holder.binding.msgTime.setText(dateFormat.format(new Date(time)));
                            holder.binding.lastMsg.setText(lastMsg);
                        } else {
                            holder.binding.lastMsg.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.username.setText(user.getName());

        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = user.getName();
                Image = user.getProfileImage();
                receiverId = user.getUid();

                if (holder.binding.deleteImageIcon.getVisibility() == View.VISIBLE) {
                    holder.binding.deleteImageIcon.setVisibility(View.INVISIBLE);
                    return;
                }

                Intent intent = new Intent(context, FragmentViewActivity.class);

                SharedPreferences preferences = context.getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor premium_data = preferences.edit();
                premium_data.putString(Constants.FRAGMENT_NAME, "ChatInbox");
                premium_data.apply();

                context.startActivity(intent);


            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.binding.deleteImageIcon.setVisibility(View.VISIBLE);
                holder.binding.msgTime.setVisibility(View.INVISIBLE);

                holder.binding.deleteImageIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Delete click", Toast.LENGTH_SHORT).show();
                        //......................Delete from chat List......................
                        String my_uid = FirebaseAuth.getInstance().getUid().toString();
                        String receiver_uid = users.get(position).getUid();
                        database.getReference().child(my_uid)
                                .child(receiver_uid)
                                .removeValue();

                        //....................delete chat...........................
                        String chatKey = my_uid + receiver_uid;
                        database.getReference().child("chats")
                                                .child(chatKey)
                                                .removeValue();

                        //..................................................................


                        users.remove(position);
                        notifyDataSetChanged();
                        holder.binding.deleteImageIcon.setVisibility(View.INVISIBLE);
                        holder.binding.msgTime.setVisibility(View.VISIBLE);

                    }
                });

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        RowConversationBinding binding;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }

}