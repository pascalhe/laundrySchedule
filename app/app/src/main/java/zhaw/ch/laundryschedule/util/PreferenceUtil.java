package zhaw.ch.laundryschedule.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferenceUtil {
    private static final String STARTED_TIME_ID = "zhaw.ch.laundryschedule.STARTED_TIME_ID";
    private static final String SET_TIME_ID = "zhaw.ch.laundryschedule.SET_TIME_ID";
    private SharedPreferences mPreferences;

    public PreferenceUtil(Context c) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    /**
     * Get the Time saved into Shared Preferences with the ID: zhaw.ch.laundryschedule
     *
     * @return long time in milliseconds
     */
    public long getStartedTime() {
        return mPreferences.getLong(STARTED_TIME_ID, 0);
    }

    /**
     * Save the Time to the Shared Preferences with the ID: zhaw.ch.laundryschedule
     *
     * @param started long time in milliseconds
     */
    public void setStartedTime(long started) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(STARTED_TIME_ID, started);
        editor.apply();
    }

    /**
     * Save the Time to the Shared Preferences with the ID: zhaw.ch.laundryschedule
     *
     * @param time long time in milliseconds
     */
    public void setSetTime(long time) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(SET_TIME_ID, time);
        editor.apply();
    }

    /**
     * Get the Time saved into Shared Preferences with the ID: zhaw.ch.laundryschedule
     *
     * @return long time in milliseconds
     */
    public long getSetTime() {
        return mPreferences.getLong(SET_TIME_ID, 0);
    }
}

