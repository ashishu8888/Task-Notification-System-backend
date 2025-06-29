package com.ashish.tasknotificationsystem.Dto;

import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.Subtask;
import com.ashish.tasknotificationsystem.Enum.Priority;
import com.ashish.tasknotificationsystem.Enum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Integer id;

    private String title;

    private String body;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate dueDate;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate completedAt;

    private List<SubtaskDto> subtasks = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private List<String> attachments = new ArrayList<>();
}
