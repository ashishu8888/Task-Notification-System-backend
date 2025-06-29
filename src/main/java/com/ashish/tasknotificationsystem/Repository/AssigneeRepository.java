package com.ashish.tasknotificationsystem.Repository;

import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssigneeRepository extends JpaRepository<Assignee, Integer> {
    Assignee findAssigneeById(Integer id);
    Optional<Assignee> findAssigneeByUsername(String username);
}
