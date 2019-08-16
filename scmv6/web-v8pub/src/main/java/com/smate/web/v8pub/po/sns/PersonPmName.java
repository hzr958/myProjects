package com.smate.web.v8pub.po.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SNS人员姓名和姓名别称信息记录表，初始数据来源于PSN_PM_ISINAME,PSN_PM_cnkiNAME
 * 
 * @author LIJUN
 * @date 2018年3月16日
 */
@Entity
@Table(name = "PERSON_PM_NAME")
public class PersonPmName implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 5368334683490928240L;
  private Long id;// 主键id
  private Long psnId;// 人员Id
  private Long insId;// 机构Id
  private String name;// 姓名
  private Integer type;// 姓名类型（各种组合）
  private Integer nameSource;

  // nameSource 姓名来源标识 ，
  // 1表示系统变化的人名，2表示地址来源的人名（在cnki表中，type=2的为addt_fullname，在isi表中
  // addt_initname=3,addt_fullname=4）
  /**
   * type<br>
   * Full_name=1;(包含原有isi cnki表 的full_name，addt_fullname) prefix_name=2;主要是isi表的 ，<br>
   * init_name=5;(包含原有isi cnki表 的init_name，addt_initname)
   * 
   * 
   */

  // addt_initname=3(isi
  // 来源为地址的人名);addt_fullname=4(isi、cnki来源为地址的fullname);.isi

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON_PM_NAME", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)

  public Long getId() {
    return id;
  }

  public PersonPmName(Long psnId, Long insId, String name, Integer type, Integer nameSource) {
    super();
    this.psnId = psnId;
    this.insId = insId;
    this.name = name;
    this.type = type;
    this.nameSource = nameSource;
  }

  public PersonPmName() {
    super();
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "NAME_SOURCE")
  public Integer getNameSource() {
    return nameSource;
  }

  public void setNameSource(Integer nameSource) {
    this.nameSource = nameSource;
  }
}
