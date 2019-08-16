package com.smate.web.management.model.institution.bpo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 文件下载Model文件，供Action使用.
 * 
 * @author lqh
 * 
 */
public class FileDownload implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1573368662394248276L;
  // 下载文件mine类型
  private String contentType;
  // 指定返回stream流的方法名称
  private String inputName;
  // 指定文件下载名称
  private String contentDisposition;
  // 下载文件缓存
  private Long bufferSize;
  // 文件路径
  private String path;
  // 附件ID
  private Long fileId;
  // 加密附件ID
  private String fdesId;
  // 节点ID
  private Integer nodeId;
  // 单位ID
  private Long insId;

  private String forwardUrl;

  /**
   * 获取真实的文件编号.
   * 
   * @return
   */
  public Long getCurrentFileId() {

    if (fileId != null && fileId != 0) {
      return fileId;
    } else if (fdesId != null && !"".equalsIgnoreCase(fdesId.trim())) {
      String strId = Des3Utils.decodeFromDes3(fdesId);
      String[] ids = strId.split("_");
      if (ids.length > 0) {
        fileId = Long.parseLong(ids[0]);
      }
      if (ids.length > 1 && NumberUtils.isDigits(ids[1])) {
        nodeId = Integer.valueOf(ids[1]);
      }
      if (ids.length > 2 && NumberUtils.isDigits(ids[2])) {
        insId = Long.valueOf(ids[2]);
      }
      return fileId;
    }
    return null;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getInputName() {
    return inputName;
  }

  public void setInputName(String inputName) {
    this.inputName = inputName;
  }

  public String getContentDisposition() {
    return contentDisposition;
  }

  public void setContentDisposition(String contentDisposition) {
    this.contentDisposition = contentDisposition;
  }

  public Long getBufferSize() {
    return bufferSize;
  }

  public void setBufferSize(Long bufferSize) {
    this.bufferSize = bufferSize;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getFdesId() {
    return fdesId;
  }

  public void setFdesId(String fdesId) {
    this.fdesId = fdesId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

}
