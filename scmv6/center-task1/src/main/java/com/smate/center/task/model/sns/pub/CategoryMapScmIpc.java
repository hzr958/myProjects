package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY_MAP_SCM_IPC")
public class CategoryMapScmIpc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;
  @Column(name = "IPC_CODE")
  private String IpcCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Long scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }

  public String getIpcCode() {
    return IpcCode;
  }

  public void setIpcCode(String ipcCode) {
    IpcCode = ipcCode;
  }

}
