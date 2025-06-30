package com.ashish.tasknotificationsystem.Schedules;

import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.Task;
import com.ashish.tasknotificationsystem.Mapper.TaskMapper;
import com.ashish.tasknotificationsystem.PubSubUtils.RmqPublisherUtil;
import com.ashish.tasknotificationsystem.Repository.AssigneeRepository;
import com.ashish.tasknotificationsystem.Repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class TaskSchedules {

    private final TaskRepository taskRepository;
    private final AssigneeRepository assigneeRepository;
    private final RmqPublisherUtil rmqPublisherUtil;

    @Autowired
    public TaskSchedules(TaskRepository taskRepository, AssigneeRepository assigneeRepository, RmqPublisherUtil rmqPublisherUtil) {
        this.taskRepository = taskRepository;
        this.assigneeRepository = assigneeRepository;
        this.rmqPublisherUtil = rmqPublisherUtil;
    }

    private List<TaskDto> getNotCompletedTaskOnADay(Assignee assignee, LocalDate date){
        String username = assignee.getUsername();

        List<Task> notCompletedTasks = taskRepository.findAllNotCompletedTasks(username);

        return notCompletedTasks.stream().filter(task -> task.getDueDate().isEqual(date)).map(TaskMapper::taskToDto).toList();
    }

    private List<TaskDto> getOverdueTaskOnADay(Assignee assignee, LocalDate date){
        String username = assignee.getUsername();

        List<Task> notCompletedTasks = taskRepository.findAllNotCompletedTasks(username);

        return notCompletedTasks.stream().filter(task -> task.getDueDate().isBefore(date)).map(TaskMapper::taskToDto).toList();
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Retryable(maxAttempts = 3)
    public void sendMorningTasks(){
        List<Assignee> appUsers = assigneeRepository.findAll();

        for(Assignee assignee : appUsers){

            List<TaskDto> taskDos = getNotCompletedTaskOnADay(assignee, LocalDate.now());

            if(!taskDos.isEmpty()) {
                taskDos.forEach( task ->
                        rmqPublisherUtil.publishTasksToRmq(assignee, task)
                );
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Retryable(maxAttempts = 3)
    public void sendEveningReminders() {
        List<Assignee> appUsers = assigneeRepository.findAll();

        for(Assignee assignee : appUsers){

            List<TaskDto> taskDos = getNotCompletedTaskOnADay(assignee,LocalDate.now());

            if(!taskDos.isEmpty()) {
                taskDos.forEach(task -> {
                    if(calCompletedPercentage(task.getSubtasks()) < 50){
                        rmqPublisherUtil.publishTasksToRmq(assignee, task);
                    }
                });
            }
        }
    }

    @Scheduled(cron = "0 0 18 * * *")
    @Retryable(maxAttempts = 3)
    public void sendOverdueTaskNotifications() {
        List<Assignee> appUsers = assigneeRepository.findAll();

        for(Assignee assignee : appUsers){

            List<TaskDto> taskDos = getOverdueTaskOnADay(assignee,LocalDate.now());

            if(!taskDos.isEmpty()) {
                taskDos.forEach( task ->
                        rmqPublisherUtil.publishTasksToRmq(assignee, task)
                );
            }
        }
    }

    private double calCompletedPercentage(List<SubtaskDto> subtasks){

        double total = subtasks.size();

        double completedSubtasks = subtasks.stream().filter(SubtaskDto::isCompleted).toList().size();

        return (completedSubtasks/total)*100;

    }

}
