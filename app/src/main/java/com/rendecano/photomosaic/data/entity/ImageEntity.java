package com.rendecano.photomosaic.data.entity;

import android.graphics.Bitmap;

/**
 * Created by Ren Decano.
 */

public class ImageEntity {

    private Bitmap bitmap;
    private int width;
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
