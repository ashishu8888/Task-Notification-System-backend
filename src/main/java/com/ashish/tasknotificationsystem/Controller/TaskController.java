package com.ashish.tasknotificationsystem.Controller;

import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Task;
import com.ashish.tasknotificationsystem.Enum.Status;
import com.ashish.tasknotificationsystem.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("task/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskBody){
        return ResponseEntity.ok(taskService.createTask(taskBody));
    }

    @PatchMapping("task/{taskId}/change-status")
    public ResponseEntity<TaskDto> changeTaskStatus(@PathVariable Integer taskId, @RequestParam Status status){
        return ResponseEntity.ok(taskService.changeTaskStatus(taskId,status));
    }

    @PostMapping("subtask/create")
    public ResponseEntity<TaskDto> addSubtask(@RequestBody SubtaskDto subtaskDto){
        return ResponseEntity.ok(taskService.addSubTask(subtaskDto));
    }

    @PutMapping("subtask/update")
    public ResponseEntity<TaskDto> updateSubtask(@RequestBody SubtaskDto subtaskDto){
        return ResponseEntity.ok(taskService.updateSubtask(subtaskDto));
    }

    @DeleteMapping("subtask/delete")
    public ResponseEntity<TaskDto> deleteSubtask(@RequestBody SubtaskDto subtaskDto){
        return ResponseEntity.ok(taskService.deleteSubtaskById(subtaskDto));
    }

    @PostMapping("task/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task taskBody){
        return ResponseEntity.ok(taskService.updateTask(taskBody));
    }

    @GetMapping("task/get/{taskId}")
    public ResponseEntity<TaskDto> fetchTaskById(@PathVariable Integer taskId){
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping("task/get/all")
    public ResponseEntity<List<TaskDto>> fetchAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("task/delete/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("task/todays-task")
    public ResponseEntity<List<TaskDto>> getTodaysTasks(){
        return ResponseEntity.ok(taskService.getTodaysTask());
    }
}
