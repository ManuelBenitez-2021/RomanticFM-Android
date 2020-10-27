package tcking.github.com.rfm.aacplay.firebasepush;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import io.paperdb.Paper;
import tcking.github.com.rfm.aacplay.MainActivity;
import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.pojos.Notificationspojo;
import tcking.github.com.rfm.aacplay.pojos.UpdateNotification;

/**
 * Created by Prerna on 4/3/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static String message = "";
    String image = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        message = remoteMessage.getNotification().getBody();




        if (remoteMessage.getData().containsKey("image")) {
            image =  remoteMessage.getData().get("image");

        }
        Log.d("imageeee","image "+image);

        sendMyNotifictaion(remoteMessage.getNotification().getBody(),image);

    }

    public static String getStringValue(Map<String, String> data, String key) {

        if (data.containsKey(key)) {
            return data.get(key);
        }

        return "";
    }


    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);


     /*   String msg = (String) intent.getExtras().get("gcm.notification.body");
        new SaveNotificationIntentService(msg);
        Intent dbSyncIntent = new Intent(MyFirebaseMessagingService.this, SaveNotificationIntentService.class);
        startService(dbSyncIntent);*/

        String msg = (String) intent.getExtras().get("gcm.notification.body");
        ArrayList<Notificationspojo> stringArrayList = Paper.book("fcm").read("msgs", new ArrayList<Notificationspojo>());


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String txt_date = dateFormat.format(date);

        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        Date date1 = new Date();
        String txt_time = dateFormat1.format(date1);

        Notificationspojo notificationspojo = new Notificationspojo();
        notificationspojo.setMessage(msg);
        notificationspojo.setDate(txt_date);
        notificationspojo.setTime(txt_time);

        stringArrayList.add(notificationspojo);

        if(msg!=null)
        Paper.book("fcm").write("msgs", stringArrayList);

        EventBus.getDefault().post(new UpdateNotification());

    }


    private void sendMyNotifictaion(String body, String image) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


       /* new SaveNotificationIntentService(message);
        Intent dbSyncIntent = new Intent(MyFirebaseMessagingService.this, SaveNotificationIntentService.class);
        startService(dbSyncIntent);*/




        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder;
        if(!image.equalsIgnoreCase("")){
            largeIcon =    getBitmapfromUrl(image);


             notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeIcon))


                    .setContentTitle("Romantic FM")
                    .setContentText(message)

                    .setAutoCancel(true)

                    .setSound(soundUri)

                    .setContentIntent(pendingIntent);
        }else{
             notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon)

                    .setContentTitle("Romantic FM")
                    .setContentText(message)

                    .setAutoCancel(true)

                    .setSound(soundUri)

                    .setContentIntent(pendingIntent);
        }



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }



    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}
