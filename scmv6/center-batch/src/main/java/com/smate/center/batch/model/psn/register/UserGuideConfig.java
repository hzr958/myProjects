package com.smate.center.batch.model.psn.register;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 新手指南用户配置信息
 * 
 * @author liangguokeng
 * 
 */
@Entity
@Table(name = "USER_GUIDE_CONFIG")
public class UserGuideConfig implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1478069854350798823L;

  private Long id;

  private Long psnId;

  private Long show;// 显示新手指南

  private Long showTimes;// 新手指南显示了多少次

  private Integer first;// 是否第一次登录,缓存起来,用于页面跳转控制

  private Integer hasPub;// pdwh是否有成果认领,缓存起来,用于页面跳转控制

  @Deprecated
  private Integer updateShowTimes; // 是否已经更新显示新手指南次数,防止页面刷新导致showTimes值不断增加

  private Integer thirthDay;// 上一次登录是30天前

  public UserGuideConfig() {
    super();
  }

  public UserGuideConfig(Long psnId, Long show, Long showTimes) {
    super();
    this.psnId = psnId;
    this.show = show;
    this.showTimes = showTimes;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_USER_GUIDE_CONFIG", allocationSize = 1)
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "SHOW")
  public Long getShow() {
    return show;
  }

  @Column(name = "SHOW_TIMES")
  public Long getShowTimes() {
    return showTimes;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setShow(Long show) {
    this.show = show;
  }

  public void setShowTimes(Long showTimes) {
    this.showTimes = showTimes;
  }

  @Transient
  public Integer getHasPub() {
    return hasPub;
  }

  public void setHasPub(Integer hasPub) {
    this.hasPub = hasPub;
  }

  @Transient
  public Integer getFirst() {
    return first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  @Transient
  public Integer getUpdateShowTimes() {
    return updateShowTimes;
  }

  public void setUpdateShowTimes(Integer updateShowTimes) {
    this.updateShowTimes = updateShowTimes;
  }

  @Transient
  public Integer getThirthDay() {
    return thirthDay;
  }

  public void setThirthDay(Integer thirthDay) {
    this.thirthDay = thirthDay;
  }

}
