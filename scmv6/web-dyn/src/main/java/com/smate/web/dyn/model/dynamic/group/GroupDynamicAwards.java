package com.smate.web.dyn.model.dynamic.group;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.enums.converter.LikeStatusAttributeConverter;

/**
 * 动态赞记录
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GROUP_DYNAMIC_AWARDS")
public class GroupDynamicAwards {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GROUP_DYNAMIC_AWARDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "DYN_ID")
  private Long dynId;// 被赞的动态id
  @Column(name = "AWARD_PSN_ID")
  private Long awardPsnId;// 赞的人员id
  @Column(name = "AWARD_DATE")
  private Date awardDate;// 赞或者取消赞的时间
  @Column(name = "STATUS")
  @Convert(converter = LikeStatusAttributeConverter.class)
  private LikeStatusEnum status;// 状态0：赞 1：取消赞

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Date getAwardDate() {
    return awardDate;
  }

  public void setAwardDate(Date awardDate) {
    this.awardDate = awardDate;
  }

  public LikeStatusEnum getStatus() {
    return status;
  }

  public void setStatus(LikeStatusEnum status) {
    this.status = status;
  }

  public Long getAwardPsnId() {
    return awardPsnId;
  }

  public void setAwardPsnId(Long awardPsnId) {
    this.awardPsnId = awardPsnId;
  }

}
