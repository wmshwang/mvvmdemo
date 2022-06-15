package com.desaysv.mvvmdemo.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.DialogEditBinding;
import com.desaysv.mvvmdemo.databinding.DialogModifyUserInfoBinding;
import com.desaysv.mvvmdemo.databinding.HomeFragmentBinding;
import com.desaysv.mvvmdemo.databinding.NavHeaderBinding;
import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.utils.Constant;
import com.desaysv.mvvmdemo.view.dialog.AlertDialog;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private User localUser;

    //可输入弹窗
    private AlertDialog editDialog = null;
    //修改用户信息弹窗
    private AlertDialog modifyUserInfoDialog = null;

    private NavBackStackEntry navBackStackEntry;
    private SavedStateHandle savedStateHandle;

    @Override
    public void onAttachFragment(@NonNull @NotNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    protected void initView() {
        viewBinding.setHomeViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        if (savedStateHandle == null){
            navBackStackEntry = NavHostFragment.findNavController(this).getPreviousBackStackEntry();
            savedStateHandle = navBackStackEntry.getSavedStateHandle();
            savedStateHandle.set(Constant.IS_LOGIN_OUT, false);
        }

        //显示加载弹窗
        showLoading();
    }

    @Override
    protected void dataObserve() {
        //获取navController
        NavHostFragment mNavHostFragment = (NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = mNavHostFragment.getNavController();
        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.info_fragment:
                    viewBinding.tvTitle.setText("热门资讯");
                    navController.navigate(R.id.info_fragment);
                    break;
                case R.id.map_fragment:
                    viewBinding.tvTitle.setText("地图天气");
                    navController.navigate(R.id.map_fragment);
                    break;
                default:
            }
            return true;
        });
        viewBinding.ivAvatar.setOnClickListener(v -> viewBinding.drawerLayout.open());
        viewBinding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_notebook://记事本
//                    jumpActivity(NotebookActivity.class);
                    break;
                case R.id.item_setting:

                    break;
                case R.id.item_about:
                    NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_aboutFragment);
                    break;
                case R.id.item_logout:
                    logout();
                    break;
                default:
                    break;
            }
            return true;
        });
        //获取NavigationView的headerLayout视图
        View headerView = viewBinding.navView.getHeaderView(0);
        headerView.setOnClickListener(v -> showModifyUserInfoDialog());
        //获取headerLayout视图的Binding
        NavHeaderBinding headerBinding = DataBindingUtil.bind(headerView);
        //获取本地用户信息
        viewModel.getUser();
        //用户信息发生改变时给对应的xml设置数据源也就是之前写好的ViewModel。
        viewModel.user.observe(this, user -> {
            localUser = user;
            if (headerBinding != null) {
                headerBinding.setHomeViewModel(viewModel);
            }
            //隐藏加载弹窗
            dismissLoading();
        });
    }

    /**
     * 显示修改用户弹窗
     */
    private void showModifyUserInfoDialog() {
        DialogModifyUserInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_modify_user_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .addDefaultAnimation()
                .setCancelable(true)
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(300f), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_modify_nickname, v -> showEditDialog(0))//修改昵称
                .setOnClickListener(R.id.tv_modify_Introduction, v -> showEditDialog(1))//修改简介
                .setOnClickListener(R.id.tv_close, v -> modifyUserInfoDialog.dismiss())//关闭弹窗
                .setOnDismissListener(dialog -> LogUtils.d("showModifyUserInfoDialog"));
        modifyUserInfoDialog = builder.create();
        modifyUserInfoDialog.show();
    }

    /**
     * 显示可输入文字弹窗
     *
     * @param type 0 修改昵称  1  修改简介
     */
    private void showEditDialog(int type) {
        modifyUserInfoDialog.dismiss();
        DialogEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .addDefaultAnimation()
                .setCancelable(true)
                .setText(R.id.tv_title, type == 0 ? "修改昵称" : "修改简介")
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px( 300f), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_cancel, v -> editDialog.dismiss())
                .setOnClickListener(R.id.tv_sure, v -> {
                    String content = binding.etContent.getText().toString().trim();
                    if (content.isEmpty()) {
                        showMsg(type == 0 ? "请输入昵称" : "请输入简介");
                        return;
                    }
                    if (type == 0 && content.length() > 10) {
                        showMsg("昵称过长，请输入8个以内汉字或字母");
                        return;
                    }
                    editDialog.dismiss();
                    showLoading();
                    //保存输入的值
                    modifyContent(type, content);
                });
        editDialog = builder.create();
        binding.etContent.setHint(type == 0 ? "请输入昵称" : "请输入简介");
        editDialog.show();
    }

    /**
     * 修改内容
     *
     * @param type    类型 0：昵称 1：简介 2: 头像
     * @param content 修改内容
     */
    private void modifyContent(int type, String content) {
        if (type == 0) {
            localUser.setNickname(content);
        } else if (type == 1) {
            localUser.setIntroduction(content);
        } else if (type == 2) {
            localUser.setAvatar(content);
        }
        viewModel.updateUser(localUser);
        viewModel.failed.observe(this, failed -> {
            dismissLoading();
            if ("200".equals(failed)) {
                showMsg("修改成功");
            }
        });
    }

    /**
     * 退出登录
     */
    private void logout() {
        showMsg("退出登录");
        SPUtils.getInstance().put(Constant.IS_LOGIN, false);
        savedStateHandle.set(Constant.IS_LOGIN_OUT, true);
        NavHostFragment.findNavController(this).popBackStack();
    }
}