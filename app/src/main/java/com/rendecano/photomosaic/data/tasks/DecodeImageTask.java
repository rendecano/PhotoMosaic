package com.rendecano.photomosaic.data.tasks;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.rendecano.photomosaic.data.config.Config;
import com.rendecano.photomosaic.data.engine.MosaicRowGenerator;
import com.rendecano.photomosaic.data.engine.MosaicRowResponseListener;
import com.rendecano.photomosaic.data.entity.MosaicTile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ren Decano.
 */
public class DecodeImageTask extends Thread {

    private Context mContext;
    private int tileSize = 0;
    private Uri mUri;

    private Handler mHandler;

    private int mWidth = 0;
    private int mHeight = 0;

    private int mTileWidthCount = 0;
    private int mTileHeightCount = 0;

    private ThreadPoolExecutor executor;
    private MosaicRowResponseListener mListener;

    public DecodeImageTask(Context pContext, int tileSize, Uri imageUri, Handler pHandler, MosaicRowResponseListener pListener) {
        this.mContext = pContext;
        this.tileSize = tileSize;
        this.mUri = imageUri;
        this.mHandler = pHandler;
        this.mListener = pListener;

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }

    @Override
    public void run() {
        super.run();

        Looper.prepare();

        try {

            File finalFile = new File(getRealPathFromURI(mUri));

            final InputStream imageStream = mContext.getContentResolver().openInputStream(mUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            mWidth = selectedImage.getWidth();
            mHeight = selectedImage.getHeight();

            // Create region decoder
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(finalFile.getAbsolutePath(), false);

            double widthDouble = mWidth / tileSize;
            double heightDouble = mHeight / tileSize;

            mTileWidthCount = (int) Math.ceil(widthDouble);
            mTileHeightCount = (int) Math.ceil(heightDouble);

            List<List<MosaicTile>> mosaicRowList = new ArrayList<>();

            Bitmap region;
            String hexColor;
            MosaicTile tile;
            Rect rect;
            String apiUrl;

            // top to bottom
            for (int i = 0; i < mTileHeightCount; i++) {

                List<MosaicTile> list = new ArrayList<>();

                // left to right
                for (int j = 0; j < mTileWidthCount; j++) {

                    int left = tileSize * j;
                    int top = tileSize * i;
                    int right = tileSize + left;
                    int bottom = tileSize + top;

                    tile = new MosaicTile();
                    rect = new Rect(left, top, right, bottom);

                    // Decode region for selected tile
                    region = decoder.decodeRegion(rect, null);

                    // Get the average color from the selected tile region
                    int col = getColor(region);

                    // Convert packed RGB to RGB Hex format
                    hexColor = String.format("%06X", (0xFFFFFF & col));

                    // Create URL
                    apiUrl = Config.API_BASE_URL + "/color/" + tileSize + "/" + tileSize + "/" + hexColor;

                    tile.setRect(rect);
                    tile.setRgbColor(hexColor);
                    tile.setUrl(apiUrl);

                    list.add(tile);

                    // Recycle bitmap
                    region.recycle();
                }

                mosaicRowList.add(list);

                new MosaicRowGenerator(i, mWidth, tileSize, mosaicRowList.get(i), executor, mHandler).run();

            }

        } catch (Exception e) {
            e.printStackTrace();

            mListener.onError("Ooops, selected image can't be decoded.");
        }

        Looper.loop();
    }

    private int getColor(Bitmap bitmap) {

        if (null == bitmap) {
            return Color.TRANSPARENT;
        }

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}