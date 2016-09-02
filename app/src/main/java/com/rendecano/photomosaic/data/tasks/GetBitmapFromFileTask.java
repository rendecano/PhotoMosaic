package com.rendecano.photomosaic.data.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.engine.ImageListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Ren Decano.
 */

public class GetBitmapFromFileTask extends AsyncTask<Uri, Void, ImageEntity> {

    Context mContext;
    ImageListener mListener;

    public GetBitmapFromFileTask(Context pContext, ImageListener pListener) {
        this.mContext = pContext;
        this.mListener = pListener;
    }

    @Override
    protected ImageEntity doInBackground(Uri... uris) {


        ImageEntity imageEntity = new ImageEntity();

        try {
            final InputStream imageStream = mContext.getContentResolver().openInputStream(uris[0]);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageEntity.setBitmap(selectedImage);
            imageEntity.setWidth(selectedImage.getWidth());
            imageEntity.setHeight(selectedImage.getHeight());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mListener.onError("Invalid file");
        }

        return imageEntity;
    }

    @Override
    protected void onPostExecute(ImageEntity imageEntity) {

        if (imageEntity != null) {
            mListener.onSuccess(imageEntity);
        }
    }
}
