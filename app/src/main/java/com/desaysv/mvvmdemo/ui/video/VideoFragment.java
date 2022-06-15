package com.desaysv.mvvmdemo.ui.video;

import android.content.Intent;
import android.net.Uri;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.VideoFragmentBinding;
import com.desaysv.mvvmdemo.ui.adapter.VideoAdapter;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VideoFragment extends BaseFragment<VideoFragmentBinding, VideoViewModel> {
    private VideoAdapter videoAdapter;

    @Override
    protected void initView() {
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        //获取视频数据
        viewModel.getVideo();
        viewBinding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        videoAdapter = new VideoAdapter();
        videoAdapter.addChildClickViewIds(R.id.detail);
        viewBinding.rv.setAdapter(videoAdapter);
    }

    @Override
    protected void dataObserve() {
        //数据刷新
        viewModel.video.observe(getViewLifecycleOwner(), videoResponse ->
                videoAdapter.setList(videoResponse.getResult()));
        viewModel.failed.observe(getViewLifecycleOwner(), this::showMsg);

        // 列表点击
        videoAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.detail){
                if (videoAdapter.getData().get(position).getShare_url() != null) {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoAdapter.getData().get(position).getShare_url())));
                } else {
                    showMsg("视频地址为空");
                }
            }
        });
    }
}