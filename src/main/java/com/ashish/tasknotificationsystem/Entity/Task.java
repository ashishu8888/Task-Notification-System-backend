package com.ashish.tasknotificationsystem.Entity;

import com.ashish.tasknotificationsystem.Enum.Priority;
import com.ashish.tasknotificationsystem.Enum.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String body;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Assignee assignedTo;

    private LocalDate dueDate;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate completedAt;

    @OneToMany(mappedBy = "parentTask" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subtask> subtasks;

    private List<String> tags;

    private List<String> attachments;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDate.now();
    }
}
