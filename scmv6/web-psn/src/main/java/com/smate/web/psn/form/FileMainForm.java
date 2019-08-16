package com.smate.web.psn.form;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.file.PsnFileShareBaseInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;

public class FileMainForm {
  private Long grpId;
  private String des3GrpId;
  private Long psnId; // 当前用户PsnId
  private String des3PsnId; // 当前用户加密的PsnId
  private Long fileId; // 文件id
  private String fileName;// 文件名字
  private String fileType;// 文件类型
  private String fileViewType;// 列表显示的文件类型
  private Date shareDate;// 分享日期
  private int isImported;// 是否已导入
  /** 文件状态. */
  private int fileStatus;
  private String des3ArchId;// 附件加密ID
  private Long fileSize;// 文件大小
  private String des3FileId;// 加密的文件id
  private String des3FileIds;// 加密的文件id //批量 用逗号分隔
  private Long receiverId; // 接收人id
  private String des3ReceiverId; // 加密的接收人id
  private String des3ReceiverIds; // 接收人 批量 用逗号分隔
  private String receiverEmails; // 接收人邮件 批量 用逗号分隔
  private Long shareBaseId; // 分享主表Id
  private String des3ShareBaseId; // 分享主表Id
  private Long msgRelationId; // 消息关联表id
  private Long fileShareRecordId; // 文件分享记录id
  private Boolean cancelShare = false; // 取消分享
  private String contentIds; // 消息内容id集合；分号隔离
  private List<PsnFileInfo> psnFileInfoList;
  private List<PsnFileShareBaseInfo> PsnFileShareBaseInfos;
  private Page<PsnFileInfo> page;
  private String url;// 登录的url
  private List<Long> grpPsnFileIdList;// 个人文件库已经在群组中存在的id
  /** -1分享已取消，-2已到有效期，0未登录，1已认领且当前登录人为确认人，2已认领但是当前登录人不是认领人. */
  private int status;
  /**
   * 邮件分享文件的必须参数
   */
  private String from;
  private String A;
  private String B;
  private String C;
  private int resNodeId;
  /**
   * 文件类型编码 1=pdf;2=doc;3=xls;7=其他；
   */
  private Integer fileTypeNum;
  private String searchKey;
  private String fileDesc;
  private String fileNames; // 文件名字 多个就用逗号分隔
  public Long archiveFileId;
  public String des3ArchiveFileId;
  private String source;// 来源于 isPFBox=个人文件库
  private String textContent; // 内容
  private Integer batchCount = 0;// 批量处理的数量
  @JsonIgnore
  private Map<String, Object> resultMap = new HashMap<String, Object>();

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public Integer getFileTypeNum() {
    return fileTypeNum;
  }

  public void setFileTypeNum(Integer fileTypeNum) {
    this.fileTypeNum = fileTypeNum;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }
  List<String>  updateContentPsnIds = new ArrayList<>();

  public List<String> getUpdateContentPsnIds() {
    return updateContentPsnIds;
  }

