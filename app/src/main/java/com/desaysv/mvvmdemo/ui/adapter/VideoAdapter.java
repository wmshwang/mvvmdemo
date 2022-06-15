package com.desaysv.mvvmdemo.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.ItemVideoBinding;
import com.desaysv.mvvmdemo.model.VideoResponse;

import org.jetbrains.annotations.NotNull;

/**
 * 视频列表适配器
 */
public class VideoAdapter extends BaseQuickAdapter<VideoResponse.ResultBean, BaseDataBindingHolder<ItemVideoBinding>> {

    public VideoAdapter() {
        super(R.layout.item_video);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemVideoBinding> bindingHolder, VideoResponse.ResultBean dataBean) {
        ItemVideoBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setVideo(dataBean);
            binding.executePendingBindings();
        }
    }
}
