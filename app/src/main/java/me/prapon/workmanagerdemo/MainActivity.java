package me.prapon.workmanagerdemo;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    Button mButton;
    TextView mTextView;
    OneTimeWorkRequest request;

    public static final String KEY_TASK_DESC = "key_task_desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Hey, I'm sending work Data")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        request = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();
        mButton = findViewById(R.id.btnClick);
        mTextView = findViewById(R.id.tv_status);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().enqueue(request);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {

                            String output = "";
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {

                                    Data data = workInfo.getOutputData();

                                    output = data.getString(MyWorker.KEY_TASK_OUTPUT);
                                }

                            }
                            String status =  Objects.requireNonNull(workInfo).getState().name();
                            mTextView.setText(status + " "+output);
                        }
                    });

    }
}
