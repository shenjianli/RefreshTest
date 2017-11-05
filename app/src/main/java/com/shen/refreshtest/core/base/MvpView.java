package com.shen.refreshtest.core.base;

public interface MvpView {
    void startLoading(int type);
    void hideLoading(int type);
    void showError(int type, String msg);
}