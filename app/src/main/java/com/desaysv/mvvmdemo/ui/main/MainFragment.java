package com.desaysv.mvvmdemo.ui.main;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.desaysv.mvvmdemo.BaseApplication;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.FragmentMainBinding;
import com.desaysv.mvvmdemo.ui.adapter.WallPaperAdapter;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.utils.Constant;
import com.google.android.material.appbar.AppBarLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> {
    private static final String TAG = MainFragment.class.getSimpleName();
    private NavBackStackEntry navBackStackEntry;
    private SavedStateHandle savedStateHandle;
    private WallPaperAdapter wallPaperAdapter;
    @Override
    protected void initView() {
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        viewBinding.rv.setLayoutManager(manager);
        wallPaperAdapter = new WallPaperAdapter();
        viewBinding.rv.setAdapter(wallPaperAdapter);

        getBackStackStateHandle();//获取navBackStackEntry和savedStateHandle
        judeToSplash();//启动时判断是否进入开屏页
        gotoMain();//开屏页倒计时结束进入主页
        loginLister();//登录成功监听
    }

    @Override
    protected void dataObserve() {
        //伸缩偏移量监听
        viewBinding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//收缩时
                    viewBinding.toolbarLayout.setTitle("每日壁纸");
                    isShow = true;
                } else if (isShow) {//展开时
                    viewBinding.toolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
        //页面上下滑动监听
        viewBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                //上滑
                viewBinding.fabHome.hide();
            } else {
                //下滑
                viewBinding.fabHome.show();
            }
        });

        //跳转到HomeActivity
        viewBinding.fabHome.setOnClickListener(v -> {
//                jumpActivity(HomeActivity.class);
        });

        //wall 列表点击
       wallPaperAdapter.setOnItemChildClickListener((adapter, view, position) -> {
//                Intent intent = new Intent(view.getContext(), PictureViewActivity.class);
//                intent.putExtra("img", verticalBean.getImg());
//                view.getContext().startActivity(intent);
       });
    }

    //获取navBackStackEntry和savedStateHandle
    private void getBackStackStateHandle(){
        navBackStackEntry = NavHostFragment.findNavController(this).getCurrentBackStackEntry();
        if (navBackStackEntry != null) {
            savedStateHandle = navBackStackEntry.getSavedStateHandle();
        }
    }

    //启动时判断是否进入开屏页
    private void judeToSplash(){
        if (BaseApplication.getInstance().isSplash()){//应用已经进入启动页
            judeToLogin();//判断是否进入登录界面
        }else {
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_splashFragment);
        }
    }

    //开屏页倒计时结束进入主页
    private void gotoMain(){
        if (navBackStackEntry != null){
            MutableLiveData<Boolean> liveData = savedStateHandle.getLiveData(Constant.IS_SPLASH);
            liveData.observe(navBackStackEntry, success -> {
                if (success) {
                    savedStateHandle.set(Constant.IS_SPLASH, false);
                    judeToLogin();//判断是否进入登录界面
                }
            });
        }
    }

    //判断是否进入登录界面
    private void judeToLogin(){
        if (SPUtils.getInstance().getBoolean(Constant.IS_LOGIN, false)){//已登录
            LogUtils.d(TAG, " onActivityCreated已登录 ");
            showMainLayout();//加载首页布局填充数据
        }else{//未登录
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_loginFragment);
        }
    }

    //登录成功监听
    private void loginLister(){
        if (navBackStackEntry != null){
            MutableLiveData<Boolean> liveData = savedStateHandle.getLiveData(Constant.IS_LOGIN);
            liveData.observe(navBackStackEntry, (Observer<Boolean>) success -> {
                    if (success) {//登录成功
                        savedStateHandle.set(Constant.IS_LOGIN, false);
                        LogUtils.d(TAG, " 登录成功 ");
                        showMainLayout();//加载首页布局填充数据
                    }
                });
        }
    }

    //加载首页布局填充数据
    private void showMainLayout(){
        showLoading();//显示加载弹窗
        //必应网络请求
        viewModel.getBiying();
        //热门壁纸 网络请求
        viewModel.getWallPaper();
        viewModel.wallPaper.observe(this, wallPaperResponse -> {
            wallPaperAdapter.setList(wallPaperResponse.getRes().getVertical());
            dismissLoading();
        });
        viewModel.failed.observe(this, failed -> dismissLoading());
    }
}