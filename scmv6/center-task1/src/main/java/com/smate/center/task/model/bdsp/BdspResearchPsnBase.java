package com.smate.center.task.model.bdsp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 科研人员
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_RESEARCH_PSN_BASE")
public class BdspResearchPsnBase implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 人员id
   */
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;
  /**
   * 学历id
   */
  @Column(name = "EDU_ID")
  private Long eduId;
  /**
   * 学位id
   */
  @Column(name = "DEGREE_ID")
  private Long degreeId;
  /**
   * 性别id 1男，0女
   */
  @Column(name = "GENDER_ID")
  private Integer genderId;
  /**
   * 职称id
   */
  @Column(name = "POS_ID")
  private Long posId;
  /**
   * 出生（年）
   */
  @Column(name = "BIRTHDAY")
  private String birthday;
  @Column(name = "CREATE_DATE")
  private Date createDate;


  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getEduId() {
    return eduId;
  }

  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  public Long getDegreeId() {
    return degreeId;
  }

  public void setDegreeId(Long degreeId) {
    this.degreeId = degreeId;
  }

  public Integer getGenderId() {
    return genderId;
  }

  public void setGenderId(Integer genderId) {
    this.genderId = genderId;
  }

  public Long getPosId() {
    return posId;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }


}
