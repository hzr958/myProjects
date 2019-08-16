package com.smate.center.task.model.email;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONST_EMAIL_INTERVAL")
public class ConstEmailInterval implements Serializable {

  /**
   * 邮件发送间隔常量表
   * 
   * @author zk
   */
  private static final long serialVersionUID = 2637905395684598726L;

  private Integer etempCode;
  // dealInterval 以小时为单位，如一天，则为24
  private Integer dealInterval;
  // 下一次处理时间
  private Date nextDeal;
  private Date currDeal;
  // 状态控制位1:开启；0:关闭；-1：异常.
  private Integer status;

  @Id
  @Column(name = "etemplate_code")
  public Integer getEtempCode() {
    return etempCode;
  }

  public void setEtempCode(Integer etempCode) {
    this.etempCode = etempCode;
  }

  @Column(name = "deal_interval")
  public Integer getDealInterval() {
    return dealInterval;
  }

  public void setDealInterval(Integer dealInterval) {
    this.dealInterval = dealInterval;
  }

  @Column(name = "next_deal")
  public Date getNextDeal() {
    return nextDeal;
  }

  public void setNextDeal(Date nextDeal) {
    this.nextDeal = nextDeal;
  }

  @Column(name = "CURR_DEAL_TIME")
  public Date getCurrDeal() {
    return currDeal;
  }

  public void setCurrDeal(Date currDeal) {
    this.currDeal = currDeal;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
