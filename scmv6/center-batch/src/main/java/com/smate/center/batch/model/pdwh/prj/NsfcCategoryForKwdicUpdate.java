package com.smate.center.batch.model.pdwh.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金委学科主任维护关键词表
 * 
 **/
@Entity
@Table(name = "NSFC_CATEGORY_FOR_KWDIC_UPDATE")
public class NsfcCategoryForKwdicUpdate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7052422806669840871L;

    private String nsfcApplicationCode;
    private Integer length;
    private Integer status;
    private Integer applicationCounts;

    public NsfcCategoryForKwdicUpdate() {
        super();
    }

    @Id
    @Column(name = "NSFC_CATEGORY")
    public String getNsfcApplicationCode() {
        return nsfcApplicationCode;
    }

    public void setNsfcApplicationCode(String nsfcApplicationCode) {
        this.nsfcApplicationCode = nsfcApplicationCode;
    }

    @Column(name = "LENGTH")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "APPLICATION_COUNTS")
    public Integer getApplicationCounts() {
        return applicationCounts;
    }

    public void setApplicationCounts(Integer applicationCounts) {
        this.applicationCounts = applicationCounts;
    }

}
