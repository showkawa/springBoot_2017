package com.kawa.sercice;


import com.kawa.pojo.Brian;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BrianService {

    @RabbitListener(queues = "brian.test")
    public void receiveMessage(Brian brian){
        System.out.println("接收到的消息体:" + brian);
    }
}
