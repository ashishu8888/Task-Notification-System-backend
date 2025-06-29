package com.ashish.tasknotificationsystem.Dto;

import com.ashish.tasknotificationsystem.Entity.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskDto {

    private Integer id;

    private String title;

    private boolean completed;

    private Integer parentTaskId;
}
