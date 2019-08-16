package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 单位关注基金表 - KPI_INS_GRANT_SCHEME
 * 
 * @author lijianming
 *
 */
@Entity
@Table(name = "KPI_INS_GRANT_SCHEME")
public class SieInsFocusScheme {

    // 主键
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_INS_GRANT_SCHEME", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    private Long id;

    @Column(name = "AGENCY_ID")
    private Long agencyId; // 单位关注的基金机构Id

    @Column(name = "GRANT_ID")
    private Long grantId; // 单位关注的资助机会Id

    @Column(name = "INS_ID")
    private Long insId; // 单位ID

    public SieInsFocusScheme() {
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

    public Long getGrantId() {
        return grantId;
    }

    public void setGrantId(Long grantId) {
        this.grantId = grantId;
    }

    public Long getInsId() {
        return insId;
    }

    public void setInsId(Long insId) {
        this.insId = insId;
    }

}
