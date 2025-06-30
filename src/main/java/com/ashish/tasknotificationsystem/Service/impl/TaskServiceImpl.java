package com.ashish.tasknotificationsystem.Service.impl;

import com.ashish.tasknotificationsystem.Config.RabbitMqConfig;
import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.Subtask;
import com.ashish.tasknotificationsystem.Entity.Task;
import com.ashish.tasknotificationsystem.Entity.TaskNotification;
import com.ashish.tasknotificationsystem.Enum.Status;
import com.ashish.tasknotificationsystem.Exception.ResourceNotFoundException;
import com.ashish.tasknotificationsystem.Mapper.TaskMapper;
import com.ashish.tasknotificationsystem.PubSubUtils.RmqPublisherUtil;
import com.ashish.tasknotificationsystem.Repository.TaskRepository;
import com.ashish.tasknotificationsystem.Service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AssigneeServiceImpl assigneeService;
    private final RmqPublisherUtil rmqPublisherUtil;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, AssigneeServiceImpl assigneeService, RmqPublisherUtil rmqPublisherUtil){
        this.taskRepository = taskRepository;
        this.assigneeService = assigneeService;
        this.rmqPublisherUtil = rmqPublisherUtil;
    }

    public Assignee getLoggedInUser(){
        return assigneeService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        return TaskMapper.taskToDto(TaskMapper.dtoToTask(taskDto,getLoggedInUser()));
    }

    @Override
    public TaskDto addSubTask(SubtaskDto subtaskDto){

        if (subtaskDto.getParentTaskId() == null) {
            throw new IllegalArgumentException("Parent task ID cannot be null.");
        }

        Task relatedTask = taskRepository.findById(subtaskDto.getParentTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent task not found when trying to add subtask with parent ID: " + subtaskDto.getParentTaskId()));


        Subtask newSubtask = TaskMapper.dtoToSubtask(subtaskDto);
        newSubtask.setParentTask(relatedTask);

        relatedTask.getSubtasks().add(newSubtask);
        taskRepository.save(relatedTask);
        return TaskMapper.taskToDto(relatedTask);
    }

    @Override
    public TaskDto updateSubtask(SubtaskDto updatedSubtaskDto) {
        if (updatedSubtaskDto.getId() == null) {
            throw new IllegalArgumentException("Subtask ID cannot be null.");
        }
        if (updatedSubtaskDto.getParentTaskId() == null) {
            throw new IllegalArgumentException("Parent task ID cannot be null.");
        }

        Task task = taskRepository.findById(updatedSubtaskDto.getParentTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent task not found with ID: " + updatedSubtaskDto.getParentTaskId()));

        Subtask subtask = task.getSubtasks().stream()
                .filter(st -> st.getId().equals(updatedSubtaskDto.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with ID: " + updatedSubtaskDto.getId()));

        if (!Objects.equals(subtask.getTitle(), updatedSubtaskDto.getTitle())) {
            subtask.setTitle(updatedSubtaskDto.getTitle());
        }

        if (subtask.isCompleted() != updatedSubtaskDto.isCompleted()) {
            subtask.setCompleted(updatedSubtaskDto.isCompleted());
        }

        taskRepository.save(task);
        return TaskMapper.taskToDto(task);
    }

    @Override
    public List<TaskDto> getTodaysTask(){
        Assignee assignee = getLoggedInUser();
        String username = assignee.getUsername();

        LocalDate todayDate = LocalDate.now();

        List<Task> notCompletedTasks = taskRepository.findAllNotCompletedTasks(username);

        return notCompletedTasks.stream().filter(task -> task.getDueDate().isEqual(todayDate)).map(TaskMapper::taskToDto).toList();
    }

    @Override
    public TaskDto deleteSubtaskById(SubtaskDto updatedSubtaskDto){
        if (updatedSubtaskDto.getId() == null) {
            throw new IllegalArgumentException("Subtask ID cannot be null.");
        }
        if (updatedSubtaskDto.getParentTaskId() == null) {
            throw new IllegalArgumentException("Parent task ID cannot be null.");
        }

        Task task = taskRepository.findById(updatedSubtaskDto.getParentTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent task not found with ID: " + updatedSubtaskDto.getParentTaskId()));

        Subtask subtask = task.getSubtasks().stream()
                .filter(st -> st.getId().equals(updatedSubtaskDto.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with ID: " + updatedSubtaskDto.getId()));

        task.getSubtasks().remove(subtask);

        taskRepository.save(task);

        return TaskMapper.taskToDto(task);
    }

    @Override
    public TaskDto getTaskById(Integer id) {
        return TaskMapper.taskToDto(taskRepository.findTaskById(id));
    }

    @Override
    public List<TaskDto> getAllTasks() {
        Assignee reqUser = getLoggedInUser();
        String username = reqUser.getUsername();

        List<Task> userTasks = taskRepository.findAllTasksByUsername(username);

        return userTasks.stream().map(TaskMapper::taskToDto).toList();
    }

    @Override
    public TaskDto changeTaskStatus(Integer taskId, Status status){
        if(!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with taskId : "+taskId);
        }

        Task task = taskRepository.findTaskById(taskId);

        task.setStatus(status);

        taskRepository.save(task);

        return TaskMapper.taskToDto(task);

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
