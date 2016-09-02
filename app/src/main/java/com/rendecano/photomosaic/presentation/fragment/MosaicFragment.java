package com.rendecano.photomosaic.presentation.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rendecano.photomosaic.R;
import com.rendecano.photomosaic.data.entity.ImageEntity;
import com.rendecano.photomosaic.data.entity.MosaicRow;
import com.rendecano.photomosaic.presentation.presenter.MosaicPresenter;
import com.rendecano.photomosaic.presentation.presenter.view.MainView;

public class MosaicFragment extends Fragment implements MainView {

    private final int SELECT_PHOTO = 0;
    private final int RESULT_OK = -1;

    private ImageView mImageView;
    private View mainView;
    private ProgressDialog mProgressBar;

    private MosaicPresenter mPresenter;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mImageView = (ImageView) mainView.findViewById(R.id.imageView);
        mImageView.setDrawingCacheEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MosaicPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        final Uri imageUri = data.getData();

                        mPresenter.displaySelectedImage(imageUri);

                        mPresenter.startMosaicProcess(imageUri);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void showSelectedImage(ImageEntity imageEntity) {

        mBitmap = Bitmap.createBitmap(imageEntity.getWidth(), imageEntity.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public void showConvertedImageRow(MosaicRow mosaicRow) {

        mCanvas.drawBitmap(mosaicRow.getBitmap(), null, mosaicRow.getRect(), null);
        mImageView.setImageBitmap(mBitmap);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        mProgressBar = ProgressDialog.show(getActivity(), "",
                getString(R.string.fetching_tiles), true);
        mProgressBar.setCancelable(false);
    }

    @Override
    public void hideLoading() {
        if (mProgressBar != null) {
            mProgressBar.dismiss();
        }
    }
}
