package com.ashish.tasknotificationsystem.PubSubUtils;

import com.ashish.tasknotificationsystem.Constants.RmqConstant;
import com.ashish.tasknotificationsystem.Dto.TaskDto;
import com.ashish.tasknotificationsystem.Entity.Assignee;
import com.ashish.tasknotificationsystem.Entity.TaskNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class RmqPublisherUtil {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RmqPublisherUtil(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void publishTasksToRmq(Assignee assignee, TaskDto taskDos){
        try{
            rabbitTemplate.convertAndSend(
                    RmqConstant.EXCHANGE_NAME,
                    RmqConstant.ROUTING_KEY,
                    new TaskNotification(assignee.getFirstName(),assignee.getLastName(),assignee.getUsername(), LocalDate.now(),taskDos));
        } catch(Exception ex){
            log.error("Failed to publish message to rmq : {}",ex.getMessage());
        }
    }
}
