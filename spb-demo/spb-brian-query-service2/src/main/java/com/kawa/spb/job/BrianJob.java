package com.kawa.spb.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

public class BrianJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("<<<<< run {} >>>>>: {}", jobExecutionContext.getJobDetail().getKey().getName(), LocalDateTime.now());
    }
}