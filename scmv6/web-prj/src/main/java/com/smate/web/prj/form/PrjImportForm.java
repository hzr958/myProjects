package com.smate.web.prj.form;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.vo.ConstRefDbVO;

import java.io.Serializable;
import java.util.List;


/**
 * 联邦检索导入项目Form.
 * 
 * @author wsn
 * @date Dec 14, 2018
 */
public class PrjImportForm implements Serializable {
  private List<ConstRefDbVO> constRefDBList; // 文献库列表
  private String dbType; // 文献库类型，1：成果，2：文献，4：项目
  private Long psnId; // 人员ID
  private String zhName; // 人员中文名
  private String firstName; // 英文名
  private String lastName; // 英文姓
  private List<WorkHistory> workList; // 人员所有单位信息
  private WorkHistory primaryWork; // 首要单位信息
  private String isInitRadio; // 本人、好友、自由检索
  private String jsonDBInfo; // json格式文献库信息
  private String inputXml; // 接收的前端传入的待导入项目的xml
  private List<ProjectInfo> prjInfoList; // 项目信息
  private  List<PrjInfoDTO>   filePrjInfos; //  文件导入的项目信息
  private String cacheKey; // 缓存待导入的项目的key
  private JSONArray prjJsonList; // 待导入成果的操作信息
  private Integer importSuccessNum; // 导入成功的项目数量
  private String prjJsonParams; // 待导入的成果操作信息
  private String errorMsg; // 错误信息
  private String snsDomain; // 科研之友域名
  private Integer count = 0;
  private String prjData = "" ; //项目数据


  public String getPrjData() {
    return prjData;
  }

  public void setPrjData(String prjData) {
    this.prjData = prjData;
  }

  public List<PrjInfoDTO> getFilePrjInfos() {
    return filePrjInfos;
  }

  public void setFilePrjInfos(List<PrjInfoDTO> filePrjInfos) {
    this.filePrjInfos = filePrjInfos;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public PrjImportForm() {
    super();
  }

  public List<ConstRefDbVO> getConstRefDBList() {
    return constRefDBList;
  }

  public void setConstRefDBList(List<ConstRefDbVO> constRefDBList) {
    this.constRefDBList = constRefDBList;
  }

  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  public Long getPsnId() {
    if(NumberUtils.isNullOrZero(psnId)){
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<WorkHistory> getWorkList() {
    return workList;
  }

  public void setWorkList(List<WorkHistory> workList) {
    this.workList = workList;
  }

  public WorkHistory getPrimaryWork() {
    return primaryWork;
  }

  public void setPrimaryWork(WorkHistory primaryWork) {
    this.primaryWork = primaryWork;
  }

  public String getIsInitRadio() {
    return isInitRadio;
  }

  public void setIsInitRadio(String isInitRadio) {
    this.isInitRadio = isInitRadio;
  }

  public String getJsonDBInfo() {
    return jsonDBInfo;
  }

  public void setJsonDBInfo(String jsonDBInfo) {
    this.jsonDBInfo = jsonDBInfo;
  }

  public String getInputXml() {
    return inputXml;
  }

  public void setInputXml(String inputXml) {
    this.inputXml = inputXml;
  }

  public List<ProjectInfo> getPrjInfoList() {
    return prjInfoList;
  }

  public void setPrjInfoList(List<ProjectInfo> prjInfoList) {
    this.prjInfoList = prjInfoList;
  }

  public String getCacheKey() {
    return cacheKey;
  }

  public void setCacheKey(String cacheKey) {
    this.cacheKey = cacheKey;
  }

  public JSONArray getPrjJsonList() {
    return prjJsonList;
  }

  public void setPrjJsonList(JSONArray prjJsonList) {
    this.prjJsonList = prjJsonList;
  }

  public Integer getImportSuccessNum() {
    return importSuccessNum;
  }

  public void setImportSuccessNum(Integer importSuccessNum) {
    this.importSuccessNum = importSuccessNum;
  }

  public String getPrjJsonParams() {
    return prjJsonParams;
  }

  public void setPrjJsonParams(String prjJsonParams) {
    this.prjJsonParams = prjJsonParams;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }


}
