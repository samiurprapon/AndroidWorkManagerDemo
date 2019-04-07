package me.prapon.workmanagerdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public static final String KEY_TASK_OUTPUT = "key_task_output";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();

        String description = data.getString(MainActivity.KEY_TASK_DESC);

        displayNotification("Hey, I'm your work", description);

        Data outputData = new Data.Builder()
                                .putString(KEY_TASK_OUTPUT, "Task Finish Successfully")
                                .build();

        setOutputData(outputData);
        return Result.SUCCESS;
    }

    private void displayNotification(String task, String description) {

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("WorkerManagerDemo", "Work Manager Demo", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        Context context;
        String str;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "WorkerManagerDemo")
                .setContentTitle(task)
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());
    }
}
