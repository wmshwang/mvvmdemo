package com.desaysv.mvvmdemo.ui.register;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;

import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;
import com.desaysv.mvvmdemo.repository.UserRepository;
import com.google.gson.Gson;

public class RegisterViewModel extends BaseViewModel {
    private final UserRepository userRepository;
    public MutableLiveData<User> user;

    public MutableLiveData<User> getUser(){
        if(user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }

    @ViewModelInject
    RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 注册
     */
    public void register() {
        failed = userRepository.failed;
        user.getValue().setUid(1);
        Log.d("TAG", "register: "+new Gson().toJson(user.getValue()));
        userRepository.saveUser(user.getValue());
    }
}