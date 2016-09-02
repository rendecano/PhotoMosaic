package com.rendecano.photomosaic.data.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Ren Decano.
 */

public class MosaicRow {

    private Bitmap bitmap;
    private Rect rect;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
