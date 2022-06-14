package com.desaysv.mvvmdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.db.dao.UserDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * 用户数据提供
 */
public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();
    private UserDao userDao;

    @Inject
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public MutableLiveData<User> getUser(){
        Flowable<List<User>> all = userDao.getAll();
        CustomDisposable.addDisposable(all, users ->{
            if (users != null && users.size() > 0) {
                for (User user : users) {
                    if (user.getUid() == 1) {
                        userMutableLiveData.postValue(user);
                        break;
                    }
                }
            } else {
                failed.postValue("你还没有注册过吧，去注册吧！");
            }
        });
        return userMutableLiveData;
    }

    /**
     * 更新用户信息
     * @param user
     */
    public void updateUser(User user){
        Completable update = userDao.update(user);
        CustomDisposable.addDisposable(update, () -> {
            Log.d(TAG, "updateUser: " + "保存成功");
            failed.postValue("200");
        });
    }

    /**
     * 保存用户
     */
    public void saveUser(User user) {
        Completable deleteAll = userDao.deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            //保存到数据库
            Completable insertAll = userDao.insert(user);
            //RxJava处理Room数据存储
            CustomDisposable.addDisposable(insertAll, () -> failed.postValue("200"));
        });
    }
}
