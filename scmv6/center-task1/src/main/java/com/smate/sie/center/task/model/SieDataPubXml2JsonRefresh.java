package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据服务，成果专利Xml数据转换为Json数据刷新表
 * 
 * @author lijianming
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "DATA_PUBXML_TO_JSON_REFRESH")
public class SieDataPubXml2JsonRefresh implements Serializable {

    @Id
    @Column(name = "PUB_ID")
    private Long pubId;

    @Column(name = "PUB_TYPE")
    private Integer pubType;

    @Column(name = "STATUS")
    private Integer status;

    public Long getPubId() {
        return pubId;
    }

    public Integer getPubType() {
        return pubType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setPubId(Long pubId) {
        this.pubId = pubId;
    }

    public void setPubType(Integer pubType) {
        this.pubType = pubType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
