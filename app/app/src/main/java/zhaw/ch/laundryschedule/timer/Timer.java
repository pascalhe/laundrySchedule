package zhaw.ch.laundryschedule.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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

    private static final int COUNT_DOWN_INTERVAL = 1000;
    private long startTimeInMs = 0;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private EditText mInputTimer;
    private Button mButtonSetTimer;

    private PreferenceUtil mPreferences;
    private Context context;
    private View rootV;

    private CountDownTimer myCountDownTimer;
    private boolean mTimerRunning;

    private long mTimeLeftInMs = startTimeInMs;


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
        mInputTimer = rootV.findViewById(R.id.input_timer);
        mButtonSetTimer = rootV.findViewById(R.id.button_set_timer);

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

        mButtonSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimer();
            }
        });

        mInputTimer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    setTimer();
                    return true;
                }
                return false;
            }
        });

        mTextViewCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTimerRunning) {
                    mTextViewCountDown.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.INVISIBLE);
                    mButtonReset.setVisibility(View.INVISIBLE);
                    mInputTimer.setVisibility(View.VISIBLE);
                    mButtonSetTimer.setVisibility(View.VISIBLE);
                }
            }
        });

        updateCountDownText();

        return rootV;
    }

    private void setTimer() {
        String inputTime = mInputTimer.getText().toString();
        if (inputTime.isEmpty()) {
            mInputTimer.setError(getString(R.string.request_minutes));
            mInputTimer.requestFocus();
            return;
        }
        Long milliseconds = Long.parseLong(inputTime) * 60000;
        if (milliseconds <= 0) {
            mInputTimer.setError(getString(R.string.positive_number));
            mInputTimer.requestFocus();
            return;
        }
        mInputTimer.setVisibility(View.INVISIBLE);
        mButtonSetTimer.setVisibility(View.INVISIBLE);
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        startTimeInMs = milliseconds;
        resetTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimerRunning) {
            myCountDownTimer.cancel();
        }
    }

    private void initTimer() {
        long startTime = mPreferences.getStartedTime();
        if (startTime > 0) {
            mTimeLeftInMs = (startTimeInMs - (System.currentTimeMillis() - startTime));
            if (mTimeLeftInMs <= 0) { // TIMER EXPIRED
                mTimeLeftInMs = startTimeInMs;
                mTimerRunning = false;
                onTimerFinish();
            } else {
                startTimer();
                mTimerRunning = true;
            }
        } else {
            mTimeLeftInMs = startTimeInMs;
            mTimerRunning = false;
        }
        updateCountDownText();
        updateButtons();
    }


    private void startTimer() {

        myCountDownTimer = new CountDownTimer(mTimeLeftInMs, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long msUntilFinished) {
                mTimeLeftInMs = msUntilFinished;
                updateCountDownText();
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
        updateCountDownText();
        updateButtons();
    }


    private void pauseTimer() {
        myCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
        removeAlarm();
    }

    private void resetTimer() {
        mTimeLeftInMs = startTimeInMs;
        updateCountDownText();
        updateButtons();
        removeAlarm();
    }

    public void setAlarm() {
        long wakeUpTime = (mPreferences.getStartedTime() + startTimeInMs);

        AlarmManager am = (AlarmManager) rootV.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (am != null) {
            am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
        }
    }

    public void removeAlarm() {
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getActivity().getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(sender);
        }
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMs / 1000 / 3600);
        int minutes = (int) (mTimeLeftInMs / 1000 / 60);
        int seconds = (int) (mTimeLeftInMs / 1000 % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
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

            if (mTimeLeftInMs < startTimeInMs) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }

        }
    }

}
