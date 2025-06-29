package com.ashish.tasknotificationsystem.Controller;

import com.ashish.tasknotificationsystem.Dto.UserLoginDto;
import com.ashish.tasknotificationsystem.Dto.UserRegisterDto;
import com.ashish.tasknotificationsystem.Service.AssigneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final AssigneeService assigneeService;

   @Autowired
    public AuthController(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> userRegistration(@RequestBody UserRegisterDto userRegisterDto){
        return ResponseEntity.ok(assigneeService.register(userRegisterDto));
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> userLogin(@RequestBody UserLoginDto userLoginDto){
       return ResponseEntity.ok(assigneeService.login(userLoginDto));
    }
}
