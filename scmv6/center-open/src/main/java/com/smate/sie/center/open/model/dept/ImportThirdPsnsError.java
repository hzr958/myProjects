package com.smate.sie.center.open.model.dept;

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
 * 导入第三方人员错误信息.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "IMPORT_THIRD_PSNS_ERROR")
public class ImportThirdPsnsError implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -922368348901920686L;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IMPORT_THIRD_PSNS_ERROR", allocationSize = 1)
  private Long id;
  @Column(name = "INS_ID")
  private Long insId;// 单位ID
  @Column(name = "PARAMS")
  private String params;// 邮箱
  @Column(name = "ERROR_MSG")
  private String errorMsg;// 邮箱
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建日期

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
