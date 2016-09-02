package com.rendecano.photomosaic.presentation;

import android.app.Application;

/**
 * Created by Ren Decano.
 */

public class MosaicApplication extends Application {

    private static MosaicApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MosaicApplication getInstance() {
        return mInstance;
    }
}
