package com.ashish.tasknotificationsystem.Service;

import com.ashish.tasknotificationsystem.Dto.UserLoginDto;
import com.ashish.tasknotificationsystem.Dto.UserRegisterDto;

public interface AssigneeService {
    String register(UserRegisterDto userRegisterDto);
    String login(UserLoginDto userLoginDto);
    String editAssignee(UserRegisterDto userRegisterDto);
    String deleteUser(String username);
}
