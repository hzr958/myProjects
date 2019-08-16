package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.enums.converter.LikeStatusAttributeConverter;

/**
 * 人员赞详情model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "DYNAMIC_AWARD_PSN")
public class DynamicAwardPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6013261508253608287L;
  private Long recordId;
  private Long awardId;
  private Long awarderPsnId;
  private String awarderName;
  private String awarderEnName;
  private String awarderAvatar;
  private Date awardDate;
  @Convert(converter = LikeStatusAttributeConverter.class)
  private LikeStatusEnum status;
  private String awarderTitle;
  private int isFriend;

  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_AWARD_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  @Column(name = "AWARD_ID")
  public Long getAwardId() {
    return awardId;
  }

  public void setAwardId(Long awardId) {
    this.awardId = awardId;
  }

  @Column(name = "AWARDER_PSNID")
  public Long getAwarderPsnId() {
    return awarderPsnId;
  }

  public void setAwarderPsnId(Long awarderPsnId) {
    this.awarderPsnId = awarderPsnId;
  }

  @Column(name = "AWARDER_NAME")
  public String getAwarderName() {
    return awarderName;
  }

  public void setAwarderName(String awarderName) {
    this.awarderName = awarderName;
  }

  @Column(name = "AWARDER_ENNAME")
  public String getAwarderEnName() {
    return awarderEnName;
  }

  public void setAwarderEnName(String awarderEnName) {
    this.awarderEnName = awarderEnName;
  }

  @Column(name = "AWARDER_AVATAR")
  public String getAwarderAvatar() {
    return awarderAvatar;
  }

  public void setAwarderAvatar(String awarderAvatar) {
    this.awarderAvatar = awarderAvatar;
  }

  @Column(name = "AWARD_DATE")
  public Date getAwardDate() {
    return awardDate;
  }

  public void setAwardDate(Date awardDate) {
    this.awardDate = awardDate;
  }

  @Column(name = "status")
  public LikeStatusEnum getStatus() {
    return status;
  }

  public void setStatus(LikeStatusEnum status) {
    this.status = status;
  }

  @Transient
  public String getAwarderTitle() {
    return awarderTitle;
  }

  public void setAwarderTitle(String awarderTitle) {
    this.awarderTitle = awarderTitle;
  }

  @Transient
  public int getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(int isFriend) {
    this.isFriend = isFriend;
  }

}
