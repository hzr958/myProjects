package com.smate.sie.core.base.utils.model.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * bpo资助机构、资助机会变化表
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "DIFF_KPI_INS_GRANT")
public class DiffKpiInsGrant implements Serializable {

    private static final long serialVersionUID = 2044069338444307693L;

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DIFF_KPI_INS_GRANT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long id; // 主键

    @Column(name = "AGENT_COUNT")
    private Long agencyCount; // 资助机构数

    @Column(name = "GRANT_COUNT")
    private Long grantCount; // 资助机会数

    @Column(name = "AGENT_INSERT_COUNT")
    private Long agencyInsertCount; // 机构新增数

    @Column(name = "AGENT_DELETE_COUNT")
    private Long agencyDeleteCount; // 机构减少数

    @Column(name = "GRANT_INSERT_COUNT")
    private Long grantInsertCount; // 机会新增数

    @Column(name = "GRANT_DELETE_COUNT")
    private Long grantDeleteCount; // 机会减少数

    @Column(name = "CREATE_DATE")
    private Date createDate; // 记录创建时间

    public DiffKpiInsGrant() {
        super();
    }

    public Long getId() {
        return id;
    }

    public Long getAgencyCount() {
        return agencyCount;
    }

    public Long getGrantCount() {
        return grantCount;
    }

    public Long getAgencyInsertCount() {
        return agencyInsertCount;
    }

    public Long getAgencyDeleteCount() {
        return agencyDeleteCount;
    }

    public Long getGrantInsertCount() {
        return grantInsertCount;
    }

    public Long getGrantDeleteCount() {
        return grantDeleteCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAgencyCount(Long agencyCount) {
        this.agencyCount = agencyCount;
    }

    public void setGrantCount(Long grantCount) {
        this.grantCount = grantCount;
    }

    public void setAgencyInsertCount(Long agencyInsertCount) {
        this.agencyInsertCount = agencyInsertCount;
    }

    public void setAgencyDeleteCount(Long agencyDeleteCount) {
        this.agencyDeleteCount = agencyDeleteCount;
    }

    public void setGrantInsertCount(Long grantInsertCount) {
        this.grantInsertCount = grantInsertCount;
    }

    public void setGrantDeleteCount(Long grantDeleteCount) {
        this.grantDeleteCount = grantDeleteCount;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
