package com.rendecano.photomosaic.domain;

import android.net.Uri;

import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.domain.subscriber.UseCaseSubscriber;

/**
 * Created by Ren Decano.
 */

public interface MosaicRepository {

    void selectedImage(Uri imageUri, UseCaseSubscriber<ImageEntity> subscriber);

    void startMosaic(Uri imageUri, UseCaseSubscriber<MosaicRow> subscriber);

}
