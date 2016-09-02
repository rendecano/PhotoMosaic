package com.rendecano.photomosaic.data.engine;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.entity.MosaicTile;
import com.rendecano.photomosaic.data.tasks.DecodeImageTask;
import com.rendecano.photomosaic.data.tasks.GetBitmapFromFileTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ren Decano.
 */

public class MosaicEngine {

    private Context mContext;

    public MosaicEngine(Context pContext) {
        this.mContext = pContext;
    }

    public void getImageBitmap(Uri uri, final ImageListener pListener) {
        new GetBitmapFromFileTask(mContext, pListener).execute(uri);
    }

    public void getImageMosaic(int tilesize, Uri imageUri, final MosaicRowResponseListener pListener) {

        final List<MosaicTile> mosaicRow = new ArrayList<>();

        new DecodeImageTask(mContext, tilesize, imageUri, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                pListener.onSuccess((MosaicRow) message.obj);
                return false;
            }
        }), pListener).start();
    }
}
