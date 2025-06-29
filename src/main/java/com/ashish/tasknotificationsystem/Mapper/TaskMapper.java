package com.ashish.tasknotificationsystem.Mapper;

import com.ashish.tasknotificationsystem.Dto.SubtaskDto;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.Subtask;
import com.ashish.tasknotificationsystem.Entity.Task;
import java.util.List;

public class TaskMapper {

    private TaskMapper(){
        // Private constructor
    }

    public static TaskDto taskToDto(Task task) {
        if (task == null) return null;

        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setBody(task.getBody());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setCompletedAt(task.getCompletedAt());
        dto.setSubtasks(task.getSubtasks().stream().map(TaskMapper::subtaskToDto).toList());
        dto.setTags(task.getTags());
        dto.setAttachments(task.getAttachments());

        return dto;
    }

    public static Task dtoToTask(TaskDto dto, Assignee assignee) {
        if (dto == null) return null;

        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setBody(dto.getBody());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setAssignedTo(assignee);
        task.setDueDate(dto.getDueDate());
        task.setUpdatedAt(dto.getUpdatedAt());
        task.setCompletedAt(dto.getCompletedAt());

        List<Subtask> subtasks = dto.getSubtasks().stream().map(TaskMapper::dtoToSubtask).toList();
        for (Subtask subtask : subtasks) {
            subtask.setParentTask(task);
        }
        task.setSubtasks(subtasks);

        task.setTags(dto.getTags());
        task.setAttachments(dto.getAttachments());

        return task;
    }

    public static SubtaskDto subtaskToDto(Subtask subtask) {
        if (subtask == null) return null;
        return new SubtaskDto(
                subtask.getId(),
                subtask.getTitle(),
                subtask.isCompleted(),
                subtask.getParentTask().getId()
        );
    }

    public static Subtask dtoToSubtask(SubtaskDto dto) {
        if (dto == null) return null;
        Subtask subtask = new Subtask();
        subtask.setId(dto.getId());
        subtask.setTitle(dto.getTitle());
        subtask.setCompleted(dto.isCompleted());
        return subtask;
    }
}

