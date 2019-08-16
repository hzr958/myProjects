package com.smate.center.task.model.bdsp;

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
 * bdsp构建数据日志记录
 * 
 * dataId+typeId保证为联合主键约束
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_DATA_LOG_RECORD")
public class BdspDataLogRecord implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 主键，不能有业务逻辑
   */
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_DATA_LOG_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  /**
   * 记录的数据的主键
   */
  @Column(name = "DATA_ID")
  private Long dataId;
  /**
   * 数据的类型 参考BdspDataConstant
   */
  @Column(name = "TYPE_ID")
  private Integer typeId;
  /**
   * 创建的时间
   */
  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  /**
   * 错误记录
   */
  @Column(name = "ERROR_MSG")
  private String errorMsg;
  /**
   * 状态 0=待处理 1=正常 2=错误
   */
  @Column(name = "STATUS")
  private Integer status;


  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDataId() {
    return dataId;
  }

  public void setDataId(Long dataId) {
    this.dataId = dataId;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


}
