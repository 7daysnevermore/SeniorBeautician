package com.example.nunepc.beautyblinkbeautician;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nunepc.beautyblinkbeautician.model.DataNoti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String s,trans;
    DataNoti dn = new DataNoti();
    Intent intent;
    Map<String, String> n = new HashMap<String, String>();
    ArrayList<String> list = new ArrayList<String>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref1 =ref.child("statusfornoti").child("status");

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.d("Runnnn","=");
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        // String click_action =remoteMessage.getNotification().getClickAction();
        //Intent intent = new Intent(click_action);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Log.e("FROM", remoteMessage.getFrom());
        sendNotification(notification, data);
    }



    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.balo);


        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref1 =ref.child("statusfornoti").child("status");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue(String.class);
                s =dataSnapshot.getValue(String.class);


                dn.setStatus(str);
                //System.out.println(dn.getStatus());
                String str1 = dn.getStatus();
                sst(dn.getStatus());
                Log.d("Guboringlaewwww",""+str1);
                Log.d("Banana",""+str1);
                list.add(str1);
                Log.d("list",""+ list.get(0));
                switch (dn.getStatus()){
                    case "offer":
                        Log.d("Please","="+dn.getStatus());

                        break;
                }
                n.put("s",dataSnapshot.getValue(String.class).toString());
                s=n.get("s").toString();
                Log.d("WWW","="+n.get("s").toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //null value
        // Log.d("list111",""+ list.get(0));
        //Log.d("WWW22222","="+s);
        //Log.d("Please1","="+dn.getStatus());
        Log.d("testweisus",""+trans);

        /*intent = new Intent(getApplicationContext(),Noti_Receive.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putString("picture_url", data.get("picture_url"));
        bundle.putString("status","offer");
        //Values.put("pictureurl","http://opsbug.com/static/google-io.jpg");
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);*/

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                //.setContentIntent(pendingIntent)
                //.setContentInfo("HelloBye")
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        //.setSmallIcon(R.mipmap.ic_launcher);


        try {
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String str = dataSnapshot.getValue(String.class);
                    s =dataSnapshot.getValue(String.class);


                    dn.setStatus(str);
                    //System.out.println(dn.getStatus());
                    String str1 = dn.getStatus();
                    sst(dn.getStatus());
                    Log.d("Guboringlaewwww",""+str1);
                    Log.d("Banana",""+str1);
                    list.add(str1);
                    Log.d("list",""+ list.get(0));
                    switch (dn.getStatus()){
                        case "offer":
                            Log.d("Please","="+dn.getStatus());

                            break;
                    }
                    n.put("s",dataSnapshot.getValue(String.class).toString());
                    s=n.get("s").toString();
                    Log.d("WWW","="+n.get("s").toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String picture_url = data.get("picture_url");
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.d("lastatata",""+s.toString());
        switch (s.toString()){
            case "offer":
                intent = new Intent(getApplicationContext(),Noti_Receive.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("picture_url", data.get("picture_url"));
                bundle.putString("status","offer");
                //Values.put("pictureurl","http://opsbug.com/static/google-io.jpg");
                intent.putExtras(bundle);
                break;
        }
       // intent = new Intent(getApplicationContext(),Noti_Receive.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo(s.toString());
        notificationBuilder.setSmallIcon(R.drawable.balo);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
    public void sst(String g){
        trans =g;
    }
}