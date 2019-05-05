package com.kawa.mq;
import com.config.Contents;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageMQService {

    @Autowired
    AmqpAdmin amqpAdmin;

    public void createExchange(String exchangeName,String mqType){
        if(mqType.equals(Contents.DIRECT_EXCHANGE)){
            amqpAdmin.declareExchange(new DirectExchange(exchangeName));
        }
        if(mqType.equals(Contents.FANOUT_EXCHANGE)){
            amqpAdmin.declareExchange(new FanoutExchange(exchangeName));
        }
        if(mqType.equals(Contents.TOPIC_EXCHANGE)){
            amqpAdmin.declareExchange(new TopicExchange(exchangeName));
        }
    }

    public void removeExchange(String exchangeName){
            amqpAdmin.deleteExchange(exchangeName);
    }

    public void createQueue(String queueName){
        amqpAdmin.declareQueue(new Queue(queueName,true));
    }

    public void removeQueue(String queueName){
        amqpAdmin.deleteQueue(queueName);
    }

    public void createBinding(String queueName, String exchangeName, String routingKey){
        amqpAdmin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE,exchangeName,routingKey,null));
    }

    public void removeBinding(String queueName, String exchangeName, String routingKey){
        amqpAdmin.removeBinding(new Binding(queueName, Binding.DestinationType.QUEUE,exchangeName,routingKey,null));
    }
}
