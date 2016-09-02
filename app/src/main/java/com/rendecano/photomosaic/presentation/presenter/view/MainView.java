package com.rendecano.photomosaic.presentation.presenter.view;

import android.graphics.Bitmap;

import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.data.entity.MosaicTile;

import java.util.List;

/**
 * Created by Ren Decano.
 */

public interface MainView extends DefaultView {

    void showSelectedImage(ImageEntity imageEntity);
    void showConvertedImageRow(MosaicRow mosaicRow);

}
