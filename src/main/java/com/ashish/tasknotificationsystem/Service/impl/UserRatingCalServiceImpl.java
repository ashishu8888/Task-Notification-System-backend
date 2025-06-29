package com.ashish.tasknotificationsystem.Service.impl;

import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.Task;
import com.ashish.tasknotificationsystem.Enum.Priority;
import com.ashish.tasknotificationsystem.Enum.Status;
import com.ashish.tasknotificationsystem.Repository.AssigneeRepository;
import com.ashish.tasknotificationsystem.Service.UserRatingCalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserRatingCalServiceImpl implements UserRatingCalService {


    private final AssigneeRepository assigneeRepository;

    @Autowired
    public UserRatingCalServiceImpl(AssigneeRepository assigneeRepository){
        this.assigneeRepository = assigneeRepository;
    }

    @Override
    public void getUserRatingAndPersist() {
        List<Assignee> activeUsers = assigneeRepository.findAll();
        for(Assignee assignee : activeUsers){
            Double updatedRating = calcUserRating(assignee.getTaskList());
            assignee.setRating(updatedRating);
            assigneeRepository.save(assignee);
        }
    }

    private Double calcUserRating(List<Task> tasks) {

        if (tasks == null || tasks.isEmpty()) return 5.0;

        int total = tasks.size();
        int completed = 0;
        int onTime = 0;
        double penalty = 0;
        double reward = 0;
        double bonus = 0;

        LocalDate now = LocalDate.now();

        for (Task task : tasks) {
            Status status = task.getStatus();
            Priority priority = task.getPriority();

            boolean isHigh = priority == Priority.HIGH;
            boolean isMed = priority == Priority.MEDIUM;

            if (status == Status.COMPLETED) {
                completed++;

                if (task.getCompletedAt() != null &&
                        task.getDueDate() != null &&
                        !task.getCompletedAt().isAfter(task.getDueDate())) {
                    onTime++;
                }

                reward += isHigh ? 0.3 : isMed ? 0.2 : 0.1;

            } else if (status == Status.CANCELLED) {
                penalty += isHigh ? 0.6 : isMed ? 0.4 : 0.2;

            } else if (status != Status.COMPLETED &&
                    task.getDueDate() != null &&
                    task.getDueDate().isBefore(now)) {
                penalty += 0.3;
            }
        }

        long recentCompleted = tasks.stream()
                .filter(t -> t.getStatus() == Status.COMPLETED)
                .filter(t -> t.getCompletedAt() != null &&
                        t.getCompletedAt().isAfter(now.minusDays(30)))
                .count();

        if (recentCompleted >= 10) bonus += 0.5;

        double completionRate = (double) completed / total;
        double onTimeRate = (double) onTime / total;

        double score = 2.5 * completionRate + 1.5 * onTimeRate + reward + bonus - penalty;

        return Math.max(0.0, Math.min(5.0, score));
    }

}
