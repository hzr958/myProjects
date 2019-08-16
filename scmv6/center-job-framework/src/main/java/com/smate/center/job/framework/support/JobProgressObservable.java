package com.smate.center.job.framework.support;


import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.util.Assert;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.framework.dto.TaskletDTO;

/**
 * 任务进度（被观察者），用以更新任务进度，及时向NodeManager报告。
 * 每一个JobRunnable的实现类都应该持有此类的实例对象。
 * @author houchuanjie
 * @date 2018/04/09 09:53
 */
public final class JobProgressObservable extends Observable {
    /**
     * 执行记录偏移量
     */
    private AtomicLong offset = new AtomicLong(0);

    /**
     * 任务信息
     */
    private final TaskletDTO taskletDTO;

    public JobProgressObservable(final TaskletDTO taskletDTO, final Observer observer) {
        this.taskletDTO = taskletDTO;
        super.addObserver(observer);
    }

    /**
     * 执行记录偏移量+1
     */
    public final void increaseOffset(){
        long offset = this.offset.incrementAndGet();
        taskletDTO.setOffset(offset);
        super.setChanged();
        super.notifyObservers(taskletDTO);
    }

    /**
     * 更新状态
     * @param status
     */
    public final void updateStatus(final JobStatusEnum status, final String errMsg){
        Assert.notNull(status);
        taskletDTO.setStatus(status);
        taskletDTO.setErrMsg(errMsg);
        super.setChanged();
        super.notifyObservers();
    }

    public final TaskletDTO getTaskletDTO() {
        return taskletDTO;
    }

    public final long getOffset(){
        return offset.get();
    }
}
