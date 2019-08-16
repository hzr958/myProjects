package com.smate.web.group.model.group.pub;

/**
 * @author yamingd 成果全文链接或全文附件.
 */
public class PubFulltextExtend implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4455045589698428059L;

  private String fullTextUrl = "";
  private Long fullTextFileId;
  private String fileName;
  private String fileExt;
  private Integer dbid;
  private Integer fileNodeId;
  private Long insId;

  public PubFulltextExtend(String url, Long fileId, String fileName, String fileExt, Integer dbid, Integer fileNodeId,
      Long insId) {
    this.fullTextUrl = url;
    this.fullTextFileId = fileId;
    this.fileName = fileName;
    this.fileExt = fileExt;
    this.dbid = dbid;
    this.fileNodeId = fileNodeId;
    this.insId = insId;
  }

  /**
   * @return the fullTextUrl
   */
  public String getFullTextUrl() {
    return fullTextUrl;
  }

  /**
   * @param fullTextUrl the fullTextUrl to set
   */
  public void setFullTextUrl(String fullTextUrl) {
    this.fullTextUrl = fullTextUrl;
  }

  /**
   * @return the fullTextFileId
   */
  public Long getFullTextFileId() {
    return fullTextFileId;
  }

  /**
   * @param fullTextFileId the fullTextFileId to set
   */
  public void setFullTextFileId(Long fullTextFileId) {
    this.fullTextFileId = fullTextFileId;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }

  /**
   * @return the fileExt
   */
  public String getFileExt() {
    return fileExt;
  }

  /**
   * @param fileExt the fileExt to set
   */
  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public Integer getFileNodeId() {
    return fileNodeId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setFileNodeId(Integer fileNodeId) {
    this.fileNodeId = fileNodeId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
