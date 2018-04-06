package com.enroute.enroute.interfaces;

import com.enroute.enroute.model.User;

import java.util.ArrayList;

/**
 * This interface callbacks the a User instance from searching user in Firebase
 * @author Haofan Hou
 */
public interface UserCallbacks {

    void onUserCallback(User user);

}
