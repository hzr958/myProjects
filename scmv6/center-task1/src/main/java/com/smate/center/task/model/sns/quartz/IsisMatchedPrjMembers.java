package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Isis_Matched_PrjMember")
public class IsisMatchedPrjMembers implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7662373031951187062L;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_Isis_Matched_PrjMember", allocationSize = 1)
  private Long ID;
  @Column(name = "NAME")
  private String name;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "company")
  private String company;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getID() {
    return ID;
  }

  public void setID(Long iD) {
    ID = iD;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }



}
