package com.smate.center.batch.model.pdwh.pub.wanfang;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位万方别名表.
 * 
 * @author linyueqin
 * 
 */
@Entity
@Table(name = "WANFANG_INS_NAME")
public class WanfangInsName implements Serializable {

  private static final long serialVersionUID = 4252446012122521856L;
  private Long id;
  private Long insId;
  private String wanfangName;
  private Integer wanfangnLength;
  private Integer freq = 0;
  private Date lastUse;

  public WanfangInsName() {
    super();
  }

  public WanfangInsName(Long id, Long insId, String wanfangName, Integer wanfangnLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.wanfangName = wanfangName;
    this.wanfangnLength = wanfangnLength;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "WANFANG_NAME")
  public String getWanfangName() {
    return wanfangName;
  }

  public void setWanfangName(String wanfangName) {
    this.wanfangName = wanfangName;
  }

  @Column(name = "WANFANGN_LENGTH")
  public Integer getWanfangnLength() {
    return wanfangnLength;
  }

  public void setWanfangnLength(Integer wanfangnLength) {
    this.wanfangnLength = wanfangnLength;
  }

  @Column(name = "FREQ")
  public Integer getFreq() {
    return freq;
  }

  public void setFreq(Integer freq) {
    this.freq = freq;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
  }

  public void setLastUse(Date lastUse) {
    this.lastUse = lastUse;
  }

}
