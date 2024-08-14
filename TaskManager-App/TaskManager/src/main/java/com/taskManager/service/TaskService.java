package com.taskManager.service;

import com.taskManager.entity.Task;

import java.util.List;

public interface TaskService {
     String createNewTask(Task task,String timeZone,String firstName,String lastName,boolean isActive);
     List<Task> getAllTask(int pageNo,int pageSize,String sortBy,String sortDir);
     Task getSingleTask(Long id);
     String updateTask(Task task, Long id, String timeZone,String firstName,String lastName,boolean isActive);
     String deleteTaskById(Long id);


}
