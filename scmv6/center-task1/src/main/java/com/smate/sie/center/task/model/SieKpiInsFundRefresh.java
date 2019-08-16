package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 单位关注的基金机构和基金机会冗余和缺失数据处理日志表
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "KPI_INS_FUND_REFRESH")
public class SieKpiInsFundRefresh implements Serializable {

    private static final long serialVersionUID = -5954180258687024540L;

    @Id
    @Column(name = "INS_ID")
    private Long insId;

    // 处理状态，0未处理(默认),1已处理,99失败
    @Column(name = "STATUS")
    private Integer status;

    // 优先级
    @Column(name = "PRIOR_CODE")
    private Integer priorCode;

    public SieKpiInsFundRefresh() {
        super();
    }

    public SieKpiInsFundRefresh(Integer status, Integer priorCode) {
        super();
        this.status = status;
        this.priorCode = priorCode;
    }

    public Long getInsId() {
        return insId;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getPriorCode() {
        return priorCode;
    }

    public void setInsId(Long insId) {
        this.insId = insId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPriorCode(Integer priorCode) {
        this.priorCode = priorCode;
    }
}
