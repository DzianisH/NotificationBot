package io.dzianish.notificationbot.service.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class JobWrapper implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
//        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
//        Job job = (Job) jobDataMap.get("job.instance");
//        job.execute(context);
        log.info("Notification");

//        notificationSender.sendNotification(Notification.builder()
//                .chatId(-1001177912950L)
//                .message("FU")
//                .build()
//        );
    }
}
