package com.desaysv.mvvmdemo.ui.map;

import com.desaysv.mvvmdemo.databinding.MapFragmentBinding;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends BaseFragment<MapFragmentBinding, MapViewModel> {

    @Override
    protected void initView() {
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    protected void dataObserve() {

    }

}