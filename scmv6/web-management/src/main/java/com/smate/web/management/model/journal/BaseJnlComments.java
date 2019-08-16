package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * cwli基础期刊评论表.
 */
@Entity
@Table(name = "BASE_JOURNAL_COMMENTS")
public class BaseJnlComments implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1027696935335739228L;
  private Long id;
  private Long jnlId;
  private Long psnId;
  private String des3PsnId;
  private String psnName;
  private String psnAvatars;
  // 质量
  private Integer quality;
  // 难易程度
  private Integer difficulty;
  // 审稿周期
  private Integer cycle;
  // 期刊学科1
  private String dis1;
  // 期刊学科2
  private String dis2;
  // 评论
  private String comments;
  // 研究方向
  private String research;
  private Date createDate;

  public BaseJnlComments() {
    super();
    this.createDate = new Date();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_COMMENTS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "QUALITY")
  public Integer getQuality() {
    return quality;
  }

  public void setQuality(Integer quality) {
    this.quality = quality;
  }

  @Column(name = "DIFFICULTY")
  public Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Integer difficulty) {
    this.difficulty = difficulty;
  }

  @Column(name = "CYCLE")
  public Integer getCycle() {
    return cycle;
  }

  public void setCycle(Integer cycle) {
    this.cycle = cycle;
  }

  @Column(name = "DIS1")
  public String getDis1() {
    return dis1;
  }

  public void setDis1(String dis1) {
    this.dis1 = dis1;
  }

  @Column(name = "DIS2")
  public String getDis2() {
    return dis2;
  }

  public void setDis2(String dis2) {
    this.dis2 = dis2;
  }

  @Column(name = "COMMENTS")
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  @Column(name = "RESEARCH")
  public String getResearch() {
    return research;
  }

  public void setResearch(String research) {
    this.research = research;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "PSN_AVATARS")
  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  @Transient
  public String getDes3PsnId() {
    if (this.psnId != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

}