  public void setUpdateContentPsnIds(List<String> updateContentPsnIds) {
    this.updateContentPsnIds = updateContentPsnIds;
  }

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    }
    if (psnId == null) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getFileId() {
    if (this.fileId == null && StringUtils.isNotBlank(this.des3FileId)) {
      this.fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3FileId));
    }
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public Long getFileShareRecordId() {
    return fileShareRecordId;
  }

  public void setFileShareRecordId(Long fileShareRecordId) {
    this.fileShareRecordId = fileShareRecordId;
  }

  public Boolean getCancelShare() {
    return cancelShare;
  }

  public void setCancelShare(Boolean cancelShare) {
    this.cancelShare = cancelShare;
  }

  public String getDes3ReceiverId() {
    return des3ReceiverId;
  }

  public void setDes3ReceiverId(String des3ReceiverId) {
    this.des3ReceiverId = des3ReceiverId;
  }

  public String getContentIds() {
    return contentIds;
  }

  public void setContentIds(String contentIds) {
    this.contentIds = contentIds;
  }

  public Long getReceiverId() {
    if (receiverId == null && StringUtils.isNotBlank(this.des3ReceiverId)) {
      receiverId = Long.parseLong(Des3Utils.decodeFromDes3(this.des3ReceiverId));
    }
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public Long getShareBaseId() {
    if (this.shareBaseId == null && StringUtils.isNotBlank(this.des3ShareBaseId)) {
      this.shareBaseId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3ShareBaseId));
    }
    return shareBaseId;
  }

  public void setShareBaseId(Long shareBaseId) {
    this.shareBaseId = shareBaseId;
  }

  public Long getMsgRelationId() {
    return msgRelationId;
  }

  public void setMsgRelationId(Long msgRelationId) {
    this.msgRelationId = msgRelationId;
  }

  public Long getArchiveFileId() {
    if (archiveFileId == null && StringUtils.isNotBlank(des3ArchiveFileId)) {
      archiveFileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ArchiveFileId));
    }
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public List<PsnFileShareBaseInfo> getPsnFileShareBaseInfos() {
    return PsnFileShareBaseInfos;
  }

  public void setPsnFileShareBaseInfos(List<PsnFileShareBaseInfo> psnFileShareBaseInfos) {
    PsnFileShareBaseInfos = psnFileShareBaseInfos;
  }

  public List<PsnFileInfo> getPsnFileInfoList() {
    return psnFileInfoList;
  }

  public void setPsnFileInfoList(List<PsnFileInfo> psnFileInfoList) {
    this.psnFileInfoList = psnFileInfoList;
  }

  public Page<PsnFileInfo> getPage() {
    return page;
  }

  public void setPage(Page<PsnFileInfo> page) {
    this.page = page;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDes3FileIds() {
    return des3FileIds;
  }

  public void setDes3FileIds(String des3FileIds) {
    this.des3FileIds = des3FileIds;
  }

  public String getDes3ReceiverIds() {
    return des3ReceiverIds;
  }

  public void setDes3ReceiverIds(String des3ReceiverIds) {
    this.des3ReceiverIds = des3ReceiverIds;
  }

  public String getFileNames() {
    return fileNames;
  }

  public void setFileNames(String fileNames) {
    this.fileNames = fileNames;
  }

  public String getTextContent() {
    return textContent;
  }

  public void setTextContent(String textContent) {
    this.textContent = textContent;
  }

  public String getDes3ArchiveFileId() {
    return des3ArchiveFileId;
  }

  public void setDes3ArchiveFileId(String des3ArchiveFileId) {
    this.des3ArchiveFileId = des3ArchiveFileId;
  }

  public String getDes3ShareBaseId() {
    return des3ShareBaseId;
  }

  public void setDes3ShareBaseId(String des3ShareBaseId) {
    this.des3ShareBaseId = des3ShareBaseId;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getResNodeId() {
    return resNodeId;
  }

  public void setResNodeId(int resNodeId) {
    this.resNodeId = resNodeId;
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

  public String getDes3ArchId() {
    return des3ArchId;
  }

  public void setDes3ArchId(String des3ArchId) {
    this.des3ArchId = des3ArchId;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getGrpId() {
    if (grpId == null && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId), 0L);
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public String getReceiverEmails() {
    return receiverEmails;
  }

  public void setReceiverEmails(String receiverEmails) {
    this.receiverEmails = receiverEmails;
  }

  public List<Long> getGrpPsnFileIdList() {
    return grpPsnFileIdList;
  }

  public void setGrpPsnFileIdList(List<Long> grpPsnFileIdList) {
    this.grpPsnFileIdList = grpPsnFileIdList;
  }

  public Integer getBatchCount() {
    return batchCount;
  }

  public void setBatchCount(Integer batchCount) {
    this.batchCount = batchCount;
  }
}
