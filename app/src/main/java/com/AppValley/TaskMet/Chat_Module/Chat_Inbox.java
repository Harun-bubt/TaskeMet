package com.AppValley.TaskMet.Chat_Module;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.ViewAdActivities.ViewUsersAdDetails;
import com.AppValley.TaskMet.chatNotification.APIService;
import com.AppValley.TaskMet.chatNotification.Client;
import com.AppValley.TaskMet.chatNotification.Data;
import com.AppValley.TaskMet.chatNotification.MyResponse;
import com.AppValley.TaskMet.chatNotification.NotificationSender;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.AppValley.TaskMet.Home.HomeFragments.Chat;
import com.AppValley.TaskMet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat_Inbox extends Fragment {

    Context context;
    View messageView;

    MessageAdapter adapter;
    ArrayList<Message> messages;

    public static SharedPreferences sharedPreferences;

    String senderRoom, receiverRoom, senderUid, receiverUid, TAG = "notification_logs";

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;

    RecyclerView recyclerView;
    ImageView btnSend, camera, goBack;
    Toolbar toolbar;

    CircleImageView profileImage;
    TextView profileName, Status;
    EditText messageBox;
    FragmentManager fragmentManager;
    Fragment fragment;
    RelativeLayout bottomLine;

    private APIService apiService;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        messageView = inflater.inflate(R.layout.fragment_messages, container, false);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);

        context = getContext();
        recyclerView = messageView.findViewById(R.id.recyclerViewForMessage);
        btnSend = messageView.findViewById(R.id.sendBtn);
        toolbar = messageView.findViewById(R.id.message_toolbar);
        goBack = messageView.findViewById(R.id.backArrow);
        profileImage = messageView.findViewById(R.id.profile);
        profileName = messageView.findViewById(R.id.name);
        Status = messageView.findViewById(R.id.status);
        messageBox = messageView.findViewById(R.id.messageBox);
        camera = messageView.findViewById(R.id.camera);
        bottomLine = messageView.findViewById(R.id.bottomLine);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        bottomLine.setEnabled(false);

        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragment = new Chat();

        dialog = new ProgressDialog(context);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

        messages = new ArrayList<>();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        String name = UsersAdapter.name;
        String profile = UsersAdapter.Image;

        profileName.setText(name);
        Glide.with(getActivity()).load(profile)
                .placeholder(R.drawable.avatar)
                .into(profileImage);

        messageView.setFocusableInTouchMode(true);
        messageView.requestFocus();
        messageView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                    return true;
                }
                return false;
            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewUsersAdDetails.isFromAd) {

                } else {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.chat_user_list, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


            }
        });

        receiverUid = UsersAdapter.receiverId;
        senderUid = FirebaseAuth.getInstance().getUid();

        database.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (!status.isEmpty()) {
                        if (status.equals("Offline")) {
                            Status.setVisibility(View.GONE);
                        } else {
                            Status.setText(status);
                            Status.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        adapter = new MessageAdapter(context, messages, senderRoom, receiverRoom);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = messageBox.getText().toString();

                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime());
                messageBox.setText("");

                String randomKey = database.getReference().push().getKey();

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());

                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(receiverUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String user_token = dataSnapshot.getValue(String.class);

                                        sendNotifications(user_token, sharedPreferences.getString(Constants.NAME,"TaskMet User"), messageTxt);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                });

            }
        });

        final Handler handler = new Handler();
        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (messageBox.getText().toString().equals("")) {
                    camera.setVisibility(View.VISIBLE);
                } else {
                    camera.setVisibility(View.GONE);
                }
                database.getReference().child("presence").child(senderUid).setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping, 1000);

            }

            Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("presence").child(senderUid).setValue("Online");
                }
            };
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                cameraActivityResultLauncher.launch(intent);

            }
        });

        UpdateToken();
        return messageView;

    }

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // There are no request codes
                        Intent data = result.getData();

                        assert data != null;
                        if (data.getData() != null) {

                            Uri selectedImage = data.getData();
                            Calendar calendar = Calendar.getInstance();

                            StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                            dialog.show();
                            reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    dialog.dismiss();
                                    if (task.isSuccessful()) {
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String filePath = uri.toString();

                                                String messageTxt = messageBox.getText().toString();

                                                Date date = new Date();
                                                Message message = new Message(messageTxt, senderUid, date.getTime());
                                                message.setMessage("photo");
                                                message.setImageUrl(filePath);
                                                messageBox.setText("");

                                                String randomKey = database.getReference().push().getKey();

                                                HashMap<String, Object> lastMsgObj = new HashMap<>();
                                                lastMsgObj.put("lastMsg", message.getMessage());
                                                lastMsgObj.put("lastMsgTime", date.getTime());

                                                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                                                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                                                database.getReference().child("chats")
                                                        .child(senderRoom)
                                                        .child("messages")
                                                        .child(randomKey)
                                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        database.getReference().child("chats")
                                                                .child(receiverRoom)
                                                                .child("messages")
                                                                .child(randomKey)
                                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });
                                                    }
                                                });

                                                //Toast.makeText(ChatActivity.this, filePath, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }

                    }
                }
            });


    private void UpdateToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token.toString();
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(msg);

                    }
                });

    }

    public void sendNotifications(String user_token, String title, String message) {

        Data data = new Data(title, message);

        NotificationSender sender = new NotificationSender(data, user_token);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {

                    Log.d(TAG, "Notification Send");

                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
