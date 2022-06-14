package com.desaysv.mvvmdemo.ui.base;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.LogUtils;
import com.desaysv.mvvmdemo.view.dialog.LoadingDialog;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 基础Fragment
 */
public abstract class BaseFragment<VB extends ViewBinding, VM extends BaseViewModel> extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    public VB viewBinding;
    public VM viewModel;
    private LoadingDialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewBinding == null) {
            Type superclass = getClass().getGenericSuperclass();
            Class<VB> aClass = (Class<VB>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
            Class<VM> bClass = (Class<VM>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
            try {
                Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
                viewBinding = (VB) method.invoke(null, getLayoutInflater());
                viewModel = new ViewModelProvider(this).get(bClass);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                LogUtils.d(TAG, "onCreateView 反射出问题啦：" + e.getMessage());
            }
            initView();
            dataObserve();
        }
        return viewBinding.getRoot();
    }

    public VB getViewBinding() {
        return viewBinding;
    }

    public VM getViewModel() {
        return viewModel;
    }

    protected abstract void initView();

    protected abstract void dataObserve();

    protected void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected boolean isNight() {
        UiModeManager uiModeManager = (UiModeManager) getContext().getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    /**
     * 显示加载弹窗
     */
    protected void showLoading() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    protected void showLoading(boolean isClose) {
        loadingDialog = new LoadingDialog(getContext(), isClose);
        loadingDialog.show();
    }

    /**
     * 隐藏加载弹窗
     */
    protected void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
