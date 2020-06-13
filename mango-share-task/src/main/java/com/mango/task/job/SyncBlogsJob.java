package com.mango.task.job;


import com.mango.core.service.BlogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class SyncBlogsJob implements SchedulingConfigurer {
    private Logger logger = LoggerFactory.getLogger(SyncBlogsJob.class);


    @Autowired
    private BlogsService blogsService;
    private static String cron;


    //修改之后的cron
    public static void setCron(String cron) {
        SyncBlogsJob.cron = cron;
    }

    public SyncBlogsJob() {
        // 每10分钟进行PS数据的同步
        cron = "0 0/1 * * * ? ";
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("【同步csdn博客数据 start】");
                    blogsService.getBlogsByCategory("https://so.csdn.net/so/search/s.do?t=blog&o=&q=");
                    logger.info("【同步csdn博客数据   end】");


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        });
    }


    /**
     * TODO 待优化
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    private static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "";
    }

}