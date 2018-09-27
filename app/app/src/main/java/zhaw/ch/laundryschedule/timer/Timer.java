package zhaw.ch.laundryschedule.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import zhaw.ch.laundryschedule.R;
import zhaw.ch.laundryschedule.util.PreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Timer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Timer extends Fragment {
    private static final String TAG = "Timer";
    private static final long START_TIME_IN_MS = 12000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private PreferenceUtil mPreferences;
    private Context context;
    private View rootV;

    private CountDownTimer myCountDownTimer;
    private boolean mTimerRunning;

    private long mTimeLeftInMs = START_TIME_IN_MS;


    public Timer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Dashboard.
     */
    public static Timer newInstance() {
        Timer fragment = new Timer();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootV = inflater.inflate(R.layout.fragment_timer, container, false);
        context = rootV.getContext();

        mPreferences = new PreferenceUtil(context);

        mTextViewCountDown = rootV.findViewById(R.id.text_view_countdown);
        mButtonStartPause = rootV.findViewById(R.id.button_start_pause);
        mButtonReset = rootV.findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    mPreferences.setStartedTime(System.currentTimeMillis());
                    startTimer();
                }

            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCounDownText();

        return rootV;
    }

    @Override
    public void onResume() {
        super.onResume();
        initTimer();
        //       removeAlarm();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimerRunning) {
            myCountDownTimer.cancel();
            setAlarm();
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mTimerRunning) {
//            myCountDownTimer.cancel();
//            setAlarm();
//        }
//    }

    private void initTimer() {
        long startTime = mPreferences.getStartedTime();
        if (startTime > 0) {
            mTimeLeftInMs = (START_TIME_IN_MS - (System.currentTimeMillis() - startTime));
            if (mTimeLeftInMs <= 0) { // TIMER EXPIRED
                mTimeLeftInMs = START_TIME_IN_MS;
                mTimerRunning = false;
                onTimerFinish();
            } else {
                startTimer();
                mTimerRunning = true;
            }
        } else {
            mTimeLeftInMs = START_TIME_IN_MS;
            mTimerRunning = false;
        }
        updateCounDownText();
        updateButtons();
    }


    private void startTimer() {

        myCountDownTimer = new CountDownTimer(mTimeLeftInMs, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long msUntilFinished) {
                mTimeLeftInMs = msUntilFinished;
                updateCounDownText();
            }

            @Override
            public void onFinish() {
                onTimerFinish();
            }
        }.start();
        mTimerRunning = true;
        updateButtons();
        setAlarm();
    }

    private void onTimerFinish() {
        Toast.makeText(context, R.string.timer_finished, Toast.LENGTH_SHORT).show();
        mPreferences.setStartedTime(0);
        mTimerRunning = false;
        updateCounDownText();
        updateButtons();
    }


    private void pauseTimer() {
        myCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMs = START_TIME_IN_MS;
        updateCounDownText();
        updateButtons();
    }

    public void setAlarm() {
        long wakeUpTime = (mPreferences.getStartedTime() + START_TIME_IN_MS);

        //       alarmMgr = context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
// Set the alarm to start at approximately 4:00 p.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000*60*60*24, alarmIntent);

        Log.d(TAG, "setAlarm: " + wakeUpTime);
        AlarmManager am = (AlarmManager) rootV.getContext().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimerExpiredReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(wakeUpTime, sender);
        if (am != null) {
            am.setAlarmClock(alarmClockInfo, sender);
        }
        // am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
    }

    public void removeAlarm() {
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), TimerExpiredReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getActivity().getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(sender);
        }
    }

    //    Intent i = new Intent(this, MainActivity.class);
//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
//    Notification.Builder builder = new Notification.Builder(this) .setSmallIcon(R.drawable.abc_ic_go_search_api_mtrl_alpha) .setContentTitle("Our notification") .setContentIntent(pendingIntent);
//    //show the notification by calling NotificationManager.notify
//    NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICA TION_SERVICE);
//m.notify("test", 0, builder.build());
    private void updateCounDownText() {
        int minutes = (int) (mTimeLeftInMs / 1000 / 60);
        int seconds = (int) (mTimeLeftInMs / 1000 % 60);

        String timeLeftFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormated);
    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText(R.string.pause);
        } else {
            mButtonStartPause.setText(R.string.start);
            if (mTimeLeftInMs < COUNT_DOWN_INTERVAL) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMs < START_TIME_IN_MS) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }

        }
    }

}
