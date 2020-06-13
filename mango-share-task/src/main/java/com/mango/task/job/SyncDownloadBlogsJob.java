//package com.qudian.task.job;
//
//import com.qudian.core.service.BlogsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//@EnableScheduling
//public class SyncDownloadBlogsJob implements SchedulingConfigurer {
//    private Logger logger = LoggerFactory.getLogger(SyncDownloadBlogsJob.class);
//    @Autowired
//    private BlogsService blogsService;
//    private static String cron;
//
//
//    //修改之后的cron
//    public static void setCron(String cron) {
//        SyncDownloadBlogsJob.cron = cron;
//    }
//
//    public SyncDownloadBlogsJob() {
//        // 每10分钟进行PS数据的同步
//        cron = "0/11 * * * * ?";
//    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.addTriggerTask(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    logger.info("【按分类同步博客 start】");
//                    blogsService.getBlogsByCategory();
//                    logger.info("【按分类同步博客   end】");
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Trigger() {
//            @Override
//            public Date nextExecutionTime(TriggerContext triggerContext) {
//                CronTrigger trigger = new CronTrigger(cron);
//                return trigger.nextExecutionTime(triggerContext);
//            }
//        });
//    }
//}
