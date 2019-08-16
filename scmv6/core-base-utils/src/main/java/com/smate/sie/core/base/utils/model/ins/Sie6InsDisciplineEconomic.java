package com.smate.sie.core.base.utils.model.ins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 机构国民经济行业分类表.
 * 
 * @author xr
 *
 */
@Entity
@Table(name = "INS_DISCIPLINE_ECONOMIC")
public class Sie6InsDisciplineEconomic implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6403718903810762814L;
    /*
     * 主键
     */
    private Long id;
    /*
     * 单位主键
     */
    private Long insId;
    /*
     * 行业代码
     */
    private String ecoCode;
    /*
     * 行业中文名称(冗余)
     */
    private String ecoZhName;
    /*
     * 行业英文名称(冗余)
     */
    private String ecoEnName;

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_DISCIPLINE_ECONOMIC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "INS_ID")
    public Long getInsId() {
        return insId;
    }

    public void setInsId(Long insId) {
        this.insId = insId;
    }

    @Column(name = "DIS_CODE")
    public String getEcoCode() {
        return ecoCode;
    }

    public void setEcoCode(String ecoCode) {
        this.ecoCode = ecoCode;
    }

    @Column(name = "ZH_NAME")
    public String getEcoZhName() {
        return ecoZhName;
    }

    public void setEcoZhName(String ecoZhName) {
        this.ecoZhName = ecoZhName;
    }

    @Column(name = "EN_NAME")
    public String getEcoEnName() {
        return ecoEnName;
    }

    public void setEcoEnName(String ecoEnName) {
        this.ecoEnName = ecoEnName;
    }

}
