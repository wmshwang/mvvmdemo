package com.desaysv.mvvmdemo.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.ItemImageBinding;
import com.desaysv.mvvmdemo.db.bean.WallPaper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 图片适配器
 */
public class ImageAdapter extends BaseQuickAdapter<WallPaper, BaseDataBindingHolder<ItemImageBinding>> {

    public ImageAdapter(@Nullable List<WallPaper> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemImageBinding> bindingHolder, WallPaper wallPaper) {
        if (wallPaper == null) {
            return;
        }
        ItemImageBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setWallPaper(wallPaper);
            binding.executePendingBindings();
        }
    }
}
