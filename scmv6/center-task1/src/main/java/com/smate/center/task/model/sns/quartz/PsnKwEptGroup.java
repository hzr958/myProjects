package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员关键词分组实体类
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "psn_kw_ept_group")
public class PsnKwEptGroup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2230256877817374397L;
  private Long id;
  private String kwTxt;
  private Long gid;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_psn_kw_ept_group", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "KEYWORD_TXT")
  public String getKwTxt() {
    return kwTxt;
  }

  @Column(name = "GID")
  public Long getGid() {
    return gid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  public void setGid(Long gid) {
    this.gid = gid;
  }
}
