package com.rendecano.photomosaic.data.engine;

import android.os.Message;

import com.rendecano.photomosaic.data.entity.MosaicTile;

import java.util.List;

public interface MosaicResponseListener {

    void onSuccess(Message response);

    void onError(String message);
}
