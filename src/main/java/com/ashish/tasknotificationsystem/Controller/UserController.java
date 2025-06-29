package com.ashish.tasknotificationsystem.Controller;

import com.ashish.tasknotificationsystem.Dto.UserRegisterDto;
import com.ashish.tasknotificationsystem.Service.AssigneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final AssigneeService assigneeService;

    @Autowired
    public UserController(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;
    }

    @PostMapping("/user/update")
    public ResponseEntity<String> editAssignee(@RequestBody UserRegisterDto userRegisterDto)  {
      return ResponseEntity.ok(assigneeService.editAssignee(userRegisterDto));
    }

    @DeleteMapping("/user/delete/{username}")
    public ResponseEntity<String> deleteAssignee(@PathVariable String username){
        return ResponseEntity.ok(assigneeService.deleteUser(username));
    }
}
