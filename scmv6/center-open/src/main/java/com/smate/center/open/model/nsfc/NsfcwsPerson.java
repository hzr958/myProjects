package com.smate.center.open.model.nsfc;

import java.io.Serializable;

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
 * 基金委webservice集成smate成果接口获取GoogleScholar库相关人员信息.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "NSFC_WS_PERSON")
public class NsfcwsPerson implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3489030855516345947L;
  @Id
  @Column(name = "PSN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFCWS_PERSON", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Transient
  private String des3Id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "INS_NAME")
  private String insName;
  @Column(name = "HTTP")
  private String http;
  @Column(name = "AVATARS")
  private String avatars;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "PUB_NUM")
  private Integer pubNum;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "DUP_NAME")
  private String dupName;
  @Column(name = "DUP_INS_NAME")
  private String dupInsName;

  public NsfcwsPerson() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDes3Id() {
    if (this.id != null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getHttp() {
    return http;
  }

  public void setHttp(String http) {
    this.http = http;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getPubNum() {
    return pubNum;
  }

  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDupName() {
    return dupName;
  }

  public void setDupName(String dupName) {
    this.dupName = dupName;
  }

  public String getDupInsName() {
    return dupInsName;
  }

  public void setDupInsName(String dupInsName) {
    this.dupInsName = dupInsName;
  }

}
