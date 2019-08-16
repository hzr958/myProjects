package com.smate.web.dyn.model.psn.rcmd;

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
 * 影响力引导记录表
 * 
 * @author yhx
 *
 */
@Entity
@Table(name = "V_IMPACT_GUIDE_RECORD")
public class ImpactGuideRecord implements Serializable {


  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "V_SEQ_IMPACT_GUIDE_RECORD", sequenceName = "V_SEQ_IMPACT_GUIDE_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_IMPACT_GUIDE_RECORD")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PSN_ID")
  private Long psnId; // 人员id

  @Column(name = "STATUS")
  private Integer status; // 状态：0启用，1暂停

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

}
