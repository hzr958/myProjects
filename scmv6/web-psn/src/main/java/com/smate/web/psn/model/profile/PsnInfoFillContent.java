package com.smate.web.psn.model.profile;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PSN_INFO_FILL_CONTENT")
public class PsnInfoFillContent implements Serializable {

  /**
   * 人员信息补充之教育经历\工作经历信息过渡表
   * 
   * @author zk
   */
  private static final long serialVersionUID = 6638960724916288842L;
  private Long id;
  private Long psnId;
  private Integer insId;
  private String insName;
  private String insDept;
  private Integer fromYear;
  private Integer fromMonth;
  private Integer toyear;
  private Integer toMonth;
  private Integer positionDegreeId;
  private String positionDegreeName;
  private Integer type;
  private Date updateDate;
  private Integer status;
  private String content;
  private Long saveId;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_PSN_INFO_FILL_CONTENT", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "ins_id")
  public Integer getInsId() {
    return insId;
  }

  public void setInsId(Integer insId) {
    this.insId = insId;
  }

  @Column(name = "ins_name")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "ins_dept")
  public String getInsDept() {
    return insDept;
  }

  public void setInsDept(String insDept) {
    this.insDept = insDept;
  }

  @Column(name = "from_year")
  public Integer getFromYear() {
    return fromYear;
  }

  public void setFromYear(Integer fromYear) {
    this.fromYear = fromYear;
  }

  @Column(name = "from_month")
  public Integer getFromMonth() {
    return fromMonth;
  }

  public void setFromMonth(Integer fromMonth) {
    this.fromMonth = fromMonth;
  }

  @Column(name = "to_year")
  public Integer getToyear() {
    return toyear;
  }

  public void setToyear(Integer toyear) {
    this.toyear = toyear;
  }

  @Column(name = "to_month")
  public Integer getToMonth() {
    return toMonth;
  }

  public void setToMonth(Integer toMonth) {
    this.toMonth = toMonth;
  }

  @Column(name = "position_degree_id")
  public Integer getPositionDegreeId() {
    return positionDegreeId;
  }

  public void setPositionDegreeId(Integer positionDegreeId) {
    this.positionDegreeId = positionDegreeId;
  }

  @Column(name = "position_degree_name")
  public String getPositionDegreeName() {
    return positionDegreeName;
  }

  public void setPositionDegreeName(String positionDegreeName) {
    this.positionDegreeName = positionDegreeName;
  }

  @Column(name = "type")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "update_date")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "ins_content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "SAVE_ID")
  public Long getSaveId() {
    return saveId;
  }

  public void setSaveId(Long saveId) {
    this.saveId = saveId;
  }

}
