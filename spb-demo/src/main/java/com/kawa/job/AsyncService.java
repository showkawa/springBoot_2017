package com.kawa.job;


import com.kawa.mail.BrianMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
public class AsyncService {

    private Logger logger = LoggerFactory.getLogger(AsyncService.class);

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
        logger.debug("异步任务");
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void brianScheduling() {
        logger.debug("定时任务" + Runtime.getRuntime().availableProcessors());
    }
}
