package com.smate.sie.center.task.model;

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
 * 单位关注资助机构表 - KPI_INS_GRANT_AGENCY
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "KPI_INS_GRANT_AGENCY")
public class SieInsFocusAgency implements Serializable {

    private static final long serialVersionUID = -2459258917408827150L;

    // 主键
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_INS_GRANT_AGENCY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long id;

    @Column(name = "AGENCY_ID")
    private Long agencyId; // 单位关注的机构Id

    @Column(name = "INS_ID")
    private Long insId; // 单位Id

    @Column(name = "CREATE_TIME")
    private Date createDate; // 关注的时间

    @Column(name = "REGION_LEVEL")
    private Long regionLevel; // 机构等级Id号

    public SieInsFocusAgency() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public Long getInsId() {
        return insId;
    }

    public void setInsId(Long insId) {
        this.insId = insId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(Long regionLevel) {
        this.regionLevel = regionLevel;
    }
}
