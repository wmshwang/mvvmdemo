package com.desaysv.mvvmdemo.ui.home;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;

import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.repository.UserRepository;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    public LiveData<User> user;

    public String defaultName = "初学者-Study";
    public String defaultIntroduction = "Android | Java";
    private final UserRepository userRepository;

    @ViewModelInject
    public HomeViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void getUser() {
        user = userRepository.getUser();
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
        failed = userRepository.failed;
        getUser();
    }
}