package com.taskManager.controller;

import com.taskManager.entity.Task;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.service.TaskService;
import com.taskManager.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private TaskService taskService;
    @PostMapping
    public ResponseEntity<String> createTask(@Valid @RequestBody Task task,@RequestParam String timeZone,@RequestParam String firstName,@RequestParam String lastName,@RequestParam boolean isActive){
        String response = taskService.createNewTask(task,timeZone,firstName,lastName,isActive);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<Task>> retriveAllTask(

            @RequestParam(value = "pageNo",defaultValue =AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value="pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.DEFAULT_SOR_BY,required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        try {
            List<Task> allTasks = taskService.getAllTask(pageNo, pageSize,sortBy,sortDir);
            return ResponseEntity.ok(allTasks);
        } catch (ResourceNotFoundException e) {
            // Return a 404 response with an empty list or custom message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }


    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> retriveSingleTask(@PathVariable Long id){
        Task singleTask = taskService.getSingleTask(id);
        return ResponseEntity.ok(singleTask);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTaskById(@Valid @RequestBody Task task,@PathVariable Long id,@RequestParam String timeZone,@RequestParam String firstName,@RequestParam String lastName,@RequestParam boolean isActive){
        String task1 = taskService.updateTask(task, id,timeZone,firstName,lastName,isActive);
        return ResponseEntity.ok(task1);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id){
        String statusOfDeletedTask = taskService.deleteTaskById(id);
        return ResponseEntity.ok(statusOfDeletedTask);


    }

}
