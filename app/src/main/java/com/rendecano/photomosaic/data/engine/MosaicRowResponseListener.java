package com.rendecano.photomosaic.data.engine;

import android.graphics.Bitmap;

import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.entity.MosaicTile;

import java.util.List;

public interface MosaicRowResponseListener {

    void onSuccess(MosaicRow response);

    void onError(String message);
}
