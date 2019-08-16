package com.smate.web.psn.model.recommend;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组人员推荐表
 * 
 * @author lhd
 *
 */
@Entity
@Table(name = "GROUP_PSN_RECOMMEND")
public class GroupPsnRecommend implements Serializable {

  private static final long serialVersionUID = 8344193232403782181L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_PSN_RECOMMEND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "GROUP_ID")
  private Long groupId;// 群组编号
  @Column(name = "RECOMMEND_PSN_ID")
  private Long recommendPsnId;// 推荐人员psnId
  @Column(name = "RECOMMEND_PSN_NAME")
  private String recommendPsnName;// 推荐人员姓名
  @Column(name = "RECOMMEND_PSN_FIRST_NAME")
  private String recommendPsnFirstName;// 推荐人员名
  @Column(name = "RECOMMEND_PSN_LAST_NAME")
  private String recommendPsnLastName;// 推荐人员姓
  @Column(name = "RECOMMEND_PSN_HEAD_URL")
  private String recommendPsnheadUrl;// 推荐人员头像url
  @Column(name = "RECOMMEND_PSN_TITOLO")
  private String recommendPsnTitolo;// 推荐人员头衔
  @Column(name = "RECOMMEND_TYPE")
  private String recommendType;// 推荐来源：1工作经历，2教育经历，3好友的好友，4成果合作，5项目合作，6科研领域
  @Column(name = "SCORE")
  private Double score;// 相识度得分


  public GroupPsnRecommend() {}


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getRecommendPsnId() {
    return recommendPsnId;
  }

  public void setRecommendPsnId(Long recommendPsnId) {
    this.recommendPsnId = recommendPsnId;
  }

  public String getRecommendPsnName() {
    return recommendPsnName;
  }

  public void setRecommendPsnName(String recommendPsnName) {
    this.recommendPsnName = recommendPsnName;
  }

  public String getRecommendPsnFirstName() {
    return recommendPsnFirstName;
  }

  public void setRecommendPsnFirstName(String recommendPsnFirstName) {
    this.recommendPsnFirstName = recommendPsnFirstName;
  }

  public String getRecommendPsnLastName() {
    return recommendPsnLastName;
  }

  public void setRecommendPsnLastName(String recommendPsnLastName) {
    this.recommendPsnLastName = recommendPsnLastName;
  }

  public String getRecommendPsnheadUrl() {
    return recommendPsnheadUrl;
  }

  public void setRecommendPsnheadUrl(String recommendPsnheadUrl) {
    this.recommendPsnheadUrl = recommendPsnheadUrl;
  }

  public String getRecommendPsnTitolo() {
    return recommendPsnTitolo;
  }

  public void setRecommendPsnTitolo(String recommendPsnTitolo) {
    this.recommendPsnTitolo = recommendPsnTitolo;
  }

  public String getRecommendType() {
    return recommendType;
  }

  public void setRecommendType(String recommendType) {
    this.recommendType = recommendType;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }


}
