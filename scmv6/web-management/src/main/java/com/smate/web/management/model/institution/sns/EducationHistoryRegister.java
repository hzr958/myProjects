package com.smate.web.management.model.institution.sns;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 注册教育经历.
 * 
 * @author lichangwen
 */
@Entity
@Table(name = "PSN_EDU_HISTORY")
public class EducationHistoryRegister implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5180244016528646350L;

  /**
   * 主键.
   */
  private Long eduId;

  // ljj 是否是首要单位
  private Long isPrimary;

  // 用户ID
  private PersonRegister person;

  private Long insId;
  /**
   * 机构名，当instituion为空时使用.
   */
  private String insName;
  /**
   * 专业.
   */
  private String study;
  /**
   * 学位.
   */
  private String degree;
  private String degreeName;

  /**
   * 开始年份.
   */
  private Long fromYear;
  /**
   * 结束月份.
   */
  private Long fromMonth;
  /**
   * 结束年份.
   */
  private Long toYear;
  /**
   * 结束月份.
   */
  private Long toMonth;

  /**
   * 加密ID.
   */
  private String des3Id;

  // 编辑教育经历时使用.
  private Map<Long, String> degreeList;

  public EducationHistoryRegister() {
    super();
  }

  /**
   * @return educationId
   */
  @Id
  @Column(name = "EDU_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_EDU_HISTORY", allocationSize = 1)
  public Long getEduId() {
    return eduId;
  }

  /**
   * @param educationId
   */
  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @return the person
   */
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "PSN_ID", insertable = true, updatable = true)
  public PersonRegister getPerson() {
    return person;
  }

  /**
   * @param person the person to set
   */
  public void setPerson(PersonRegister person) {
    this.person = person;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return institutionName
   */
  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  /**
   * @param institutionName
   */
  public void setInsName(String insName) {
    this.insName = insName;
  }

  /**
   * @return study
   */
  @Column(name = "STUDY")
  public String getStudy() {
    return study;
  }

  /**
   * @param study
   */
  public void setStudy(String study) {
    this.study = study;
  }

  /**
   * @return degree
   */
  @Column(name = "DEGREE")
  public String getDegree() {
    return degree;
  }

  /**
   * @param degree
   */
  public void setDegree(String degree) {
    this.degree = degree;
  }

  /**
   * @return fromYear
   */
  @Column(name = "FROM_YEAR")
  public Long getFromYear() {
    return fromYear;
  }

  /**
   * @param fromYear
   */
  public void setFromYear(Long fromYear) {
    this.fromYear = fromYear;
  }

  /**
   * @return fromMonth
   */
  @Column(name = "FROM_MONTH")
  public Long getFromMonth() {
    return fromMonth;
  }

  /**
   * @param fromMonth
   */
  public void setFromMonth(Long fromMonth) {
    this.fromMonth = fromMonth;
  }

  /**
   * @return toYear
   */
  @Column(name = "TO_YEAR")
  public Long getToYear() {
    return toYear;
  }

  /**
   * @param toYear
   */
  public void setToYear(Long toYear) {
    this.toYear = toYear;
  }

  /**
   * @return toMonth
   */
  @Column(name = "TO_MONTH")
  public Long getToMonth() {
    return toMonth;
  }

  @Column(name = "IS_PRIMARY")
  public Long getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(Long isPrimary) {
    this.isPrimary = isPrimary;
  }

  /**
   * @param toMonth
   */
  public void setToMonth(Long toMonth) {
    this.toMonth = toMonth;
  }

  @Column(name = "DEGREE_NAME")
  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  @Transient
  public String getDes3Id() {

    if (this.eduId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.eduId.toString());
    }
    return des3Id;
  }

  @Transient
  public Map<Long, String> getDegreeList() {
    return degreeList;
  }

  public void setDegreeList(Map<Long, String> degreeList) {
    this.degreeList = degreeList;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }
}
