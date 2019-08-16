package com.smate.web.psn.model.file;

import java.io.Serializable;
import java.util.List;

import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 分享记录显示INFO类
 * 
 * @author zzx
 *
 */
public class PsnFileShareBaseInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long senderId;
  private String senderAvatars;
  private String senderZhName;
  private String senderEnName;
  private String senderShortUrl;
  private PsnFileShareBase psnFileShareBase;
  private List<PsnFileShareRecordInfo> psnFileShareRecordInfoList;
  private List<PsnFileInfo> psnFileInfoList;
  private List<PsnInfo> psnInfoList;

  public List<PsnFileShareRecordInfo> getPsnFileShareRecordInfoList() {
    return psnFileShareRecordInfoList;
  }

  public void setPsnFileShareRecordInfoList(List<PsnFileShareRecordInfo> psnFileShareRecordInfoList) {
    this.psnFileShareRecordInfoList = psnFileShareRecordInfoList;
  }

  public PsnFileShareBase getPsnFileShareBase() {
    return psnFileShareBase;
  }

  public void setPsnFileShareBase(PsnFileShareBase psnFileShareBase) {
    this.psnFileShareBase = psnFileShareBase;
  }

  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public String getSenderAvatars() {
    return senderAvatars;
  }

  public void setSenderAvatars(String senderAvatars) {
    this.senderAvatars = senderAvatars;
  }

  public String getSenderZhName() {
    return senderZhName;
  }

  public void setSenderZhName(String senderZhName) {
    this.senderZhName = senderZhName;
  }

  public String getSenderEnName() {
    return senderEnName;
  }

  public void setSenderEnName(String senderEnName) {
    this.senderEnName = senderEnName;
  }

  public String getSenderShortUrl() {
    return senderShortUrl;
  }

  public void setSenderShortUrl(String senderShortUrl) {
    this.senderShortUrl = senderShortUrl;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public List<PsnFileInfo> getPsnFileInfoList() {
    return psnFileInfoList;
  }

  public void setPsnFileInfoList(List<PsnFileInfo> psnFileInfoList) {
    this.psnFileInfoList = psnFileInfoList;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }


}
