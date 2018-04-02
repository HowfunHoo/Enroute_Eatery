package com.enroute.enroute.interfaces;

import com.enroute.enroute.model.User;

import java.util.ArrayList;

/**
 * Created by haoyu on 2018/4/2.
 */

public interface UserCallbacks {

    void onUserCallback(ArrayList<User> users);

}
