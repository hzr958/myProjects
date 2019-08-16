package com.smate.sie.core.base.utils.model.validate.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "TASK_KPI_VALIDATE_MAIN")
public class SieTaskKpiValidateMain implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 991456073391410594L;

  @Id
  @Column(name = "UUID")
  private String uuId;

  /* 验证文档或业务标题 */
  @Column(name = "TITLE")
  private String title;

  @Column(name = "DATA")
  private String data;

  @Column(name = "SPLIT_STATUS")
  private Integer splitStatus; // 拆分处理状态，0 待处理，2拆分成功；9 拆分失败

  @Column(name = "SPLIT_SUCCESS_COUNT")
  private Long splitSuccessCount; // detail表拆分成功条数数

  @Column(name = "SPLIT_ERROR_COUNT")
  private Long splitErrorCount; // detail表拆分失败条数数

  @Column(name = "SPLIT_COUNT")
  private Long splitCount; // detail表需拆分条数



  public SieTaskKpiValidateMain() {
    super();
  }

  public SieTaskKpiValidateMain(String uuId, String title, String data, Integer splitStatus, Long splitSuccessCount,
      Long splitErrorCount, Long splitCount) {
    super();
    this.uuId = uuId;
    this.title = title;
    this.data = data;
    this.splitStatus = splitStatus;
    this.splitSuccessCount = splitSuccessCount;
    this.splitErrorCount = splitErrorCount;
    this.splitCount = splitCount;
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

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Integer getSplitStatus() {
    return splitStatus;
  }

  public void setSplitStatus(Integer splitStatus) {
    this.splitStatus = splitStatus;
  }

  public Long getSplitSuccessCount() {
    return splitSuccessCount;
  }

  public void setSplitSuccessCount(Long splitSuccessCount) {
    this.splitSuccessCount = splitSuccessCount;
  }

  public Long getSplitErrorCount() {
    return splitErrorCount;
  }

  public void setSplitErrorCount(Long splitErrorCount) {
    this.splitErrorCount = splitErrorCount;
  }

  public Long getSplitCount() {
    return splitCount;
  }

  public void setSplitCount(Long splitCount) {
    this.splitCount = splitCount;
  }

  @Override
  public String toString() {
    return "TmpKpiValidateMain [uuId=" + uuId + ", title=" + title + ", data=" + data + ", splitStatus=" + splitStatus
        + ", splitSuccessCount=" + splitSuccessCount + ", splitErrorCount=" + splitErrorCount + ", splitCount="
        + splitCount + "]";
  }
}
