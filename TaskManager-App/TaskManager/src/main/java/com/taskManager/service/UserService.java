package com.taskManager.service;

import com.taskManager.entity.User;

public interface UserService {
    String createUserToDb(User user);
    String updateUserInDb(User user,Long id);
    String deleteUserInDb(Long id);
    User getUserById(Long id);
}
