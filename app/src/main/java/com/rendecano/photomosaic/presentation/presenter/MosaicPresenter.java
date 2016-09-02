package com.rendecano.photomosaic.presentation.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.entity.MosaicTile;
import com.rendecano.photomosaic.domain.interactor.MosaicUseCase;
import com.rendecano.photomosaic.domain.subscriber.UseCaseSubscriber;
import com.rendecano.photomosaic.presentation.presenter.view.MainView;

import java.util.List;

/**
 * Created by Ren Decano.
 */

public class MosaicPresenter implements Presenter<MainView>  {

    MainView mView;
    MosaicUseCase usecase;

    public MosaicPresenter() {
        usecase = new MosaicUseCase();
    }

    @Override
    public void attachView(MainView view) {
        mView = view;
    }

    @Override
    public void destroy() {

    }

    public void displaySelectedImage(Uri imageUri) {
        mView.showLoading();
        usecase.displaySelectedImage(imageUri, new GetImageSubscriber());
    }

    public void startMosaicProcess(Uri imageUri) {
        usecase.getMosaicImage(imageUri, new GetMosaicSubscriber());
    }

    private class GetImageSubscriber extends UseCaseSubscriber<ImageEntity> {

        @Override
        public void onError(String message) {
            mView.hideLoading();
            mView.showError(message);
        }

        @Override
        public void onNext(ImageEntity imageEntity) {
            mView.showSelectedImage(imageEntity);
        }
    }

    private class GetMosaicSubscriber extends UseCaseSubscriber<MosaicRow> {

        @Override
        public void onError(String message) {
            mView.hideLoading();
            mView.showError(message);
        }

        @Override
        public void onNext(MosaicRow mosaicRow) {
            mView.hideLoading();
            mView.showConvertedImageRow(mosaicRow);
        }
    }
}
