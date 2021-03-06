package com.smate.sie.center.task.model.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 临时表处理单位获取单位地址
 * 
 * @author ztg
 *
 */
@Entity
@Table(name = "TASK_INS_BAIDU_GET_ADDR")
public class SieTaskInsBaiDuGetAddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3573111377585938377L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_BAIDU_GET_ADDR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "TMP_INS_ID") // 处理表的主键Id
  private Long tmpInsId;

  @Column(name = "TMP_INS_NAME") // 单位名
  private String tmpInsName;

  @Column(name = "TMP_INS_NAME_HASH") // 单位名
  private Long tmpInsNameHash;

  @Column(name = "COUNTRY") // 国家
  private String country;

  @Column(name = "PROVINCE") // 省
  private String province;

  @Column(name = "CITY") // 市
  private String city;

  @Column(name = "FULL_ADDRESS") // 获取的完整地址
  private String fullAddress;

  @Column(name = "STATUS") // 处理状态，0未处理，1处理成功，2处理失败
  private Integer status;

  @Column(name = "LANGUAGE") // 语言1 中文 2英文
  private Integer language = 1;

  public SieTaskInsBaiDuGetAddr() {
    super();
  }



  public SieTaskInsBaiDuGetAddr(Long id, Long tmpInsId, String tmpInsName, Long tmpInsNameHash, String country,
      String province, String city, String fullAddress, Integer status, Integer language) {
    super();
    this.id = id;
    this.tmpInsId = tmpInsId;
    this.tmpInsName = tmpInsName;
    this.tmpInsNameHash = tmpInsNameHash;
    this.country = country;
    this.province = province;
    this.city = city;
    this.fullAddress = fullAddress;
    this.status = status;
    this.language = language;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTmpInsId() {
    return tmpInsId;
  }

  public void setTmpInsId(Long tmpInsId) {
    this.tmpInsId = tmpInsId;
  }

  public String getTmpInsName() {
    return tmpInsName;
  }

  public void setTmpInsName(String tmpInsName) {
    this.tmpInsName = tmpInsName;
  }

  public Long getTmpInsNameHash() {
    return tmpInsNameHash;
  }

  public void setTmpInsNameHash(Long tmpInsNameHash) {
    this.tmpInsNameHash = tmpInsNameHash;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getFullAddress() {
    return fullAddress;
  }

  public void setFullAddress(String fullAddress) {
    this.fullAddress = fullAddress;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }
}
