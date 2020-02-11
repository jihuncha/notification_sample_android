package com.example.notification_sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;

    private Button bt_notification_send;
    private Button bt_notification_delete;
    private Button bt_notification_heads_up;
    private Button bt_notification_big_picture;
    private Button bt_notification_inbox;
    private Button bt_notification_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        initLayout();

    }

    public void initLayout() {
        bt_notification_send = (Button) findViewById(R.id.bt_notification_send);
        bt_notification_delete = (Button) findViewById(R.id.bt_notification_delete);
        bt_notification_heads_up = (Button) findViewById(R.id.bt_notification_heads_up);
        bt_notification_big_picture = (Button) findViewById(R.id.bt_notification_big_picture);
        bt_notification_inbox = (Button) findViewById(R.id.bt_notification_inbox);
        bt_notification_message = (Button) findViewById(R.id.bt_notification_message);

        bt_notification_send.setOnClickListener(this);
        bt_notification_delete.setOnClickListener(this);
        bt_notification_heads_up.setOnClickListener(this);
        bt_notification_big_picture.setOnClickListener(this);
        bt_notification_inbox.setOnClickListener(this);
        bt_notification_message.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_notification_send :
                createNotification();
                break;

            case R.id.bt_notification_delete :
                removeNotification();
                break;

            case R.id.bt_notification_heads_up :
                headsUpNotification();
                break;

            case R.id.bt_notification_big_picture:
                bigPictureNoti();
                break;

            case R.id.bt_notification_inbox:
                inboxStyleNotification();
                break;

            case R.id.bt_notification_message:
                messageStyleNotification();
                break;
        }
    }

    public PendingIntent getLaunchIntent(int notificationId, Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notificationId", notificationId);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void createNotification() {
        int NOTIFICATION_ID = 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Notification Actions");
        builder.setContentText("Tap View to launch website");
        builder.setAutoCancel(true);
        PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);

        builder.setContentIntent(launchIntent);
        builder.addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent);
        builder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    private void removeNotification() {
        int notificationId = getIntent().getIntExtra("notificationId", 1);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private void headsUpNotification() {
        int NOTIFICATION_ID = 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "Heads Up", NotificationManager.IMPORTANCE_HIGH));
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Heads Up Notification")
                        .setContentText("View NAVER")
                        .setAutoCancel(true)
                        //진동 및 사운드 설정..
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        //우선순위 높게 낮게 설정
                        //high 로 하면 상단에서 노티가내려오게됨
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);

        builder.addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent);
        builder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void bigPictureNoti() {
        int NOTIFICATION_ID = 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "Heads Up", NotificationManager.IMPORTANCE_HIGH));
        }

        Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.developer);

        Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);
        PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Big Picture Style");
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(pic));
        builder.setAutoCancel(true);
        builder.setContentIntent(launchIntent);
        builder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void messageStyleNotification() {
        int NOTIFICATION_ID = 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "message", NotificationManager.IMPORTANCE_HIGH));
        }

//        Person.Builder builder = new Person.Builder().setName("test").setIcon(R.drawable.developer);

        PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.developer));
        builder.setContentTitle("Messages");
//        builder.setStyle(new NotificationCompat.MessagingStyle("Teacher").setConversationTitle("Q&A Group")
//                .addMessage("This type of notification was introduced in Android N. Right?",0,"Student 1")
//                .addMessage("Yes",0,test)
//                .addMessage("The constructor is passed with the name of the current user. Right?",0,"Student 2")
//                .addMessage("True",0,test));
        builder.setAutoCancel(true);
        builder.setContentIntent(launchIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void inboxStyleNotification() {
        int NOTIFICATION_ID = 1;

        PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.developer));
        builder.setStyle(new NotificationCompat.InboxStyle().addLine("Hello").
                addLine("Are you there?").
                addLine("How's your day?").
                setBigContentTitle("3 New Messages for you").
                setSummaryText("Inbox"));
        builder.setAutoCancel(true);
        builder.setContentIntent(launchIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
