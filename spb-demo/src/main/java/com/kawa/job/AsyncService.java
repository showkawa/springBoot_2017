package com.kawa.job;


import com.kawa.mail.BrianMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Autowired
    BrianMail brianMail;

    @Async
    public void brianAsync(){
        try {
            Thread.sleep(3000);
            brianMail.sendEmail();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步任务");
    }

    @Scheduled(cron = "0 * * * * *")
    public void brianScheduling() {
        System.out.println("定时任务");
    }
}
