package com.ashish.tasknotificationsystem.Service;

import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Task;
import com.ashish.tasknotificationsystem.Enum.Status;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);

    TaskDto addSubTask(SubtaskDto subtaskDto);

    TaskDto updateSubtask(SubtaskDto updatedSubtaskDto);

    List<TaskDto> getTodaysTask();

    TaskDto deleteSubtaskById(SubtaskDto updatedSubtaskDto);

    TaskDto getTaskById(Integer id);
    List<TaskDto> getAllTasks();

    TaskDto changeTaskStatus(Integer taskId, Status status);

    Task updateTask(Task updatedTask);
    void deleteTask(Integer id);
}
