package com.example.notificationservice;

import com.example.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    // the same topic name from 'OrderService' class
    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // send out an email notification
        // TODO: add the necessary dep and expend the functionality
        log.info("Received Notification for Order - {} ", orderPlacedEvent.getOrderNumber());
    }
}