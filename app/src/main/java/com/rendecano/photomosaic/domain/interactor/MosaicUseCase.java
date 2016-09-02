package com.rendecano.photomosaic.domain.interactor;

import android.graphics.Bitmap;
import android.net.Uri;

import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.repository.MosaicDataRepository;
import com.rendecano.photomosaic.domain.MosaicRepository;
import com.rendecano.photomosaic.domain.subscriber.UseCaseSubscriber;

/**
 * Created by Ren Decano.
 */

public class MosaicUseCase {

    private MosaicRepository repository;

    public MosaicUseCase() {
        repository = new MosaicDataRepository();
    }

    public void displaySelectedImage(Uri imageUri, UseCaseSubscriber<ImageEntity> subscriber) {
        repository.selectedImage(imageUri, subscriber);
    }

    public void getMosaicImage(Uri imageUri, UseCaseSubscriber<MosaicRow> subscriber) {
        repository.startMosaic(imageUri, subscriber);
    }

}
