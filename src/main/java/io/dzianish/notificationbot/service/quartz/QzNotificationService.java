package io.dzianish.notificationbot.service.quartz;

import io.dzianish.notificationbot.dto.Notification;
import io.dzianish.notificationbot.dto.NotificationConfig;
import io.dzianish.notificationbot.sender.NotificationSender;
import io.dzianish.notificationbot.service.NotificationService;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QzNotificationService implements NotificationService {
    private final NotificationSender notificationSender;

    @Autowired
    public QzNotificationService(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    public void createNotification(NotificationConfig config) {
        JobDetail jobDetail = JobBuilder.newJob(JobWrapper.class)
                .withIdentity("myJob", Scheduler.DEFAULT_GROUP)
                .build();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("job.instance", createJob(config));

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .startAt(config.getDate())
                .withIdentity("myJob", Scheduler.DEFAULT_GROUP)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(30)
                        .withRepeatCount(3))
                .forJob("myJob", Scheduler.DEFAULT_GROUP)
                .usingJobData(jobDataMap)
                .build();

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private Job createJob(NotificationConfig config) {
        return context -> notificationSender.sendNotification(Notification.builder()
                .chatId(config.getChatId())
                .message(config.getMessage())
                .build());
    }


}
