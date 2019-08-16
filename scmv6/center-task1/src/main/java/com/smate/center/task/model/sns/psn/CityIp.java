package com.smate.center.task.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 城市Ip段
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "CITY_IP")
public class CityIp implements Serializable {

  private static final long serialVersionUID = -7612566962379135389L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYS_USER_LOGIN_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  // 转换后的IP开始段
  @Column(name = "START_IP")
  private Long startIp;

  // 转换后的IP结束段
  @Column(name = "END_IP")
  private Long endIp;

  // 省
  @Column(name = "PROVINCE")
  private String province;

  // 市
  @Column(name = "CITY")
  private String city;

  // 营运商
  @Column(name = "ISP")
  private String isp;

  // IP开始段
  @Column(name = "START_IP1")
  private String startIp1;

  // IP结束段
  @Column(name = "END_IP1")
  private String endIp1;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStartIp() {
    return startIp;
  }

  public void setStartIp(Long startIp) {
    this.startIp = startIp;
  }

  public Long getEndIp() {
    return endIp;
  }

  public void setEndIp(Long endIp) {
    this.endIp = endIp;
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

  public String getIsp() {
    return isp;
  }

  public void setIsp(String isp) {
    this.isp = isp;
  }

  public String getStartIp1() {
    return startIp1;
  }

  public void setStartIp1(String startIp1) {
    this.startIp1 = startIp1;
  }

  public String getEndIp1() {
    return endIp1;
  }

  public void setEndIp1(String endIp1) {
    this.endIp1 = endIp1;
  }

}
