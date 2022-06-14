package com.desaysv.mvvmdemo.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.ItemWallPaperBinding;
import com.desaysv.mvvmdemo.model.WallPaperResponse;

import org.jetbrains.annotations.NotNull;

/**
 * 壁纸列表适配器
 */
public class WallPaperAdapter extends BaseQuickAdapter<WallPaperResponse.ResBean.VerticalBean, BaseDataBindingHolder<ItemWallPaperBinding>> {

    public WallPaperAdapter() {
        super(R.layout.item_wall_paper);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemWallPaperBinding> bindingHolder, WallPaperResponse.ResBean.VerticalBean verticalBean) {
        if (verticalBean == null) {
            return;
        }
        // 获取 Binding
        ItemWallPaperBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setWallPaper(verticalBean);
            binding.executePendingBindings();
        }
    }
}
