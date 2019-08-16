package com.smate.center.job.framework.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 本想用于子任务创建时的依赖注入，然而却不生效。可能使用方法不妥当。已通过其他方式注入。
 * @author houchuanjie
 * @date 2018/05/10 16:19
 */
public class SpringQuartzJobFactory extends SpringBeanJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
