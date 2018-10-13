package com.kawa.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessageService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange,String routingKey,Object obj){
        //Message需要自己构造一个；定义消息体和消息头
        //rabbitTemplate.send(exchange,routingKey,message);

        //object默认当成消息体，只需要传入发送对象，自动序列化发送给rabbitmq
        rabbitTemplate.convertAndSend(exchange,routingKey,obj);
    }

}
