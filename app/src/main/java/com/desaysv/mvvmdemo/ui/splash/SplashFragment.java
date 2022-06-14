package com.desaysv.mvvmdemo.ui.splash;

import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;

import com.desaysv.mvvmdemo.BaseApplication;
import com.desaysv.mvvmdemo.databinding.FragmentSplashBinding;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;
import com.desaysv.mvvmdemo.utils.Constant;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends BaseFragment<FragmentSplashBinding, BaseViewModel> {
    private Handler handler = new Handler(Looper.getMainLooper());
    private SavedStateHandle savedStateHandle;

    @Override
    protected void initView() {
        if (savedStateHandle == null)
            savedStateHandle = NavHostFragment.findNavController(this)
                    .getPreviousBackStackEntry().getSavedStateHandle();
        savedStateHandle.set(Constant.IS_SPLASH, false);
    }

    @Override
    protected void dataObserve() {

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            BaseApplication.getInstance().setSplash(true);
            savedStateHandle.set(Constant.IS_SPLASH, true);
            NavHostFragment.findNavController(SplashFragment.this).popBackStack();
        }
    };
}