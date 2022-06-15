package com.desaysv.mvvmdemo.ui.info;

import androidx.fragment.app.Fragment;

import com.desaysv.mvvmdemo.databinding.InfoFragmentBinding;
import com.desaysv.mvvmdemo.ui.adapter.InfoFragmentAdapter;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;
import com.desaysv.mvvmdemo.ui.news.NewsFragment;
import com.desaysv.mvvmdemo.ui.video.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InfoFragment extends BaseFragment<InfoFragmentBinding, BaseViewModel> {
    /**
     * 标题数组
     */
    private final String[] titles = {"新闻","视频"};
    private final List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void initView() {
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        fragmentList.add(new NewsFragment());
        fragmentList.add(new VideoFragment());
        viewBinding.vp.setAdapter(new InfoFragmentAdapter(getChildFragmentManager(), fragmentList, titles));
        viewBinding.tab.setupWithViewPager(viewBinding.vp);
    }

    @Override
    protected void dataObserve() {

    }

}