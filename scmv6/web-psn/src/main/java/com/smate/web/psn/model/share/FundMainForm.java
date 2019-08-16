package com.smate.web.psn.model.share;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

public class FundMainForm {

  private Long psnId; // 当前用户PsnId
  private String des3PsnId; // 当前用户加密的PsnId
  private Long fundId; // 基金id
  private String des3FundId;// 加密的基金id
  private String des3FundIds;// 加密的基金id //批量 用逗号分隔
  private Long receiverId; // 接收人id
  private String des3ReceiverId; // 加密的接收人id
  private String des3ReceiverIds; // 接收人 批量 用逗号分隔
  private Long shareBaseId; // 分享主表Id
  private String des3ShareBaseId; // 分享主表Id
  private Long msgRelationId; // 消息关联表id
  private Long fundShareRecordId; // 基金分享记录id
  private Boolean cancelShare = false; // 取消分享
  private String contentIds; // 消息内容id集合；分号隔离
  // private List<FundFileInfo> psnFundInfoList;
  // private List<PsnFundShareBaseInfo> PsnFundShareBaseInfos;
  // private Page<PsnFundInfo> page;
  /**
   * 基金类型编码 1=pdf;2=doc;3=xls;7=其他；
   */
  // private Integer fileTypeNum;
  private String searchKey;
  private String fundDesc;

  private String fundNames; // 基金名字 多个就用逗号分隔

  // public Long archiveFileId;
  // public String des3ArchiveFileId;
  // private String source;// 来源于 isPFBox=个人基金库

  private String textContent; // 内容
  private String resInfoJson; // 基金信息，json格式

  @JsonIgnore
  private Map<String, Object> resultMap;

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  // public Integer getFileTypeNum() {
  // return fileTypeNum;
  // }
  //
  // public void setFileTypeNum(Integer fileTypeNum) {
  // this.fileTypeNum = fileTypeNum;
  // }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
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

  public String getDes3ReceiverIds() {
    return des3ReceiverIds;
  }

  public void setDes3ReceiverIds(String des3ReceiverIds) {
    this.des3ReceiverIds = des3ReceiverIds;
  }

  public String getTextContent() {
    return textContent;
  }

  public void setTextContent(String textContent) {
    this.textContent = textContent;
  }

  public String getDes3ShareBaseId() {
    return des3ShareBaseId;
  }

  public void setDes3ShareBaseId(String des3ShareBaseId) {
    this.des3ShareBaseId = des3ShareBaseId;
  }

  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public String getDes3FundId() {
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }

  public String getDes3FundIds() {
    return des3FundIds;
  }

  public void setDes3FundIds(String des3FundIds) {
    this.des3FundIds = des3FundIds;
  }

  public Long getFundShareRecordId() {
    return fundShareRecordId;
  }

  public void setFundShareRecordId(Long fundShareRecordId) {
    this.fundShareRecordId = fundShareRecordId;
  }

  public String getFundDesc() {
    return fundDesc;
  }

  public void setFundDesc(String fundDesc) {
    this.fundDesc = fundDesc;
  }

  public String getFundNames() {
    return fundNames;
  }

  public void setFundNames(String fundNames) {
    this.fundNames = fundNames;
  }

  public String getResInfoJson() {
    return resInfoJson;
  }

  public void setResInfoJson(String resInfoJson) {
    this.resInfoJson = resInfoJson;
  }

}
