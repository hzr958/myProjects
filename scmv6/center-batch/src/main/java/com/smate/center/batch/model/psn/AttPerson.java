/**
 * 
 */
package com.smate.center.batch.model.psn;

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
 * @author oyh
 * 
 */
@Entity
@Table(name = "ATT_PERSON")
public class AttPerson implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3272829379839160800L;

  private Long id;
  private Long psnId;
  private Long refPsnId;
  private String refPsnName;
  private String refFirstName;
  private String refLastName;
  private String refTitle;
  private String refInsName;
  private String refHeadUrl;
  private String refDes3PsnId;

  public AttPerson() {
    super();
    // TODO Auto-generated constructor stub
  }

  public AttPerson(Long psnId, Long refPsnId) {
    super();
    this.psnId = psnId;
    this.refPsnId = refPsnId;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ATT_PERSON", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the psnId
   */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the refPsnId
   */
  @Column(name = "REF_PSN_ID")
  public Long getRefPsnId() {
    return refPsnId;
  }

  /**
   * @param refPsnId the refPsnId to set
   */
  public void setRefPsnId(Long refPsnId) {
    this.refPsnId = refPsnId;
  }

  /**
   * @return the refPsnName
   */
  @Column(name = "REF_PSN_NAME")
  public String getRefPsnName() {
    return refPsnName;
  }

  /**
   * @param refPsnName the refPsnName to set
   */
  public void setRefPsnName(String refPsnName) {
    this.refPsnName = refPsnName;
  }

  /**
   * @return the refFirstName
   */
  @Column(name = "REF_FIRST_NAME")
  public String getRefFirstName() {
    return refFirstName;
  }

  /**
   * @param refFirstName the refFirstName to set
   */
  public void setRefFirstName(String refFirstName) {
    this.refFirstName = refFirstName;
  }

  /**
   * @return the refLastName
   */
  @Column(name = "REF_LAST_NAME")
  public String getRefLastName() {
    return refLastName;
  }

  /**
   * @param refLastName the refLastName to set
   */
  public void setRefLastName(String refLastName) {
    this.refLastName = refLastName;
  }

  /**
   * @return the refTitle
   */
  @Column(name = "REF_TITOLO")
  public String getRefTitle() {
    return refTitle;
  }

  /**
   * @param refTitle the refTitle to set
   */
  public void setRefTitle(String refTitle) {
    this.refTitle = refTitle;
  }

  /**
   * @return the refInsName
   */
  @Column(name = "REF_INS_NAME")
  public String getRefInsName() {
    return refInsName;
  }

  /**
   * @param refInsName the refInsName to set
   */
  public void setRefInsName(String refInsName) {
    this.refInsName = refInsName;
  }

  /**
   * @return the refHeadUrl
   */
  @Column(name = "REF_HEAD_URL")
  public String getRefHeadUrl() {
    return refHeadUrl;
  }

  /**
   * @param refHeadUrl the refHeadUrl to set
   */
  public void setRefHeadUrl(String refHeadUrl) {
    this.refHeadUrl = refHeadUrl;
  }

  /**
   * @return the refDes3PsnId
   */
  @Transient
  public String getRefDes3PsnId() {
    return refDes3PsnId;
  }

  /**
   * @param refDes3PsnId the refDes3PsnId to set
   */
  public void setRefDes3PsnId(String refDes3PsnId) {
    this.refDes3PsnId = refDes3PsnId;
  }

}
