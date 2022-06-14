package com.desaysv.mvvmdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.KeyEvent;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private NavHostFragment mNavHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_nav);
    }

    /**
     * 监听Back键按下事件
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mNavHostFragment != null && (mNavHostFragment.getNavController().getCurrentDestination().getId() == R.id.splashFragment
                    || mNavHostFragment.getNavController().getCurrentDestination().getId() == R.id.loginFragment)){
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}