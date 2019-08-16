package com.smate.web.psn.model.share;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 文件分享
 * 
 * @author aijiangbin
 *
 */
public class FileShareForm {
  private String des3GrpId;
  private Long grpId;
  // private Integer status = 0 ; // -1=分享记录被删除 ，0=未登录 ， 1=已登录
  private String A; // 加密的 resSendId
  private String B; // 加密的 resReveiverId
  private String C; // 加密的 baseId

  private Long resId;
  private String des3ResId;
  private int resNodeId;
  private Long receivePsnId;
  private String fileName;
  private String fileType;

  private String fileViewType;
  private String filePath;
  private Long fileSize;
  private Date shareDate;

  /** 是否已导入. */
  private int isImported;
  /** 文件状态. */
  private int fileStatus;
  /** -1分享已取消，-2已到有效期，0未登录，1已认领且当前登录人为确认人，2已认领但是当前登录人不是认领人. */
  private int status;
  private String des3ArchId;
  private String loginUrl;
  private String registerUrl;
  private String downloadUrl;// 下载链接
  private String imgThumbUrl;// 缩略图链接

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getGrpId() {
    if (grpId == null && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getA() {
    return A;
  }

  public void setA(String a) {
    A = a;
  }

  public String getB() {
    return B;
  }

  public void setB(String b) {
    B = b;
  }

  public String getC() {
    return C;
  }

  public void setC(String c) {
    C = c;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public int getResNodeId() {
    return resNodeId;
  }

  public void setResNodeId(int resNodeId) {
    this.resNodeId = resNodeId;
  }

  public Long getReceivePsnId() {
    return receivePsnId;
  }

  public void setReceivePsnId(Long receivePsnId) {
    this.receivePsnId = receivePsnId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileViewType() {
    return fileViewType;
  }

  public void setFileViewType(String fileViewType) {
    this.fileViewType = fileViewType;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public Date getShareDate() {
    return shareDate;
  }

  public void setShareDate(Date shareDate) {
    this.shareDate = shareDate;
  }

  public int getIsImported() {
    return isImported;
  }

  public void setIsImported(int isImported) {
    this.isImported = isImported;
  }

  public int getFileStatus() {
    return fileStatus;
  }

  public void setFileStatus(int fileStatus) {
    this.fileStatus = fileStatus;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getDes3ArchId() {
    return des3ArchId;
  }

  public void setDes3ArchId(String des3ArchId) {
    this.des3ArchId = des3ArchId;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getRegisterUrl() {
    return registerUrl;
  }

  public void setRegisterUrl(String registerUrl) {
    this.registerUrl = registerUrl;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  /**
   * @return imgThumbUrl
   */
  public String getImgThumbUrl() {
    return imgThumbUrl;
  }

  /**
   * @param imgThumbUrl 要设置的 imgThumbUrl
   */
  public void setImgThumbUrl(String imgThumbUrl) {
    this.imgThumbUrl = imgThumbUrl;
  }

}
