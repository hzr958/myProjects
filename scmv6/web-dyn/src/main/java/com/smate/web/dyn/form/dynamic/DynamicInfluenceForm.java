package com.smate.web.dyn.form.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 动态影响力Form
 * 
 * @author yhx
 *
 */
public class DynamicInfluenceForm {

  private Long psnId; // 人员id
  private String des3psnId; // 人员id
  private String platform; // 平台，pc或移动端，移动端(mobile)
  private Long pubConfirmCount;// 待认领成果数
  private List<DynPubInfoForm> confirmList = new ArrayList<DynPubInfoForm>();
  private List<DynPubInfoForm> uploadList = new ArrayList<DynPubInfoForm>();
  private List<DynPsnInfoForm> psnInfoList = new ArrayList<DynPsnInfoForm>();
  private List<DynGroupInfoFrom> grpInfoList = new ArrayList<DynGroupInfoFrom>();
  private Page page = new Page();
  private DynPubInfoForm confirmInfo = new DynPubInfoForm();
  private DynPubInfoForm uploadInfo = new DynPubInfoForm();
  private DynPsnInfoForm dynPsnInfo = new DynPsnInfoForm();
  private DynGroupInfoFrom dynGrpInfo = new DynGroupInfoFrom();

  public Long getPsnId() {
    if (psnId == null) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDes3psnId() {
    if (StringUtils.isBlank(des3psnId) && this.psnId != null && this.psnId != 0L) {
      des3psnId = Des3Utils.encodeToDes3(psnId.toString());
    }
    return des3psnId;
  }

  public void setDes3psnId(String des3psnId) {
    this.des3psnId = des3psnId;
  }

  public Long getPubConfirmCount() {
    return pubConfirmCount;
  }

  public void setPubConfirmCount(Long pubConfirmCount) {
    this.pubConfirmCount = pubConfirmCount;
  }

  public List<DynPubInfoForm> getConfirmList() {
    return confirmList;
  }

  public void setConfirmList(List<DynPubInfoForm> confirmList) {
    this.confirmList = confirmList;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<DynPubInfoForm> getUploadList() {
    return uploadList;
  }

  public void setUploadList(List<DynPubInfoForm> uploadList) {
    this.uploadList = uploadList;
  }

  public List<DynPsnInfoForm> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<DynPsnInfoForm> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public DynPsnInfoForm getDynPsnInfo() {
    return dynPsnInfo;
  }

  public void setDynPsnInfo(DynPsnInfoForm dynPsnInfo) {
    this.dynPsnInfo = dynPsnInfo;
  }

  public List<DynGroupInfoFrom> getGrpInfoList() {
    return grpInfoList;
  }

  public void setGrpInfoList(List<DynGroupInfoFrom> grpInfoList) {
    this.grpInfoList = grpInfoList;
  }

  public DynGroupInfoFrom getDynGrpInfo() {
    return dynGrpInfo;
  }

  public void setDynGrpInfo(DynGroupInfoFrom dynGrpInfo) {
    this.dynGrpInfo = dynGrpInfo;
  }

  public DynPubInfoForm getConfirmInfo() {
    return confirmInfo;
  }

  public void setConfirmInfo(DynPubInfoForm confirmInfo) {
    this.confirmInfo = confirmInfo;
  }

  public DynPubInfoForm getUploadInfo() {
    return uploadInfo;
  }

  public void setUploadInfo(DynPubInfoForm uploadInfo) {
    this.uploadInfo = uploadInfo;
  }

}
