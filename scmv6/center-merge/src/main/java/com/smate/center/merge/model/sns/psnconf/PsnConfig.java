package com.smate.center.merge.model.sns.psnconf;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 个人配置：主表.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG")
public class PsnConfig implements PsnCnfBase, Serializable {
  private static final long serialVersionUID = -1222234011873285773L;
  // 配置主键
  private Long cnfId;
  // 人员psn_id
  private Long psnId;
  // 任务状态类别：1、成功，-1、失败，0、待运行
  private Integer status = 0;
  // 注意：必须status=0时，runs才有效。
  // 执行类别（数字表示二进制位，待执行类别）：
  // 1(1)更新数据，2(1<<1)、调整模块顺序，2(1<<2)、移除过时模块，3(1<<3)、清空配置（慎用，不可恢复），
  // 4(1<<4)、旧配置转换，6保留位，7项目，8成果，9工作经历，10教育经历，11学科领域，12所教课程，13个人简介，14联系方式。如：仅调整模块顺序，则值为1；补全项目和成果，则值为1<<6
  // & 1<<7(项目和成果的位运算)；其他情况根据位的组合运算，得到相应的值；值为0时，什么效果都没有，相当于空跑
  private Long runs = 0L;
  // 创建日期
  private Date createDate = new Date();
  // 更新日期
  private Date updateDate = new Date();
  // 简明错误信息
  private String errMsg;

  public PsnConfig() {}

  public PsnConfig(Long psnId) {
    this.psnId = psnId;
  }

  @Override
  @Id
  @Column(name = "CNF_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_CONFIG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "RUNS")
  public Long getRuns() {
    return runs;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  @Column(name = "ERR_MSG")
  public String getErrMsg() {
    return errMsg;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setRuns(Long runs) {
    this.runs = runs;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  /*
   * （非 Javadoc）
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((runs == null) ? 0 : runs.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    return result;
  }

  /*
   * （非 Javadoc）
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PsnConfig)) {
      return false;
    }
    PsnConfig other = (PsnConfig) obj;
    if (psnId == null) {
      if (other.psnId != null) {
        return false;
      }
    } else if (!psnId.equals(other.psnId)) {
      return false;
    }
    if (runs == null) {
      if (other.runs != null) {
        return false;
      }
    } else if (!runs.equals(other.runs)) {
      return false;
    }
    if (status == null) {
      if (other.status != null) {
        return false;
      }
    } else if (!status.equals(other.status)) {
      return false;
    }
    return true;
  }
}
