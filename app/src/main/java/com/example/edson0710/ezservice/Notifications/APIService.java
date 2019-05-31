package com.example.edson0710.ezservice.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1_0ODIY:APA91bHBwHwznx0WFZwt9py4J4EyUU7yelCqXkBxeiuUwrGuSIacQwakZCef50Y-GS_Wt0GAaR6KEFcEoc6RyHZ0V8cT4gMbQl2wX4uJgqCbOE0oWduG1Jual9RAc0vWf7ecofqemX5M"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}