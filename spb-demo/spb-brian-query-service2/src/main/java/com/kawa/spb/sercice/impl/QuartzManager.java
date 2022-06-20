package com.kawa.spb.sercice.impl;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QuartzManager {

    private static QuartzManager jobUtil;

    @Autowired
    private Scheduler scheduler;

    public QuartzManager() {
        jobUtil = this;
    }

    public static QuartzManager getInstance() {
        return QuartzManager.jobUtil;
    }

    /**
     * createJob
     *
     * @param clazz
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    public void createJob(Class clazz, String jobName, String jobGroupName, String cronExpression) {

    }

    /**
     * create Job with params
     *
     * @param clazz
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    public void createJob(Class clazz, String jobName, String jobGroupName, String cronExpression, Map<String, Object> params) {

    }

    /**
     * update job
     * only update the cron
     *
     * @param clazz
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    public void updateJob(Class clazz, String jobName, String jobGroupName, String cronExpression) {

    }

    /**
     * update Job for cron and params
     *
     * @param clazz
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    public void updateJob(Class clazz, String jobName, String jobGroupName, String cronExpression, Map<String, Object> params) {

    }

    /**
     * update job
     * only update the cron
     *
     * @param jobName
     * @param jobGroupName
     */
    public void deleteJob(String jobName, String jobGroupName) {

    }

    /**
     * get all job
     *
     * @return
     */
    public List<Map<String, Object>> getAllJob() {
        return null;
    }

    /**
     * pause job
     *
     * @param jobName
     * @param jobGroupName
     */
    public void pauseJob(String jobName, String jobGroupName) {

    }

    /**
     * resume job
     * only update the cron
     *
     * @param jobName
     * @param jobGroupName
     */
    public void resumeJob(String jobName, String jobGroupName) {

    }

    /**
     * start all job
     */
    public void startAllJob() {

    }

    /**
     * shutdown all job
     */
    public void shutdownAllJob() {

    }


}
