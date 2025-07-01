package com.vacation.homework.app.rabbitMq;

import com.vacation.homework.app.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class HomeworkMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendHomeworkCreated(Long userSeq, Long homeworkSeq) {
        HomeworkCreatedMessage msg = new HomeworkCreatedMessage(userSeq, homeworkSeq);
        rabbitTemplate.convertAndSend(
                RabbitConfig.HOMEWORK_EXCHANGE,
                RabbitConfig.HOMEWORK_ROUTING_KEY,
                msg
        );
    }
}