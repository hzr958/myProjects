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

/**
 * 
 * @author Lrl
 *
 */
@Entity
@Table(name = "PSN_ALIAS")
public class PsnAlias implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8954005696401633839L;

  // 主键id
  private Long id;
  // 个人id
  private Long psnId;
  // 文献id
  private Long dbId;
  // 别名
  private String name;
  // 别名的hash码
  private Long hashName;
  // 是否选中的状态 0 否 1 选中
  private Integer status;
  // 用来排序的列 暂时不用
  private String psnName;
  // 操作日期
  private Date date;

  public PsnAlias() {
    super();
    // TODO Auto-generated constructor stub
  }

  public PsnAlias(Long id, Long psnId, Long dbId, String name, Long hashName, Integer status, String psnName,
      Date date) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.dbId = dbId;
    this.name = name;
    this.hashName = hashName;
    this.status = status;
    this.psnName = psnName;
    this.date = date;
  }



  public PsnAlias(Long psnId, Long dbId, String psnName, String name, Long hashName, Integer status, Date date) {
    super();
    this.psnId = psnId;
    this.dbId = dbId;
    this.psnName = psnName;
    this.name = name;
    this.hashName = hashName;
    this.status = status;
    this.date = date;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_NAME", sequenceName = "SEQ_PSN_ALIAS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NAME")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "NAME_HASH")
  public Long getHashName() {
    return hashName;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "PSN_NAME")
  public String getpsnName() {
    return psnName;
  }

  @Column(name = "REG_DATE")
  public Date getDate() {
    return date;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setHashName(Long hashName) {
    this.hashName = hashName;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setpsnName(String psnName) {
    this.psnName = psnName;
  }

  public void setDate(Date date) {
    this.date = date;
  }



}
