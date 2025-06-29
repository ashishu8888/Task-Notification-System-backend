package com.ashish.tasknotificationsystem.Service.impl;

import com.ashish.tasknotificationsystem.Dto.UserLoginDto;
import com.ashish.tasknotificationsystem.Dto.UserRegisterDto;
import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Jwt.JwtService;
import com.ashish.tasknotificationsystem.Repository.AssigneeRepository;
import com.ashish.tasknotificationsystem.Service.AssigneeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AssigneeServiceImpl implements AssigneeService, UserDetailsService {

    private final AssigneeRepository assigneeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AssigneeServiceImpl(AssigneeRepository assigneeRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.assigneeRepository = assigneeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Assignee findByUsername(String username){
        return assigneeRepository.findAssigneeByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public String register(UserRegisterDto userRegisterDto) {

        log.info("Attempting register for user: {}", userRegisterDto.getUsername());

        Assignee assignee = new Assignee();

        assignee.setFirstName(userRegisterDto.getFirstName());
        assignee.setLastName(userRegisterDto.getLastName());
        assignee.setUsername(userRegisterDto.getUsername());
        assignee.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        assigneeRepository.save(assignee);

        log.info("registration successful for user: {}", userRegisterDto.getUsername());

        return "User registered successfully.";
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        log.info("Attempting login for user: {}", userLoginDto.getUsername());
        Assignee foundAssignee = findByUsername(userLoginDto.getUsername());

        if(!passwordEncoder.matches(userLoginDto.getPassword(), foundAssignee.getPassword())){
            throw new BadCredentialsException("Invalid credentials.");
        }

        log.info("Login successful for user: {}", userLoginDto.getUsername());

        return jwtService.generateToken(foundAssignee);
    }

    @Override
    public String editAssignee(UserRegisterDto userRegisterDto) {
        log.info("Attempting updating data for user: {}", userRegisterDto.toString());

        Assignee foundAssignee = findByUsername(userRegisterDto.getUsername());

        String password = userRegisterDto.getPassword();
        String firstName = userRegisterDto.getFirstName();
        String lastName = userRegisterDto.getLastName();


        if(password!=null && !passwordEncoder.matches(password,foundAssignee.getPassword())){
            foundAssignee.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        }

        if(firstName!=null){
            foundAssignee.setFirstName(firstName);
        }

        if(lastName!=null){
            foundAssignee.setLastName(lastName);
        }

        log.info("updated data for user: {}", foundAssignee.toString());

        assigneeRepository.save(foundAssignee);

        return "User updated successfully.";
    }

    public UserRegisterDto convertToUserRegisterDto(Assignee assignee){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername(assignee.getUsername());
        userRegisterDto.setPassword(assignee.getPassword());
        userRegisterDto.setFirstName(assignee.getFirstName());
        userRegisterDto.setLastName((assignee.getLastName()));
        return userRegisterDto;
    }

    @Override
    public String deleteUser(String username) {
        log.info("Attempting deleting the user: {}", username);
        assigneeRepository.delete(findByUsername(username));
        log.info("Deletion successful for user: {}", username);
        return "Deleted User Successfully.";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }
}