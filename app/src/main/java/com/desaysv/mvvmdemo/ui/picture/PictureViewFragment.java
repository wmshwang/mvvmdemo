package com.desaysv.mvvmdemo.ui.picture;

import com.desaysv.mvvmdemo.databinding.PictureViewFragmentBinding;
import com.desaysv.mvvmdemo.ui.adapter.ImageAdapter;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * 图片查看
 */
@AndroidEntryPoint
public class PictureViewFragment extends BaseFragment<PictureViewFragmentBinding, PictureViewViewModel> {
    String img;

    @Override
    protected void initView() {
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        if (getArguments() != null)
            img = getArguments().getString("img");

        //获取热门壁纸数据
        viewModel.getWallPaper();
    }

    @Override
    protected void dataObserve() {
        viewModel.wallPaper.observe(this, wallPapers -> {
            viewBinding.vp.setAdapter(new ImageAdapter(wallPapers));
            for (int i = 0; i < wallPapers.size(); i++) {
                if (img == null) {
                    return;
                }
                if (wallPapers.get(i).getImg().equals(img)) {
                    viewBinding.vp.setCurrentItem(i,false);
                    break;
                }
            }
        });
    }

}