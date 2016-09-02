package com.rendecano.photomosaic.data.repository;

import android.net.Uri;

import com.rendecano.photomosaic.data.config.Config;
import com.rendecano.photomosaic.data.engine.MosaicRowResponseListener;
import com.rendecano.photomosaic.data.engine.MosaicEngine;
import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.engine.ImageListener;
import com.rendecano.photomosaic.domain.MosaicRepository;
import com.rendecano.photomosaic.domain.subscriber.UseCaseSubscriber;
import com.rendecano.photomosaic.presentation.MosaicApplication;

/**
 * Created by Ren Decano.
 */
public class MosaicDataRepository implements MosaicRepository {

    private MosaicEngine mosaicEngine;

    public MosaicDataRepository() {
        mosaicEngine = new MosaicEngine(MosaicApplication.getInstance());
    }

    @Override
    public void selectedImage(Uri imageUri, final UseCaseSubscriber<ImageEntity> subscriber) {

        mosaicEngine.getImageBitmap(imageUri, new ImageListener() {
            @Override
            public void onSuccess(ImageEntity response) {
                subscriber.onNext(response);
            }

            @Override
            public void onError(String message) {
                subscriber.onError(message);
            }
        });
    }

    @Override
    public void startMosaic(Uri imageUri, final UseCaseSubscriber<MosaicRow> subscriber) {

        mosaicEngine.getImageMosaic(Config.TILE_SIZE, imageUri, new MosaicRowResponseListener() {
            @Override
            public void onSuccess(MosaicRow response) {
                subscriber.onNext(response);
            }

            @Override
            public void onError(String message) {
                subscriber.onError(message);
            }
        });
    }
}
