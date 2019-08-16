package com.smate.center.job.framework.dto;

import java.util.List;

import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.framework.cluster.support.JobFactory;
import com.smate.center.job.framework.cluster.JobMaster;

/**
 * 任务加工处理、封装的数据传输模型，主要用于{@link JobFactory}加工后包装，提供给{@link JobMaster}使用
 * 
 * @author houchuanjie
 * @date 2018年4月25日 下午2:09:22
 */
public class TaskletPackageDTO<T extends BaseJobDTO> {
    private JobTypeEnum jobType;
    private final T job;
    private final List<TaskletDTO> tasklets;

    /**
     * 封装一个任务对象
     * 
     * @param job
     *            原始任务对象，{@link BaseJobDTO}
     *            的实现类{@link OfflineJobDTO}、{@link OnlineJobDTO}对象
     * @param taskletList
     *            加工处理后的小任务列表
     */
    public TaskletPackageDTO(T job, List<TaskletDTO> taskletList) {
        this.job = job;
        this.tasklets = taskletList;
        if (job instanceof OfflineJobDTO) {
            this.jobType = JobTypeEnum.OFFLINE;
        } else if (job instanceof OnlineJobDTO) {
            this.jobType = JobTypeEnum.ONLINE;
        }
    }

    /**
     * 获取任务类型
     * 
     * @return jobType
     */
    public JobTypeEnum getJobType() {
        return this.jobType;
    }

    /**
     * 获取任务信息
     * 
     * @return job
     */
    public T getJob() {
        return this.job;
    }

    /**
     * 获取拆分后的小任务集合
     * 
     * @return tasklets
     */
    public List<TaskletDTO> getTasklets() {
        return this.tasklets;
    }

}
