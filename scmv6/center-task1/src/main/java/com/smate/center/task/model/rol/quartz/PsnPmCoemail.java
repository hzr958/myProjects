package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户确认的成果合作者email信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_COEMAIL")
public class PsnPmCoemail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -709284520274059102L;
  private Long id;
  // 合作者email
  private String email;
  // 用户ID
  private Long psnId;

  public PsnPmCoemail() {
    super();
  }

  public PsnPmCoemail(String email, Long psnId) {
    super();
    this.email = email;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_COEMAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
