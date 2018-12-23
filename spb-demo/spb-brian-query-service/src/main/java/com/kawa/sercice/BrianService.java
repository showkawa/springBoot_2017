package com.kawa.sercice;


import com.kawa.dao.UserDao;
import com.kawa.mq.SendMessageService;
import com.kawa.pojo.Brian;
import com.kawa.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class BrianService {

    Logger logger = LoggerFactory.getLogger(BrianService.class);

    @Autowired
    private SendMessageService sendMessageService;

    @Resource
    private UserDao userDao;

    @Resource(name = "brianThreadPool")
    private ThreadPoolTaskExecutor executor;

    List<Future> futureslist = new ArrayList<>();



    @RabbitListener(queues = "brian.test")
    public void receiveMessage(User user){
        logger.info("接收到MQ的消息体: " + user);
    }

    /**
     * 多线程向MQ推送消息(从数据库取数据t推送到到MQ服务器)
     */
    public void sendMessageByThredPool() throws ExecutionException, InterruptedException {
        List<Future> futures = new ArrayList<>();
        List<User> userList = userDao.queryUserList(null);
        Brian brian1 = new Brian();
        for(User user: userList) {
            brian1.setUser(user);
            Future<String> future = executor.submit(() -> {
                //推送消息到RabbitMQ服务
                sendMessageService.sendMessage("brian","mymq",brian1);
                return Thread.currentThread().getName();
            });
            futures.add(future);
        }

        for( Future<?> future: futures){
            while (true){
                //CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。
                // 即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                if(future.isDone() && !future.isCancelled()){
                    //获取future成功完成状态，如果想要限制每个任务的超时时间，
                    // 取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
                    String i = (String) future.get();
                    logger.info("线程 [ " + i + " ] 推送消息到MQ成功! " + new Date());
                    //当前future获取结果完毕，跳出while
                    break;
                } else {
                    //每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU
                    Thread.sleep(1);
                }
            }
        }
    }
    /**
     * 多线程推送消息到MQ服务
     */
    public void sendMessageByThredPool(User user) throws ExecutionException, InterruptedException {
            Future<String> future = executor.submit(() -> {
                sendMessageService.sendMessage("brian","mymq",user);
                logger.info("线程 [ " + Thread.currentThread().getName() + " ] 推送消息到MQ成功! " + new Date());
                return Thread.currentThread().getName();
            });
           // futureslist.add(future);

       /* if(future.isDone() && !future.isCancelled()){
            String i = (String) future.get();
            logger.info("线程 [ " + i + " ] 推送消息到MQ成功! " + new Date());
        } else {
            Thread.sleep(1);
        }*/
    }
}
