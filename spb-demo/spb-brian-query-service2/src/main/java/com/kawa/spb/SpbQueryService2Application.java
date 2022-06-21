package com.kawa.spb;

import com.kawa.spb.job.BrianJob;
import com.kawa.spb.sercice.impl.QuartzManager;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpbQueryService2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpbQueryService2Application.class, args);

        // test job
        try {
            QuartzManager.getInstance().createJob(BrianJob.class, "brianJob", "demoGroup", "0/3 * * * * ?");
        } catch (SchedulerException e) {
            if(e instanceof ObjectAlreadyExistsException){
                System.out.println("<<<<<<<<<<< this job exist >>>>>>>>>>");
            }
        }

    }

}

