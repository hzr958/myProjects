package com.smate.center.batch.form.pub;

import java.io.Serializable;
import java.util.Date;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果、收藏夹关系表.
 * 
 * @author liqinghua
 * 
 */
public class PubFolderForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5705233922580589726L;
  private Long id;
  private String folderId;

  private int articleType;
  private String folderName;
  private String floderDesc;
  private int enabled;
  private String articleName;
  private Long psnId;

  private Date createDate;
  private String des3Id;

  private String result;

  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public Long getId() {
    return id;
  }

  public String getFolderId() {
    return folderId;
  }

  public void setFolderId(String folderId) {
    this.folderId = folderId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public int getArticleType() {
    return articleType;
  }

  public String getFolderName() {
    return folderName;
  }

  public String getFloderDesc() {
    return floderDesc;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setArticleType(int articleType) {
    this.articleType = articleType;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public void setFloderDesc(String floderDesc) {
    this.floderDesc = floderDesc;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

}
