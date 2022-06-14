package com.desaysv.mvvmdemo.ui.register;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.desaysv.mvvmdemo.databinding.RegisterFragmentBinding;
import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends BaseFragment<RegisterFragmentBinding, RegisterViewModel> {

    @Override
    protected void initView() {
        viewModel.getUser().setValue(new User(0, "", "", "", null, null));
        viewBinding.setViewModel(viewModel);
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    protected void dataObserve() {
        viewBinding.toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).popBackStack());

        viewBinding.btnRegister.setOnClickListener(v -> {
            if (viewModel.user.getValue().getAccount().isEmpty()) {
                showMsg("请输入账号");
                return;
            }
            if (viewModel.user.getValue().getPwd().isEmpty()) {
                showMsg("请输入密码");
                return;
            }
            if (viewModel.user.getValue().getConfirmPwd().isEmpty()) {
                showMsg("请确认密码");
                return;
            }
            if (!viewModel.user.getValue().getPwd().equals(viewModel.user.getValue().getConfirmPwd())) {
                showMsg("两次输入密码不一致");
                return;
            }

            viewModel.register();
            viewModel.failed.observe(getViewLifecycleOwner(), failed -> {
                showMsg("200".equals(failed) ? "注册成功" : failed);
                if ("200".equals(failed)) {
                    NavHostFragment.findNavController(this).popBackStack();
                }
            });
        });

    }
}