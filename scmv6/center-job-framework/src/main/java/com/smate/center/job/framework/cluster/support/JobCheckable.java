package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.framework.dto.BaseJobDTO;

/**
 * 任务信息检查接口
 * 
 * @author houchuanjie
 * @date 2018年4月26日 下午2:30:41
 */
public interface JobCheckable {
    /**
     * 任务检查，检查任务的类型、配置信息是否满足任务要求；满足要求则返回true，如果不满足要求，则将该任务标记为执行失败，并赋值错误信息，保存到数据库，然后返回false。
     * 
     * @param job
     *            OfflineJobDTO或者OnlineJobDTO实例
     * @return 如果任务检查发现配置出错或者导致任务无法执行的问题，返回false；如果检查合格，返回true。
     */
    boolean check(BaseJobDTO job);
}
