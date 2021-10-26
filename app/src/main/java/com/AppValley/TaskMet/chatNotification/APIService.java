package com.AppValley.TaskMet.chatNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAb46M55U:APA91bEpiyomGHGHLRDZYs7b7RZQ8Z3R1djfl0UtLZYP7-1VC0nnzBAgCs8Xx18PQGL9nH1CjfKIw1u9uU92fcFXqNnIosWJJMCDTk1TSsFkT08pjbqZk4AyempRp1X5-xskFlc0zaLD" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

