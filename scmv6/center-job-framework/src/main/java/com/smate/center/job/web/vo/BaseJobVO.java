package com.smate.center.job.web.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobStatusEnum.JobStatusEnumDeserializer;
import java.io.Serializable;
import java.util.Date;

/**
 * 离线任务和在线任务的页面数据模型公共父类
 */
public abstract class BaseJobVO implements Serializable {
    /**
     * 任务id
     */
    protected String id;

    /**
     * 任务名称
     */
    protected String name;
    /**
     * 任务优先级
     */
    protected Integer priority;
    /**
     * 任务状态
     */
    protected JobStatusEnum status;
    /**
     * 任务参数
     */
    protected String paramsMap;
    /**
     * 任务错误信息
     */
    protected String errMsg;
    
    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 修改时间
     */
    protected Date modifiedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return status
     */
    public JobStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status
     *            要设置的 status
     */
    @JsonDeserialize(using = JobStatusEnumDeserializer.class)
    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    /**
     * @return paramsMap
     */
    public String getParamsMap() {
        return paramsMap;
    }

    /**
     * @param paramsMap
     *            要设置的 paramsMap
     */
    public void setParamsMap(String paramsMap) {
        this.paramsMap = paramsMap;
    }

    /**
     * @return errMsg
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * @param errMsg
     *            要设置的 errMsg
     */
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

}
