package com.ashish.tasknotificationsystem.PubSubUtils;

import com.ashish.tasknotificationsystem.Constants.RmqConstant;
import com.ashish.tasknotificationsystem.Entity.TaskNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RmqConsumerUtil {

    @RabbitListener(queues = RmqConstant.QUEUE_NAME)
    public void consumeTodayTasks(TaskNotification taskNotification) {
        log.info("🔔 Received Task Notification for: {}", taskNotification.getEmail());
         log.info("📌 Task: {}", taskNotification.getTask().getTitle());
    }
}
