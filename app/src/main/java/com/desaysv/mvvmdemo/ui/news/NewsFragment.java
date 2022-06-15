package com.desaysv.mvvmdemo.ui.news;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.NewsFragmentBinding;
import com.desaysv.mvvmdemo.ui.adapter.NewsAdapter;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * 新闻
 */
@AndroidEntryPoint
public class NewsFragment extends BaseFragment<NewsFragmentBinding, NewsViewModel> {
    private NewsAdapter newsAdapter;

    @Override
    protected void initView() {
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
        //获取新闻数据
        viewModel.getNews();
        viewBinding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter();
        newsAdapter.addChildClickViewIds(R.id.detail);
        viewBinding.rv.setAdapter(newsAdapter);
    }

    @Override
    protected void dataObserve() {
        //数据刷新
        viewModel.news.observe(getViewLifecycleOwner(), newsResponse ->
                newsAdapter.setList(newsResponse.getResult().getData()));
        viewModel.failed.observe(getViewLifecycleOwner(), this::showMsg);

        //wall 列表点击
        newsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.detail){
                if("1".equals(newsAdapter.getData().get(position).getIs_content())){
//                    Intent intent = new Intent(view.getContext(), WebActivity.class);
//                    intent.putExtra("uniquekey", dataBean.getUniquekey());
//                    view.getContext().startActivity(intent);
                } else {
                    showMsg("没有详情信息");
                }
            }
        });
    }
}