package com.ashish.tasknotificationsystem.Service.impl;

import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.Subtask;
import com.ashish.tasknotificationsystem.Entity.Task;
import com.ashish.tasknotificationsystem.Mapper.TaskMapper;
import com.ashish.tasknotificationsystem.Repository.TaskRepository;
import com.ashish.tasknotificationsystem.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AssigneeServiceImpl assigneeService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, AssigneeServiceImpl assigneeService){
        this.taskRepository = taskRepository;
        this.assigneeService = assigneeService;
    }

    private Assignee getLoggedInUser(){
        return assigneeService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        return TaskMapper.taskToDto(taskRepository.save(TaskMapper.dtoToTask(taskDto,getLoggedInUser())));
    }

    @Override
    public TaskDto addSubTask(SubtaskDto subtaskDto){
        Task relatedTask = getTaskById(subtaskDto.getParentTaskId());

        Subtask newSubtask = TaskMapper.dtoToSubtask(subtaskDto);
        newSubtask.setParentTask(relatedTask);

        relatedTask.getSubtasks().add(newSubtask);
        taskRepository.save(relatedTask);
        return TaskMapper.taskToDto(relatedTask);
    }

    @Override
    public Task getTaskById(Integer id) {
        return taskRepository.findTaskById(id);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        Assignee reqUser = getLoggedInUser();
        String username = reqUser.getUsername();

        List<Task> userTasks = taskRepository.findAllTasksByUsername(username);

        return userTasks.stream().map(TaskMapper::taskToDto).toList();
    }

    @Override
    public Task updateTask(Task updatedTask) {
        return taskRepository.save(updatedTask);
    }

    @Override
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}
