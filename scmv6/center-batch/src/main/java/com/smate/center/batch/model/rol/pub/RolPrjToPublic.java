package com.smate.center.batch.model.rol.pub;

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
 * 项目公开表.
 * 
 * @author mjg
 * @since 2014-08-21
 */
@Entity
@Table(name = "PRJ_TO_PUBLIC")
public class RolPrjToPublic implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;// 主键.
  private Long prjId;// 项目ID.
  private Integer status;// 开放状态1-开放；0-不开放.
  private Date createTime;// 创建时间.
  private Date updateTime;// 更新时间.
  private String remark;// 备注.

  public RolPrjToPublic() {
    super();
  }

  public RolPrjToPublic(Long prjId, Integer status) {
    super();
    this.prjId = prjId;
    this.status = status;
  }

  public RolPrjToPublic(Long id, Long prjId, Integer status, Date createTime, Date updateTime, String remark) {
    super();
    this.id = id;
    this.prjId = prjId;
    this.status = status;
    this.createTime = createTime;
    this.updateTime = updateTime;
    this.remark = remark;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_TO_PUBLIC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
