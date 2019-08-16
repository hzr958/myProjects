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
 * 用户确认CNKI成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_CNKICONAME")
public class PsnPmCnkiConame implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8791445092433504634L;
  private Long id;
  // 用户名(小写)
  private String name;
  // 用户ID
  private Long psnId;

  public PsnPmCnkiConame() {
    super();
  }

  public PsnPmCnkiConame(String name, Long psnId) {
    super();
    this.name = name;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_CNKICONAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
