package com.desaysv.mvvmdemo.ui.map;

import com.desaysv.mvvmdemo.databinding.MapFragmentBinding;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends BaseFragment<MapFragmentBinding, BaseViewModel> {

    @Override
    protected void initView() {
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    protected void dataObserve() {

    }

}