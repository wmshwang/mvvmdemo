package com.desaysv.mvvmdemo.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.ItemNewsBinding;
import com.desaysv.mvvmdemo.model.NewsResponse;

import org.jetbrains.annotations.NotNull;

/**
 * 新闻列表适配器
 */
public class NewsAdapter extends BaseQuickAdapter<NewsResponse.ResultBean.DataBean, BaseDataBindingHolder<ItemNewsBinding>> {

    public NewsAdapter() {
        super(R.layout.item_news);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemNewsBinding> bindingHolder, NewsResponse.ResultBean.DataBean dataBean) {
        ItemNewsBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setNews(dataBean);
            binding.executePendingBindings();
        }
    }
}
