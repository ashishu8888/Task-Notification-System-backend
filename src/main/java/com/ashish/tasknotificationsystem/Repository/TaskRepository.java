package com.ashish.tasknotificationsystem.Repository;

import com.ashish.tasknotificationsystem.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    @Query("SELECT t from Task t where t.assignedTo.username = :username")
    List<Task> findAllTasksByUsername(@Param("username") String username);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.username = :username AND t.status != com.ashish.tasknotificationsystem.Enum.Status.COMPLETED")
    List<Task> findAllNotCompletedTasks(@Param("username") String username );

    Task findTaskById(Integer id);
}
