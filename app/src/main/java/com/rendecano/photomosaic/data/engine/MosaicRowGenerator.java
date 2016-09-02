package com.rendecano.photomosaic.data.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.entity.MosaicTile;
import com.rendecano.photomosaic.data.tasks.DownloadImageRunnable;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Ren Decano.
 */

public class MosaicRowGenerator {

    List<MosaicTile> mosaicTiles;
    int row;
    int rowcount;
    int rowWidth;
    int tileSize;

    private Bitmap rowBitmap;
    private Canvas rowCanvas;
    private ThreadPoolExecutor executor;
    private Handler mHandler;

    public MosaicRowGenerator(int row, int rowWidth, int tileSize, List<MosaicTile> pMosaicTiles, ThreadPoolExecutor pExecutor, Handler pHandler) {
        this.row = row;
        this.mosaicTiles = pMosaicTiles;
        this.rowWidth = rowWidth;
        this.tileSize = tileSize;
        this.executor = pExecutor;
        this.mHandler = pHandler;
        this.rowBitmap = Bitmap.createBitmap(rowWidth, tileSize, Bitmap.Config.ARGB_8888);
        this.rowCanvas = new Canvas(rowBitmap);
    }

    public void run() {

        for (MosaicTile tile : mosaicTiles) {
            executor.execute(new DownloadImageRunnable(row, tile, handler));
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int num = message.what;

            if (num == row) {
                MosaicTile r = (MosaicTile) message.obj;

                int left = r.getRect().left;
                int top = 0;
                int right = r.getRect().right;
                int bottom = tileSize + top;

                rowCanvas.drawBitmap(r.getBitmap(), null, new Rect(left, top, right, bottom), null);

                if (++rowcount == mosaicTiles.size()) {

                    int leftRow = 0;
                    int topRow = tileSize * num;
                    int rightRow = rowWidth;
                    int bottomRow = tileSize + topRow;

                    MosaicRow row = new MosaicRow();
                    row.setBitmap(rowBitmap);
                    row.setRect(new Rect(leftRow, topRow, rightRow, bottomRow));

                    Message messageRow = mHandler.obtainMessage(num, row);
                    messageRow.sendToTarget();
                }

            }

            return false;
        }
    });
}
