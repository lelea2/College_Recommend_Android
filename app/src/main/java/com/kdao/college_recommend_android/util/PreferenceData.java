package com.kdao.college_recommend_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class PreferenceData {
    static final String SAT_SCORE = "sat";
    static final String ACT_SCORE = "act";
    static final String STATE = "state";
    static final String TUITION_RANGE = "tuition_range";
    static final String VOLUNTEER_HOUR_RANGE = "hour_range";

    /**
     * Get SharePreferences object
     * @param ctx
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setPersonalPref(Context ctx, String sat_score, String act_score, String state, String tuition_range, String hour_range) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SAT_SCORE, sat_score);
        editor.putString(ACT_SCORE, act_score);
        editor.putString(STATE, state);
        editor.putString(TUITION_RANGE, tuition_range);
        editor.putString(VOLUNTEER_HOUR_RANGE, hour_range);
        editor.commit();
    }

    public static String getSatScore(Context ctx) {
        return getSharedPreferences(ctx).getString(SAT_SCORE, "");
    }

    public static String getActScore(Context ctx) {
        return getSharedPreferences(ctx).getString(ACT_SCORE, "");
    }

    public static String getState(Context ctx) {
        return getSharedPreferences(ctx).getString(STATE, "");
    }

    public static String getTuitionRange(Context ctx) {
        return getSharedPreferences(ctx).getString(TUITION_RANGE, "");
    }
    public static String getVolunteerHourRange(Context ctx) {
        return getSharedPreferences(ctx).getString(VOLUNTEER_HOUR_RANGE, "");
    }

}
