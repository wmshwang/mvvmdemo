package com.desaysv.mvvmdemo.ui.info;

import com.desaysv.mvvmdemo.databinding.InfoFragmentBinding;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;

public class InfoFragment extends BaseFragment<InfoFragmentBinding, InfoViewModel> {

    @Override
    protected void initView() {
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    protected void dataObserve() {

    }

}