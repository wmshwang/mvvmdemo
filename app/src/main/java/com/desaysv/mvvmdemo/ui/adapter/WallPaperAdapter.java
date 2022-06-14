package com.desaysv.mvvmdemo.ui.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.ItemWallPaperBinding;
import com.desaysv.mvvmdemo.model.WallPaperResponse;

import org.jetbrains.annotations.NotNull;

/**
 * 壁纸列表适配器
 */
public class WallPaperAdapter extends BaseQuickAdapter<WallPaperResponse.ResBean.VerticalBean, BaseViewHolder> {

    public WallPaperAdapter() {
        super(R.layout.item_wall_paper);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        // 绑定 view
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, WallPaperResponse.ResBean.VerticalBean verticalBean) {
        if (verticalBean == null) {
            return;
        }
        // 获取 Binding
        ItemWallPaperBinding binding = baseViewHolder.getBinding();
        if (binding != null) {
            binding.setWallPaper(verticalBean);
            binding.executePendingBindings();
        }
    }
}
