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
 * scopus用户确认成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_SPSCONAME")
public class PsnPmSpsConame implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8621506063234641810L;
  private Long id;
  // 用户名(小写)
  private String name;
  // 用户ID
  private Long psnId;

  public PsnPmSpsConame() {
    super();
  }

  public PsnPmSpsConame(String name, Long psnId) {
    super();
    this.name = name;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_SPSCONAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
