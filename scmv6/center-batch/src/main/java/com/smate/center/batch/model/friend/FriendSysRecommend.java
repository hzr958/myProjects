package com.smate.center.batch.model.friend;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 个人好友系统智能推荐.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_RECOMMEND")
public class FriendSysRecommend implements Serializable {

  private static final long serialVersionUID = 8485203851484332882L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 推荐好友psnId
  private Long tempPsnId;
  // 推荐好友的姓名
  private String tempPsnName;
  private String tempPsnFirstName;
  private String tempPsnLastName;
  // 推荐好友头像url
  private String tempPsnHeadUrl;
  // 推荐好友头衔
  private String tempPsnTitel;
  // 推荐来源：1工作经历，2教育经历，3好友的好友，4成果合作，5项目合作，6科研领域
  private String recommendType;
  // 相识度得分
  private Double score;

  public FriendSysRecommend() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_RECOMMEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "RECOMMEND_PSN_ID")
  public Long getTempPsnId() {
    return tempPsnId;
  }

  public void setTempPsnId(Long tempPsnId) {
    this.tempPsnId = tempPsnId;
  }

  @Column(name = "RECOMMEND_PSN_NAME")
  public String getTempPsnName() {
    return tempPsnName;
  }

  public void setTempPsnName(String tempPsnName) {
    this.tempPsnName = tempPsnName;
  }

  @Column(name = "RECOMMEND_PSN_FIRST_NAME")
  public String getTempPsnFirstName() {
    return tempPsnFirstName;
  }

  public void setTempPsnFirstName(String tempPsnFirstName) {
    this.tempPsnFirstName = tempPsnFirstName;
  }

  @Column(name = "RECOMMEND_PSN_LAST_NAME")
  public String getTempPsnLastName() {
    return tempPsnLastName;
  }

  public void setTempPsnLastName(String tempPsnLastName) {
    this.tempPsnLastName = tempPsnLastName;
  }

  @Column(name = "RECOMMEND_PSN_HEAD_URL")
  public String getTempPsnHeadUrl() {
    return tempPsnHeadUrl;
  }

  public void setTempPsnHeadUrl(String tempPsnHeadUrl) {
    this.tempPsnHeadUrl = tempPsnHeadUrl;
  }

  @Column(name = "RECOMMEND_PSN_TITOLO")
  public String getTempPsnTitel() {
    return tempPsnTitel;
  }

  public void setTempPsnTitel(String tempPsnTitel) {
    this.tempPsnTitel = tempPsnTitel;
  }

  @Column(name = "RECOMMEND_TYPE")
  public String getRecommendType() {
    return recommendType;
  }

  public void setRecommendType(String recommendType) {
    this.recommendType = recommendType;
  }

  @Column(name = "SCORE")
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
