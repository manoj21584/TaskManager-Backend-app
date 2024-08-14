package com.taskManager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Instant;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="Task_Table")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "title must not be empty")
    private String title;
    @NotEmpty(message = "description must not be empty")
    private String description;
    @NotEmpty(message = "Status must not be empty")
    @Pattern(regexp = "Pending|In Progress|Completed", message = "Status must be either 'pending', 'inprogress', or 'completed'")
    private String status;
    private String createdAt;
    private String updatedAt;

//    private Instant dueDate;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

}
