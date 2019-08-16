package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.smate.core.base.utils.string.ServiceUtil;

@Entity
@Table(name = "KPI_VALIDATE_MAIN")
public class KpiValidateMain implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1786423458930561047L;

  /* 生成凭证号，表主键 */
  @Id
  @GeneratedValue(generator = "uuId")
  @GenericGenerator(name = "uuId", strategy = "uuid")
  @Column(name = "UUID", unique = true, nullable = false, length = 32)
  private String uuId;

  /* 验证文档或业务标题 */
  @Column(name = "TITLE")
  private String title;

  /* 提交时间 */
  @Column(name = "SUBMIT_DATE")
  private Date smDate;

  /* 结束时间 */
  @Column(name = "END_DATE")
  private Date endDate;

  /* 数据来源，资助机构名或科研之友机构版 */
  @Column(name = "DATA_FROM")
  private String dataFrom;

  /* 申请年份 */
  @Column(name = "PRP_YEAR")
  private String prpYear;

  /* 【拓展字段】检测优先级 */
  @Column(name = "PRIORITY")
  private Integer priority;

  /* 状态，0待处理1已完成 */
  @Column(name = "STATUS")
  private Integer status;

  /* 结束时间 */
  @Column(name = "RECEIPT_TIME")
  private Date receiptTime;

  @Column(name = "RECEIPT_COUNT")
  private Integer receiptCount;// 请求验证完毕后的数据，只能请求5次

  /* 依托单位 */
  @Column(name = "ORG_NAME")
  private String orgName;
  /* 版本号 */
  @Column(name = "VERSION_NO")
  private String versionNo;
  /* 版本号 */
  @Column(name = "KEY_CODE")
  private String keyCode;
  /* 版本号 */
  @Column(name = "KEY_TYPE")
  private String keyType;
  @Transient
  private String des3UuId;
  @Transient
  private String resultStatus; // 1全部验证通过，2存在验证不通过或者疑问，3待验证，4调接口异常
  @Transient
  private String smDateStr; // 提交时间，列表显示用

  public KpiValidateMain() {
    super();
  }

  public KpiValidateMain(String title, Integer status, String uuId) {
    super();
    this.title = title;
    this.status = status;
    this.uuId = uuId;
  }

  public KpiValidateMain(String title, Date smDate, String dataFrom, Integer priority, Integer status, String prpYear,
      String orgName, String keyCode, String keyType, String versionNo) {
    super();
    this.title = title;
    this.smDate = smDate;
    this.dataFrom = dataFrom;
    this.priority = priority;
    this.status = status;
    this.prpYear = prpYear;
    this.orgName = orgName;
    this.keyCode = keyCode;
    this.keyType = keyType;
    this.versionNo = versionNo;
  }


  public KpiValidateMain(String uuId, String title, Date smDate, Integer status) {
    super();
    this.uuId = uuId;
    this.title = title;
    this.smDate = smDate;
    this.status = status;
  }


  public KpiValidateMain(String title, Date smDate, String dataFrom, String prpYear, Integer priority, Integer status,
      Date receiptTime) {
    super();
    this.title = title;
    this.smDate = smDate;
    this.dataFrom = dataFrom;
    this.prpYear = prpYear;
    this.priority = priority;
    this.status = status;
    this.receiptTime = receiptTime;
  }



  public KpiValidateMain(String title, Date smDate, String dataFrom, Integer priority, Integer status, String prpYear) {
    super();
    this.title = title;
    this.smDate = smDate;
    this.dataFrom = dataFrom;
    this.priority = priority;
    this.status = status;
    this.prpYear = prpYear;
  }

  public KpiValidateMain(String uuId, String title, Date smDate, Date endDate, String dataFrom, Integer priority,
      Integer status) {
    super();
    this.uuId = uuId;
    this.title = title;
    this.smDate = smDate;
    this.endDate = endDate;
    this.dataFrom = dataFrom;
    this.priority = priority;
    this.status = status;
  }

  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getSmDate() {
    return smDate;
  }

  public void setSmDate(Date smDate) {
    this.smDate = smDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getDataFrom() {
    return dataFrom;
  }

  public void setDataFrom(String dataFrom) {
    this.dataFrom = dataFrom;
  }


  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getPrpYear() {
    return prpYear;
  }

  public void setPrpYear(String prpYear) {
    this.prpYear = prpYear;
  }


  public Date getReceiptTime() {
    return receiptTime;
  }

  public void setReceiptTime(Date receiptTime) {
    this.receiptTime = receiptTime;
  }

  public Integer getReceiptCount() {
    return receiptCount;
  }

  public void setReceiptCount(Integer receiptCount) {
    this.receiptCount = receiptCount;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getDes3UuId() {
    if (this.uuId != null && des3UuId == null) {
      des3UuId = ServiceUtil.encodeToDes3(this.uuId.toString());
    }
    return des3UuId;
  }

  public void setDes3UuId(String des3UuId) {
    this.des3UuId = des3UuId;
  }

  public String getResultStatus() {
    return resultStatus;
  }

  public void setResultStatus(String resultStatus) {
    this.resultStatus = resultStatus;
  }

  public String getSmDateStr() {
    if (smDate != null) {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        smDateStr = format.format(smDate);
      } catch (Exception e) {
        smDateStr = "";
      }
    }
    return smDateStr;
  }

  public void setSmDateStr(String smDateStr) {
    this.smDateStr = smDateStr;
  }


  public String getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(String versionNo) {
    this.versionNo = versionNo;
  }

  public String getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(String keyCode) {
    this.keyCode = keyCode;
  }

  public String getKeyType() {
    return keyType;
  }

  public void setKeyType(String keyType) {
    this.keyType = keyType;
  }

  @Override
  public String toString() {
    return "KpiValidateMain [uuId=" + uuId + ", title=" + title + ", smDate=" + smDate + ", endDate=" + endDate
        + ", dataFrom=" + dataFrom + ", prpYear=" + prpYear + ", priority=" + priority + ", status=" + status
        + ", receiptTime=" + receiptTime + ", receiptCount=" + receiptCount + ", orgName=" + orgName + ", versionNo="
        + versionNo + ", keyCode=" + keyCode + ", keyType=" + keyType + ", des3UuId=" + des3UuId + ", resultStatus="
        + resultStatus + ", smDateStr=" + smDateStr + "]";
  }


}
