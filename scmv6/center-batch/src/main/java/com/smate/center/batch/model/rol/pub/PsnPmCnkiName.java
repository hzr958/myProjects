package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户CNKI名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_CNKINAME")
public class PsnPmCnkiName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8527108098107827848L;

  private Long id;
  // 用户姓名的各种组合(小写)
  private String name;
  // 用户ID
  private Long psnId;
  // zh_name=1; addt_name=2
  private Integer type;

  public PsnPmCnkiName() {}

  public PsnPmCnkiName(String name, Long psnId, Integer type) {
    super();
    this.name = name;
    this.psnId = psnId;
    this.type = type;
  }

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_CNKINAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
