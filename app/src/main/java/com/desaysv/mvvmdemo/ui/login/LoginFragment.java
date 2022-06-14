package com.desaysv.mvvmdemo.ui.login;

import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.blankj.utilcode.util.SPUtils;
import com.desaysv.mvvmdemo.R;
import com.desaysv.mvvmdemo.databinding.LoginFragmentBinding;
import com.desaysv.mvvmdemo.model.User;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.utils.Constant;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<LoginFragmentBinding, LoginViewModel> {
    private SavedStateHandle savedStateHandle;

    @Override
    protected void initView() {
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        if (savedStateHandle == null)
            savedStateHandle = NavHostFragment.findNavController(this)
                    .getPreviousBackStackEntry().getSavedStateHandle();
        savedStateHandle.set(Constant.IS_LOGIN, false);
        viewModel.getUser().setValue(new User("", ""));
    }

    @Override
    protected void dataObserve() {
        viewBinding.btnLogin.setOnClickListener(v -> {
            if (viewModel.getUser().getValue().getAccount().isEmpty()) {
                showMsg("请输入账号");
                return;
            }
            if (viewModel.getUser().getValue().getPwd().isEmpty()) {
                showMsg("请输入密码");
                return;
            }
            //检查输入的账户和密码是否是注册过的。
            checkUser();
        });

        //跳转到注册界面
        viewBinding.register.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));
    }

    private void checkUser() {
        viewModel.getLocalUser();

        viewModel.localUser.observe(getViewLifecycleOwner(), localUser -> {
            if (!viewModel.getUser().getValue().getAccount().equals(localUser.getAccount()) ||
                    !viewModel.getUser().getValue().getPwd().equals(localUser.getPwd())) {
                showMsg("账号或密码错误");
                return;
            }
            //记录已经登录过
            SPUtils.getInstance().put(Constant.IS_LOGIN, true);
            showMsg("登录成功");
            savedStateHandle.set(Constant.IS_LOGIN, true);
            NavHostFragment.findNavController(this).popBackStack();
        });
        viewModel.failed.observe(getViewLifecycleOwner(), this::showMsg);
    }
}