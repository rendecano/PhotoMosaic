package com.rendecano.photomosaic.data.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.rendecano.photomosaic.data.entity.MosaicTile;

import java.io.InputStream;

/**
 * Created by Ren Decano.
 */

public class DownloadImageRunnable implements Runnable {

    private int threadNo;
    private MosaicTile tile;
    private Handler handler;
    public static final String TAG = DownloadImageRunnable.class.getSimpleName();

    public DownloadImageRunnable(int threadNo, MosaicTile tile, Handler handler) {
        this.threadNo = threadNo;
        this.tile = tile;
        this.handler = handler;
    }

    @Override
    public void run() {
        tile.setBitmap(getBitmap(tile.getUrl()));
        sendMessage(threadNo, tile);
    }

    public void sendMessage(int what, MosaicTile msg) {

        Message message = handler.obtainMessage(what, msg);
        message.sendToTarget();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {

            InputStream input = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
