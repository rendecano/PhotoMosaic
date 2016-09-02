package com.rendecano.photomosaic.data.engine;

import android.graphics.Bitmap;

import com.rendecano.photomosaic.data.entity.ImageEntity;

public interface ImageListener {

    void onSuccess(ImageEntity response);

    void onError(String message);
}
