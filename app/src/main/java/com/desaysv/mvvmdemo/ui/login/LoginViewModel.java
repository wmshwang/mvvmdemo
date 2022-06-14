package com.desaysv.mvvmdemo.ui.login;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.desaysv.mvvmdemo.model.User;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;
import com.desaysv.mvvmdemo.repository.UserRepository;

public class LoginViewModel extends BaseViewModel {
    private final UserRepository userRepository;
    private MutableLiveData<User> user;
    public LiveData<com.desaysv.mvvmdemo.db.bean.User> localUser;

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    @ViewModelInject
    public LoginViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void getLocalUser(){
        localUser = userRepository.getUser();
        failed = userRepository.failed;
    }
}