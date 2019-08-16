package com.smate.web.management.model.institution.bpo;

import java.io.File;
import java.io.Serializable;

import com.smate.core.base.file.model.ArchiveFile;

/**
 * 上传单文件Model文件，供Action使用.
 * 
 * @author lqh
 * 
 */
public class FileUploadSimple implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4013434463476024571L;

  private String fileDesc;
  private String fileType;
  private File filedata;
  private String filedataContentType;
  private String filedataFileName;
  private String denyTypes;
  private String allowType;
  private Long fileSize;
  // MB
  private Long limitSize;
  // 带单位长度的
  private String limitLength;
  private String saveResult;
  private Boolean result;
  private String des3Id;
  private Long psnId;
  private Long insId;
  private Long insRegId;
  private Long dataId;
  private String jsessionId;
  private int x;
  private int y;
  private int width;
  private int height;
  private String srcImage;
  private Long folderId;
  private String folderIds;
  private String folderName;
  private Long groupId;
  private int nodeId;
  private int currentNodeId;
  private String currentPsnId;
  private ArchiveFile archiveFile;
  private String navAction;
  private String selectMenuId;
  private int isNeedSendGroupEmail;// 是否选择了向群组成员发送电子邮件的通知0 没选择，1 选择
  private String psnIds;// 当isNeedSendGroupEmail=1时，邮件发送到指定人，用逗号,隔开
  private String groupMemberIsAll;// 当psnIds=""时，有2种情况(没有找开选人插件和找开后全不选),需要这个属性判断是否给所有人发送
  private Integer isNeedSyncToMyFile;// 是否同步到"我的文件"中,0 不同步,1 同步
  // 1头像 ，2单位LOGO，3群组图片，4文件缩略图，5简历头像.
  private int imgType;
  // 单位域名
  private String domain;

  public FileUploadSimple() {
    super();
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public File getFiledata() {
    return filedata;
  }

  public void setFiledata(File filedata) {
    this.filedata = filedata;
  }

  public String getFiledataContentType() {
    return filedataContentType;
  }

  public String getJsessionId() {
    return jsessionId;
  }

  public void setJsessionId(String jsessionId) {
    this.jsessionId = jsessionId;
  }

  public void setFiledataContentType(String filedataContentType) {
    this.filedataContentType = filedataContentType;
  }

  public String getFiledataFileName() {
    return filedataFileName;
  }

  public void setFiledataFileName(String filedataFileName) {
    this.filedataFileName = filedataFileName;
  }

  public String getDenyTypes() {
    return denyTypes;
  }

  public void setDenyTypes(String denyTypes) {
    this.denyTypes = denyTypes;
  }

  public String getAllowType() {
    return allowType;
  }

  public void setAllowType(String allowType) {
    this.allowType = allowType;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public Long getLimitSize() {
    return limitSize;
  }

  public void setLimitSize(Long limitSize) {
    this.limitSize = limitSize;
  }

  public String getSaveResult() {
    return saveResult;
  }

  public Boolean getResult() {
    return result;
  }

  public void setSaveResult(String saveResult) {
    this.saveResult = saveResult;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getInsRegId() {
    return insRegId;
  }

  public void setInsRegId(Long insRegId) {
    this.insRegId = insRegId;
  }

  public Long getDataId() {
    return dataId;
  }

  public void setDataId(Long dataId) {
    this.dataId = dataId;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public String getSrcImage() {
    return srcImage;
  }

  public Long getFolderId() {
    return folderId;
  }

  public String getFolderIds() {
    return folderIds;
  }

  public void setFolderIds(String folderIds) {
    this.folderIds = folderIds;
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public Long getGroupId() {
    return groupId;
  }

  public int getNodeId() {
    return nodeId;
  }

  public int getCurrentNodeId() {
    return currentNodeId;
  }

  public String getCurrentPsnId() {
    return currentPsnId;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setSrcImage(String srcImage) {
    this.srcImage = srcImage;
  }

  public void setFolderId(Long folderId) {
    this.folderId = folderId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setNodeId(int nodeId) {
    this.nodeId = nodeId;
  }

  public void setCurrentNodeId(int currentNodeId) {
    this.currentNodeId = currentNodeId;
  }

  public void setCurrentPsnId(String currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getNavAction() {
    return navAction;
  }

  public void setNavAction(String navAction) {
    this.navAction = navAction;
  }

  public String getSelectMenuId() {
    return selectMenuId;
  }

  public void setSelectMenuId(String selectMenuId) {
    this.selectMenuId = selectMenuId;
  }

  public int getIsNeedSendGroupEmail() {
    return isNeedSendGroupEmail;
  }

  public String getLimitLength() {
    return limitLength;
  }

  public void setLimitLength(String limitLength) {
    this.limitLength = limitLength;
  }

  public void setIsNeedSendGroupEmail(int isNeedSendGroupEmail) {
    this.isNeedSendGroupEmail = isNeedSendGroupEmail;
  }

  public int getImgType() {
    return imgType;
  }

  public void setImgType(int imgType) {
    this.imgType = imgType;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public Integer getIsNeedSyncToMyFile() {
    return isNeedSyncToMyFile;
  }

  public void setIsNeedSyncToMyFile(Integer isNeedSyncToMyFile) {
    this.isNeedSyncToMyFile = isNeedSyncToMyFile;
  }

  public String getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(String psnIds) {
    this.psnIds = psnIds;
  }

  public String getGroupMemberIsAll() {
    return groupMemberIsAll;
  }

  public void setGroupMemberIsAll(String groupMemberIsAll) {
    this.groupMemberIsAll = groupMemberIsAll;
  }

  public ArchiveFile getArchiveFile() {
    return archiveFile;
  }

  public void setArchiveFile(ArchiveFile archiveFile) {
    this.archiveFile = archiveFile;
  }


}
