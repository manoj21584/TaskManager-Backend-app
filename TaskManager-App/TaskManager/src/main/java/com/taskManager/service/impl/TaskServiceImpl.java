package com.taskManager.service.impl;

import com.taskManager.entity.Task;
import com.taskManager.entity.User;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.repository.TaskRepository;
import com.taskManager.repository.UserRepository;
import com.taskManager.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private WebClient webClient;

//    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository, WebClient.Builder webClientBuilder) {
//    }

    @Override
public String createNewTask(Task task, String timeZone,String firstName,String lastName,boolean isActive) {
    ZonedDateTime utcDateTime;

    try {
        // Try to convert the current time to the provided time zone
        LocalDateTime currentDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = currentDateTime.atZone(ZoneId.of(timeZone));
        utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    } catch (Exception e) {
        // If the provided time zone is invalid, fall back to UTC
        utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String utcDateTimeString = utcDateTime.format(formatter);

    task.setCreatedAt(utcDateTimeString);
    task.setUpdatedAt("Not updated yet");

    // Create and save the user
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setActive(isActive);
    user.setTimeZone(timeZone); // You may want to handle the user's time zone similarly
    userRepository.save(user);

    // Associate the task with the user and save the task
    task.setUser(user);
    taskRepository.save(task);

    return "New task saved to DB successfully";
}


    @Override
    public List<Task> getAllTask(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Determine the sorting direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create a Pageable object with the specified page number, size, and sorting
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        // Retrieve the paginated list of tasks
        Page<Task> allTasksPage = taskRepository.findAll(pageable);

        // Get the content from the page
        List<Task> tasks = allTasksPage.getContent();

        // If no tasks are found, throw an exception
        if (tasks.isEmpty()) {
            throw new RuntimeException("No tasks found");
        }

        // Fetch user details for each task and associate the user with the task
        List<Task> updatedTasks = tasks.stream().map(task -> {
            User user = webClient.get()
                    .uri("http://localhost:8080/api/user/" + task.getId())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            task.setUser(user);
            return task;
        }).collect(Collectors.toList());

        return updatedTasks;
    }




    @Override
    public Task getSingleTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("task","id",id));
        return task;
    }

    @Override
    public String updateTask(Task task, Long id, String timeZone,String firstName,String lastName,boolean isActive) {

        // Retrieve the task from the database, or throw an exception if not found
        Task taskFromDb = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task", "id", id));

        ZonedDateTime utcDateTime;

        try {
            // Try to convert the current time to the provided time zone
            LocalDateTime currentDateTime = LocalDateTime.now();
            ZonedDateTime zonedDateTime = currentDateTime.atZone(ZoneId.of(timeZone));
            utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        } catch (Exception e) {
            // If the provided time zone is invalid, fall back to UTC
            utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String utcDateTimeString = utcDateTime.format(formatter);

        // Update the task with new details
        taskFromDb.setUpdatedAt(utcDateTimeString);
        taskFromDb.setTitle(task.getTitle());
        taskFromDb.setStatus(task.getStatus());
        taskFromDb.setDescription(task.getDescription());

        // Uncomment and modify the code below if you need to update the User associated with the Task

    User user = webClient.get()
            .uri("http://localhost:8080/api/user/" +id)
            .retrieve()
            .bodyToMono(User.class)
            .block();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setActive(isActive);

    taskFromDb.setUser(user);


        // Save the updated task to the database
        userRepository.save(user);
        taskRepository.save(taskFromDb);

        return "Task updated successfully";
    }


    @Override
    public String deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("task", "id", id));
            taskRepository.deleteById(id);
        return "deleted Successfully";
    }
}
