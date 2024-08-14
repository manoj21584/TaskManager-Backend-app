package com.taskManager.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskManager.entity.Task;
import com.taskManager.entity.User;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.repository.TaskRepository;
import com.taskManager.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks // This will automatically inject the mocks into TaskServiceImpl
    private TaskServiceImpl taskServiceImpl;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

//        // Mock the WebClient.Builder behavior
//        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
//        when(webClientBuilder.build()).thenReturn(webClient);

        // No need to manually instantiate taskServiceImpl, @InjectMocks will handle it
    }


    @Test
    void testCreateNewTask_Success() {
        Task task = new Task();
        User user = new User();
        String timeZone = "Asia/Kolkata";
        String firstName = "John";
        String lastName = "Doe";
        boolean isActive = true;

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        String result = taskServiceImpl.createNewTask(task, timeZone, firstName, lastName, isActive);

        assertEquals("New task saved to DB successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetSingleTask_Success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("description");

        User user = new User();
        user.setFirstName("Manoj");
        user.setLastName("Kumar");
        user.setActive(false);
        task.setUser(user);

        Task task1 = new Task();
        task1.setId(2L);
        task1.setTitle("title2");
        task1.setDescription("description2");

        User user1 = new User();
        user1.setFirstName("Mahi");
        user1.setLastName("Soni");
        user1.setActive(true);
        task1.setUser(user1);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));

        Task result = taskServiceImpl.getSingleTask(2L);
        assertNotNull(result);
        assertEquals("title2",result.getTitle());
        verify(taskRepository, times(1)).findById(anyLong());
    }
    //
//    @Test
//    void testUpdateTask_Success() throws Exception {
//        Task task = new Task();
//        task.setTitle("Updated Title");
//        task.setStatus("In Progress");
//        task.setDescription("Updated Description");
//
//        Task taskFromDb = new Task();
//        taskFromDb.setId(1L);
//        taskFromDb.setTitle("Old Title");
//        taskFromDb.setStatus("Old Status");
//        taskFromDb.setDescription("Old Description");
//
//        User user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setActive(true);
//
//        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskFromDb));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(taskRepository.save(any(Task.class))).thenReturn(taskFromDb);
//
//        mockWebServer.enqueue(new MockResponse()
//                .setBody(mapper.writeValueAsString(user))
//                .addHeader("Content-Type", "application/json"));
//
//        String result = taskServiceImpl.updateTask(task, 1L, "Asia/Kolkata", "John", "Doe", true);
//
//        assertEquals("Task updated successfully", result);
//        assertEquals("Updated Title", taskFromDb.getTitle());
//        assertEquals("In Progress", taskFromDb.getStatus());
//        assertEquals("Updated Description", taskFromDb.getDescription());
//        assertEquals("John", taskFromDb.getUser().getFirstName());
//        assertEquals("Doe", taskFromDb.getUser().getLastName());
//        assertTrue(taskFromDb.getUser().isActive());
//        verify(taskRepository, times(1)).findById(anyLong());
//        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(userRepository, times(1)).save(any(User.class));
//
//    }
//
    @Test
    void testUpdateTask_TaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            taskServiceImpl.updateTask(new Task(), 1L, "Asia/Kolkata", "John", "Doe", true);
        });

        assertEquals("task not found with id: '1'", thrown.getMessage());
        verify(taskRepository, times(1)).findById(anyLong());
        verifyNoInteractions(userRepository);
    }
    //
//    @Test
//    void testUpdateTask_InvalidTimeZone() throws Exception {
//        Task task = new Task();
//        task.setTitle("Updated Title");
//        task.setStatus("In Progress");
//        task.setDescription("Updated Description");
//
//        Task taskFromDb = new Task();
//        taskFromDb.setId(1L);
//        taskFromDb.setTitle("Old Title");
//        taskFromDb.setStatus("Old Status");
//        taskFromDb.setDescription("Old Description");
//
//        User user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setActive(true);
//
//        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskFromDb));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(taskRepository.save(any(Task.class))).thenReturn(taskFromDb);
//
//        mockWebServer.enqueue(new MockResponse()
//                .setBody(mapper.writeValueAsString(user))
//                .addHeader("Content-Type", "application/json"));
//
//        String result = taskServiceImpl.updateTask(task, 1L, "Invalid/TimeZone", "John", "Doe", true);
//
//        assertEquals("Task updated successfully", result);
//        assertEquals("Updated Title", taskFromDb.getTitle());
//        assertEquals("In Progress", taskFromDb.getStatus());
//        assertEquals("Updated Description", taskFromDb.getDescription());
//        assertEquals("John", taskFromDb.getUser().getFirstName());
//        assertEquals("Doe", taskFromDb.getUser().getLastName());
//        assertTrue(taskFromDb.getUser().isActive());
//        verify(taskRepository, times(1)).findById(anyLong());
//        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(userRepository, times(1)).save(any(User.class));
//
//    }
//
    @Test
    void testDeleteTaskById_Success() {
        Task task = new Task();
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        String result = taskServiceImpl.deleteTaskById(1L);

        assertEquals("deleted Successfully", result);
        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteTaskById_TaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            taskServiceImpl.deleteTaskById(1L);
        });

        assertEquals("task not found with id: '1'", thrown.getMessage());
        verify(taskRepository, times(1)).findById(anyLong());
        verifyNoInteractions(userRepository);
    }

}

