package com.ashish.tasknotificationsystem.Service;

import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Task;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);

    TaskDto addSubTask(SubtaskDto subtaskDto);

    Task getTaskById(Integer id);
    List<TaskDto> getAllTasks();
    Task updateTask(Task updatedTask);
    void deleteTask(Integer id);
}
