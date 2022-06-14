package com.desaysv.mvvmdemo.ui.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * 基础ViewModel
 */
public class BaseViewModel extends ViewModel {
    public LiveData<String> failed;
}
