package com.desaysv.mvvmdemo.ui.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.navigation.fragment.NavHostFragment;

import com.desaysv.mvvmdemo.databinding.FragmentAboutBinding;
import com.desaysv.mvvmdemo.ui.base.BaseFragment;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.CLIPBOARD_SERVICE;


@AndroidEntryPoint
public class AboutFragment extends BaseFragment<FragmentAboutBinding, BaseViewModel> {
    /**
     * 博客个人主页
     */
    private final String CSDN = "https://llw-study.blog.csdn.net/";
    /**
     * 博客地址
     */
    private final String CSDN_BLOG_URL = "https://blog.csdn.net/qq_38436214/category_11482619.html";
    /**
     * 源码地址
     */
    private final String GITHUB_URL = "https://github.com/lilongweidev/MVVM-Demo";

    @Override
    protected void initView() {
        viewBinding.setLifecycleOwner(getViewLifecycleOwner());

        viewBinding.tvVersion.setText(getVerName(getContext()));

    }

    @Override
    protected void dataObserve() {
        viewBinding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        viewBinding.tvBlog.setOnClickListener(v -> jumpUrl(CSDN_BLOG_URL));
        viewBinding.tvCode.setOnClickListener(v -> jumpUrl(GITHUB_URL));
        viewBinding.tvCopyEmail.setOnClickListener(v -> copyEmail());
        viewBinding.tvAuthor.setOnClickListener(v -> jumpUrl(CSDN));
    }

    /**
     * 获取版本号名称
     * @param context 上下文
     * @return
     */
    private String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    private void jumpUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void copyEmail() {
        ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", "lonelyholiday@qq.com");
        myClipboard.setPrimaryClip(myClip);
        showMsg("邮箱已复制");
    }
}