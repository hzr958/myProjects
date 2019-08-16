package com.smate.center.job.framework.cluster.support;

import java.util.List;

import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;

/**
 * 任务处理器接口
 * 
 * @author houchuanjie
 * @date 2018/04/11 14:46
 */
public interface JobProcessable {
    /**
     * 对给定原始任务信息进行加工处理
     * 
     * @param jobInfo
     * @param <T>
     * @return
     */
    public <T extends BaseJobDTO> List<TaskletDTO> process(T jobInfo);
}
