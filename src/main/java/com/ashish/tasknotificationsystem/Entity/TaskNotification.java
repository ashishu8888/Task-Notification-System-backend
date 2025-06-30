package com.ashish.tasknotificationsystem.Entity;


import com.ashish.tasknotificationsystem.Dto.TaskDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskNotification {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate date;
    private TaskDto task;
}
