package edu.neu.mad_sea.yaofuyang;

import android.app.Application;
import android.content.res.Resources;

/**
 * Defined so that the app resources can be used in non-activity.
 */
public class App extends Application {
    private static App mInstance;
    private static Resources res;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getRes() {
        return res;
    }

}