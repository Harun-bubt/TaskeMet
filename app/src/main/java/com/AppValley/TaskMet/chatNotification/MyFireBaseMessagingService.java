package com.AppValley.TaskMet.chatNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.R;
import com.application.isradeleon.notify.Notify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Random;


public class MyFireBaseMessagingService extends FirebaseMessagingService {

    String title, message, TAG = "notification_logs";

    @Override
    public void onNewToken(@NotNull String s) {

        super.onNewToken(s);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseMessaging.getInstance().getToken().toString();

        if (firebaseUser != null) {
            updateToken(refreshToken);
        }

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("message");


        Notify.build(getApplicationContext())

                .setTitle(title)
                .setContent(message)

                .setSmallIcon(R.drawable.logo)
                .setColor(R.color.Theme1)

                .setLargeIcon(R.drawable.logo_round)

                //.setPicture(imageUrl)

                .show(); // Show notification

    }


    private void updateToken(String refreshToken) {

        Token token1 = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);

    }

}

