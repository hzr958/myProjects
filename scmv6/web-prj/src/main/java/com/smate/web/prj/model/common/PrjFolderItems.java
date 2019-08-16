package com.smate.web.prj.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 文件夹-项目关系表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_FOLDER_ITEMS")
public class PrjFolderItems implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6881054552391596506L;
  private Long id;
  private Long folderId;
  private Long prjId;

  public PrjFolderItems() {
    super();
  }

  public PrjFolderItems(Long folderId, Long prjId) {
    super();
    this.folderId = folderId;
    this.prjId = prjId;
  }

  @Id
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FOLDER_ID")
  public Long getFolderId() {
    return folderId;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setFolderId(Long folderId) {
    this.folderId = folderId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

}
