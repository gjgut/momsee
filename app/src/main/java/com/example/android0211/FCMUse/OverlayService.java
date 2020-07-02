package com.example.android0211.FCMUse;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android0211.R;

public class OverlayService extends Service {
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Button momsee, unlock, stop,Call;
    private BlockThread block;
    final Handler handler1 = new Handler();
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    LayoutInflater inflater;
    ConstraintLayout layout;
    View temp;
    long Point,Duration;

    private String getForegroundPackageName() {

        String packageName = "";
        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            final long endTime = System.currentTimeMillis();
            final long beginTime = endTime - 100000;
            final UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    packageName = event.getPackageName();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            setting = getSharedPreferences("Set", 0);
            editor = setting.edit();


            //editor.putLong("Point", Point);
            //Duration = 60*1000*setting.getLong("Freetime",0);
            Duration = 60*1000*setting.getLong("Freetime",30);
            editor.putLong("Duration", Duration);
            editor.commit();

            block = new BlockThread();
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (ConstraintLayout) inflater.inflate(R.layout.activity_overlay, null);
            TextView Remain = layout.findViewById(R.id.textRemain);
            Remain.setText(String.valueOf(setting.getLong("Freetime",30)));
            Call = (Button)layout.findViewById(R.id.btnCall);
            momsee = layout.findViewById(R.id.momsee);

            momsee.setOnClickListener(v -> {
                if (v.getId() == R.id.momsee) {
                    Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.android0211.momsee");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            unlock = layout.findViewById(R.id.unlock);
            unlock.setOnClickListener(v -> {
                if (v.getId() == R.id.unlock) {
                    editor.putLong("Point", System.currentTimeMillis());
                    editor.putLong("Duration", Duration);
                    editor.putLong("Freetime",0);
                    editor.commit();
                    Remain.setText(String.valueOf(setting.getLong("Freetime",30)));
                    block.SleepForSecond(Duration);
                }
            });
            stop = layout.findViewById(R.id.stop);
            stop.setOnClickListener(v -> {
                if (v.getId() == R.id.stop) {
                    stopSelf();
                }
            });
            Call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_DIAL));
                }
            });
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,               //�׻� �� ������ �ְ�. status bar �ؿ� ����. ��ġ �̺�Ʈ ���� �� ����.
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Toast.makeText(getApplicationContext(), "onStartCommand 호출", Toast.LENGTH_LONG).show();
            block.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        block.Stop();//이거 추가해서 언락이됨
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class BlockThread extends Thread {
        boolean flag = true;
        long Second = 0;
        public  int safeLongToInt(long l) {

            if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {

                throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");

            }

            return (int) l;

        }
        public void run() {
            while (flag) {
                int i = 0;
                long Point = setting.getLong("Point", 0);
                long Duration = setting.getLong("Duration", 0);
                Second = ( Point + Duration) - System.currentTimeMillis();
                String Name = getForegroundPackageName();
                try {
                    /*
                    if (Second > 0) {
                        if (temp != null) {
                            handler1.post(() -> mWindowManager.removeViewImmediate(layout));
                        }
                        temp = null;
                        Thread.sleep(Second);
                        Second = 0;
                        editor.putLong("Point", 0);
                        editor.putInt("Duration", 0);
                        editor.commit();
                    }
                    if (Name.equals("com.example.android0211.momsee")) {
                        if (temp != null) {
                            handler1.post(() -> mWindowManager.removeViewImmediate(layout));
                        }
                        temp = null;
                    } */
                    if ((Second > 0 || Name.equals("com.example.android0211"))) {
                        if (temp != null) {
                            handler1.post(() ->
                                 mWindowManager.removeViewImmediate(layout));
                        }
                        temp = null;
                        Thread.sleep(Second);
                        Second = 0;
                        flag = true;
                        editor.putLong("Point", 0);
                        editor.putLong("Duration", 0);
                        editor.commit();
                    }
                    else {
                        if (temp == null)
                            try {
                                handler1.post(() -> {
                                    try {
                                        mWindowManager.addView(layout, mParams);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        temp = layout;
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            handler1.post(() -> mWindowManager.removeViewImmediate(layout));
        }

        public void Stop() {
            this.flag = false;
        }

        public void SleepForSecond(long Second) {
            this.Second = Second;
        }
    }

}