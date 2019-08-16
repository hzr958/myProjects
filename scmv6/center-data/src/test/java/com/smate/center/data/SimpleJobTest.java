package com.smate.center.data;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opensymphony.xwork2.interceptor.annotations.Before;

/**
 *
 * @author houchuanjie
 * @date 2017年12月19日 下午2:00:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-quartz-task.xml" })
public class SimpleJobTest {
    private static final String JOB_NAME = "HelloJob_";
    private static final String JOB_GROUP = "Group";
    @Resource(name = "masterScheduler")
    private Scheduler scheduler;
    @SuppressWarnings("rawtypes")
    private ScheduleBuilder scheduleBuilder;

    @SuppressWarnings("static-access")
    @Before
    public void before() {
        scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(1);
    }

    /* @SuppressWarnings("unchecked")
    @Test
    public void test() throws SchedulerException {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
    
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetailImpl jobDetailImpl = new JobDetailImpl();
        jobDetailImpl.setJobClass(HelloJob.class);
        jobDetailImpl.setGroup(JOB_GROUP);
        for (int i = 0; i < 20; i++) {
            jobDetailImpl.setName(JOB_NAME + (i + 1));
            Trigger trigger = triggerBuilder.withIdentity(JOB_NAME + (i + 1), JOB_GROUP).withSchedule(scheduleBuilder)
                    .build();
            scheduler.scheduleJob(jobDetailImpl, trigger);
        }
        while (schedulerFactoryBean.isRunning())
            ;
    }*/

    @Test
    public void test2() throws SchedulerException {
        while (!scheduler.isInStandbyMode())
            ;
    }

}
