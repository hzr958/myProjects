package com.smate.sie.web.application.form.validate;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 科研验证表单参数
 * 
 * @author sjzhou
 *
 */
public class KpiValidateForm implements Serializable {
  private static final long serialVersionUID = -7780123561314043756L;

  private String title;
  private String type;
  private String typeId;
  private String prpYear;
  private Long userId;
  private List<Map<String, String>> typeList;// 验证类型
  private List<Map<String, Object>> yearList;// 申请年份
  private Long totalNum;
  private String searchKey;
  private File filedata; // 导入的文件流
  private String filedataFileName;
  private String des3Uuid; // 导入的文件流
  private List<Map<String, Object>> psnList;// 人员验证
  private List<Map<String, Object>> insList;// 单位验证
  private List<Map<String, Object>> prjPubList;// 项目成果
  private Integer psnPass = 0;
  private Integer psnCount = 0;
  private Integer insPass = 0;
  private Integer insCount = 0;
  private Integer prjPubPass = 0;
  private Integer prjPubCount = 0;
  private Integer isPaymentIns; // 0未付款 1已付款 弃用该属性 2019-2-28
  private String adminPsnName; // 单位管理员名称
  private String adminEmail; // 单位管理员邮箱
  private Integer status;
  private Integer ifOutside; // 是否站外请求1是站外; 0 不是;
  private String clientIP;
  private boolean flag; // 是申请书类型
  private Integer totalDetailNum; // 查看页面总记录数
  private String des3Id; // 上传表主键
  private Integer isPay;// 0未付款 1已付款
  private String des3IsPay;
  @JsonIgnore
  private Map<String, Object> resultMap;

  public String getTitle() {
    return title;
  }

  public Long getUserId() {
    return userId;
  }

  public List<Map<String, String>> getTypeList() {
    return typeList;
  }

  public List<Map<String, Object>> getYearList() {
    return yearList;
  }

  public Long getTotalNum() {
    return totalNum;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setTypeList(List<Map<String, String>> typeList) {
    this.typeList = typeList;
  }

  public void setYearList(List<Map<String, Object>> yearList) {
    this.yearList = yearList;
  }

  public void setTotalNum(Long totalNum) {
    this.totalNum = totalNum;
  }

  public String getType() {
    return type;
  }

  public String getPrpYear() {
    return prpYear;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setPrpYear(String prpYear) {
    this.prpYear = prpYear;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public File getFiledata() {
    return filedata;
  }

  public void setFileData(File filedata) {
    this.filedata = filedata;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public String getDes3Uuid() {
    return des3Uuid;
  }

  public void setDes3Uuid(String des3Uuid) {
    this.des3Uuid = des3Uuid;
  }

  public List<Map<String, Object>> getPsnList() {
    return psnList;
  }

  public List<Map<String, Object>> getInsList() {
    return insList;
  }

  public void setPsnList(List<Map<String, Object>> psnList) {
    this.psnList = psnList;
  }

  public void setInsList(List<Map<String, Object>> insList) {
    this.insList = insList;
  }

  public List<Map<String, Object>> getPrjPubList() {
    return prjPubList;
  }

  public void setPrjPubList(List<Map<String, Object>> prjPubList) {
    this.prjPubList = prjPubList;
  }

  public Integer getPsnPass() {
    return psnPass;
  }

  public Integer getPsnCount() {
    return psnCount;
  }

  public Integer getInsPass() {
    return insPass;
  }

  public Integer getInsCount() {
    return insCount;
  }

  public Integer getPrjPubPass() {
    return prjPubPass;
  }

  public Integer getPrjPubCount() {
    return prjPubCount;
  }

  public void setPsnPass(Integer psnPass) {
    this.psnPass = psnPass;
  }

  public void setPsnCount(Integer psnCount) {
    this.psnCount = psnCount;
  }

  public void setInsPass(Integer insPass) {
    this.insPass = insPass;
  }

  public void setInsCount(Integer insCount) {
    this.insCount = insCount;
  }

  public void setPrjPubPass(Integer prjPubPass) {
    this.prjPubPass = prjPubPass;
  }

  public void setPrjPubCount(Integer prjPubCount) {
    this.prjPubCount = prjPubCount;
  }

  public Integer getIsPaymentIns() {
    return isPaymentIns;
  }

  public void setIsPaymentIns(Integer isPaymentIns) {
    this.isPaymentIns = isPaymentIns;
  }

  public String getAdminPsnName() {
    return adminPsnName;
  }

  public String getAdminEmail() {
    return adminEmail;
  }

  public void setAdminPsnName(String adminPsnName) {
    this.adminPsnName = adminPsnName;
  }

  public void setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
  }

  public String getFiledataFileName() {
    return filedataFileName;
  }

  public void setFiledata(File filedata) {
    this.filedata = filedata;
  }

  public void setFiledataFileName(String filedataFileName) {
    this.filedataFileName = filedataFileName;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getIfOutside() {
    return ifOutside;
  }

  public void setIfOutside(Integer ifOutside) {
    this.ifOutside = ifOutside;
  }

  public String getClientIP() {
    return clientIP;
  }

  public void setClientIP(String clientIP) {
    this.clientIP = clientIP;
  }

  public boolean isFlag() {
    return flag;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  public Integer getTotalDetailNum() {
    return totalDetailNum;
  }

  public void setTotalDetailNum(Integer totalDetailNum) {
    this.totalDetailNum = totalDetailNum;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getDes3IsPay() {
    return des3IsPay;
  }

  public void setDes3IsPay(String des3IsPay) {
    this.des3IsPay = des3IsPay;
  }

  public Integer getIsPay() {
    return isPay;
  }

  public void setIsPay(Integer isPay) {
    this.isPay = isPay;
  }


}
