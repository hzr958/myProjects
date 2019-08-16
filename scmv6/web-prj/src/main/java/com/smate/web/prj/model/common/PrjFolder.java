package com.smate.web.prj.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目、收藏夹关系表.
 * 
 * @author tj
 * 
 */
@Entity
@Table(name = "PRJ_FOLDER")
public class PrjFolder implements Serializable {

  /**
   * tj
   */
  private static final long serialVersionUID = 1278946378192959270L;
  private Long id;
  private String name;
  private String floderDesc;
  private Long psnId;

  public PrjFolder(Long id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

  @Id
  @Column(name = "FOLDER_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "OWNER_PSN_ID")
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

  @Column(name = "FOLDER_DESC")
  public String getFloderDesc() {
    return floderDesc;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFloderDesc(String floderDesc) {
    this.floderDesc = floderDesc;
  }

}
