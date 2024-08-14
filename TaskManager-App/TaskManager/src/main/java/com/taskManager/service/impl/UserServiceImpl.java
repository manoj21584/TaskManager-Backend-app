package com.taskManager.service.impl;

import com.taskManager.entity.User;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.repository.UserRepository;
import com.taskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Override
    public String createUserToDb(User user) {
        user.setTimeZone(user.getTimeZone());
        userRepository.save(user);
        return "User saved to db";
    }

    @Override
    public String updateUserInDb(User user, Long id) {
        User userFetchFormDb=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user","id",id));
        userFetchFormDb.setFirstName(user.getFirstName());
        userFetchFormDb.setLastName(user.getLastName());
        userFetchFormDb.setActive(user.isActive());
        userRepository.save(userFetchFormDb);

        return "updated successfully";
    }

    @Override
    public String deleteUserInDb(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @Override
    public User getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
return user;
    }
}
