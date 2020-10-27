package tcking.github.com.rfm.aacplay.helper;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;
import tcking.github.com.rfm.aacplay.pojos.Notificationspojo;

public class SaveNotificationIntentService extends IntentService {
    private static String message;

    public SaveNotificationIntentService() {
        super(SaveNotificationIntentService.class.getName());
    }

    public SaveNotificationIntentService(String message) {
        super(SaveNotificationIntentService.class.getName());
        this.message= message;

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ArrayList<Notificationspojo> stringArrayList = Paper.book("fcm").read("msgs", new ArrayList<Notificationspojo>());


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String txt_date = dateFormat.format(date);

        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        Date date1 = new Date();
        String txt_time = dateFormat1.format(date1);

        Notificationspojo notificationspojo = new Notificationspojo();
        notificationspojo.setMessage(message);
        notificationspojo.setDate(txt_date);
        notificationspojo.setTime(txt_time);

        stringArrayList.add(notificationspojo);

        Paper.book("fcm").write("msgs", stringArrayList);

    }
}
