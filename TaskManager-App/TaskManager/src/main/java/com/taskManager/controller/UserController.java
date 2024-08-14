package com.taskManager.controller;

import com.taskManager.entity.User;
import com.taskManager.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user){
        String userToDb = userService.createUserToDb(user);
        return ResponseEntity.ok(userToDb);

    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@Valid @RequestBody User user ,@PathVariable Long id){
        String userToDb = userService.updateUserInDb(user,id);
        return ResponseEntity.ok(userToDb);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByIdUser(@PathVariable Long id){
        String userToDb = userService.deleteUserInDb(id);
        return ResponseEntity.ok(userToDb);

    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getSingleUser(@PathVariable Long id){
        User userById = userService.getUserById(id);
        return  ResponseEntity.ok(userById);
    }


}
