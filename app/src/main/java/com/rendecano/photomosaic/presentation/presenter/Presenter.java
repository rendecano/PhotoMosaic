package com.rendecano.photomosaic.presentation.presenter;


import com.rendecano.photomosaic.presentation.presenter.view.DefaultView;

/**
 * Created by Ren Decano.
 */

public interface Presenter<T extends DefaultView> {

    void attachView(T view);

    void destroy();

}
