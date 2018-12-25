package com.kawa.spbjob.job;

import com.kawa.spbjob.service.QueryBrainById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SendBrian2MQ {
    private Logger logger = LoggerFactory.getLogger(SendBrian2MQ.class);

    @Autowired
    QueryBrainById queryBrainById;


    @Scheduled(cron = "0/5 * * * * *")
    public void brianScheduling() {
        Object obj = null;
        //obj = queryBrainById.queryBrian(null);
        logger.info("<<<< query brian >>>> :" + obj.toString());
    }
}
