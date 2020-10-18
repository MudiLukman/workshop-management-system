package com.control.repairsystem.presenter;

import com.control.repairsystem.view.AboutView;

public class AboutPresenter {

    private AboutView view;

    public AboutPresenter() {
        view = new AboutView();
    }

    public AboutView getView() {
        return view;
    }
}
