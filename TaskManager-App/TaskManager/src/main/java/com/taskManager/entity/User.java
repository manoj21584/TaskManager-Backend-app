package com.taskManager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_Table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotEmpty(message = "firstName must not be null")
    private String firstName;
    @NotEmpty(message = "last must not be null")
    private String lastName;

    private String timeZone;
    @NotNull(message = "status must not be null")
    private boolean isActive;


//@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
//private Task task;
}
