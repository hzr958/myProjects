package com.smate.web.v8pub.vo.searchimport;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.vo.PendingImportPubVO;
import com.smate.web.v8pub.vo.psn.PsnInfo;

/**
 * 成果检索导入VO
 * 
 * @author wsn
 * @date 2018年8月9日
 */
public class PubImportVO {

  private Long snsPubId; // sns库的成果id
  private List<ConstRefDbVO> constRefDBList; // 文献库列表
  private String dbType; // 文献库类型，1：成果，2：文献，4：项目
  private String jsonDBInfo; // json格式文献库信息
  private String isInitRadio; // 本人、好友、自由检索，可查看PubSearchImportConstants中常量
  private Long psnId; // 人员ID
  private String zhName; // 人员中文名
  private String firstName; // 英文名
  private String lastName; // 英文姓
  private String xmlId; // 成果xmlId,导入成果时用到
  private String inputXml; // 导入成果时传过来的xml字符串
  @SuppressWarnings("rawtypes")
  private List<PendingImportPubVO> pendingImportPubs; // 待导入的成果xml的List
  private String pubImportType; // 成果导入类型，search：检索导入，file：文件导入
  private String des3GroupId; // 加密的群组ID
  private Long grpId; // 群组ID
  private List<WorkHistory> workList; // 人员所有单位信息
  private WorkHistory primaryWork; // 首要单位信息
  private JSONArray pubJsonList; // 成果jsonList
  private String savePubJsonParams; // 待导入成果保存操作的参数

  private String dbCode;// 数据来源
  private String des3PsnId; // 加密的人员ID
  private String pubJsonParams; // 成果json格式参数字符串，转成JSONArray赋值给pubJsonList
  private Integer importSuccessSize = 0; // 成功导入的成果数量
  private String cacheKey; // 待导入成果的缓存的key
  private Set<PsnInfo> recommendPsn; // 有相同成果的人员（推荐个用户加为好友）
  private String snsDomain; // 科研之友域名
  private String systemType; // 发送请求的操作系统类型
  private String returnInt;
  private Integer artType;
  private Integer articleType = 1; // 1== 导入成果
  private Integer savePubType; // 成果类型，0==群组文献， 1==群组项目成果 ，2==群组项目文献
  private Long currentPsnId; // 当前人
  private Long ownerPsnId; // 成果拥有者的psnId
  private Integer pubGener = 1; // 成果检查的类型， 1=个人库查重； 2=群组成果查重 ； 3=基准库成果查重
  private String orgName; // 单位名称
  private int currentYear; // 当前年份
  private String searchType; // 检索类型，默认是检索成果（空或pub）,项目（prj）,安装插件后判断跳转页面用

  private boolean validPub = true; // 标示所导入的成果是否都有效，存在一个有效，值就为true，只有当所有都无效时才为false

  private Integer updateMark;// 更新标记：1.在线导入，且未修改；2.在线导入，并已修改；3.手工录入
  private PubSnsRecordFromEnum recordFrom;



  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
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

  public String getJsonDBInfo() {
    return jsonDBInfo;
  }

  public void setJsonDBInfo(String jsonDBInfo) {
    this.jsonDBInfo = jsonDBInfo;
  }

  public String getIsInitRadio() {
    return isInitRadio;
  }

  public void setIsInitRadio(String isInitRadio) {
    this.isInitRadio = isInitRadio;
  }

  public Long getPsnId() {
    if (StringUtils.isNotBlank(this.des3PsnId)) {
      this.psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3PsnId));
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

  public String getXmlId() {
    return xmlId;
  }

  public void setXmlId(String xmlId) {
    this.xmlId = xmlId;
  }

  public String getInputXml() {
    return inputXml;
  }

  public void setInputXml(String inputXml) {
    this.inputXml = inputXml;
  }

  public List<PendingImportPubVO> getPendingImportPubs() {
    return pendingImportPubs;
  }

  public void setPendingImportPubs(List<PendingImportPubVO> pendingImportPubs) {
    this.pendingImportPubs = pendingImportPubs;
  }

  public String getPubImportType() {
    return pubImportType;
  }

  public void setPubImportType(String pubImportType) {
    this.pubImportType = pubImportType;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public Long getGrpId() {
    if (StringUtils.isNotBlank(this.des3GroupId)) {
      this.grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3GroupId));
    }
    return grpId;
  }

  public void setGrpId(Long groupId) {
    this.grpId = groupId;
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

  public JSONArray getPubJsonList() {
    return pubJsonList;
  }

  public void setPubJsonList(JSONArray pubJsonList) {
    this.pubJsonList = pubJsonList;
  }

  public String getSavePubJsonParams() {
    return savePubJsonParams;
  }

  public void setSavePubJsonParams(String savePubJsonParams) {
    this.savePubJsonParams = savePubJsonParams;
  }

  public String getDes3PsnId() {
    if (this.psnId != null) {
      this.des3PsnId = Des3Utils.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getPubJsonParams() {
    return pubJsonParams;
  }

  public void setPubJsonParams(String pubJsonParams) {
    this.pubJsonParams = pubJsonParams;
  }

  public Integer getImportSuccessSize() {
    return importSuccessSize;
  }

  public void setImportSuccessSize(Integer importSuccessSize) {
    this.importSuccessSize = importSuccessSize;
  }

  public String getCacheKey() {
    return cacheKey;
  }

  public void setCacheKey(String cacheKey) {
    this.cacheKey = cacheKey;
  }

  public Set<PsnInfo> getRecommendPsn() {
    return recommendPsn;
  }

  public void setRecommendPsn(Set<PsnInfo> recommendPsn) {
    this.recommendPsn = recommendPsn;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }

  public String getSystemType() {
    return systemType;
  }

  public void setSystemType(String systemType) {
    this.systemType = systemType;
  }

  public String getReturnInt() {
    return returnInt;
  }

  public void setReturnInt(String returnInt) {
    this.returnInt = returnInt;
  }

  public Integer getArtType() {
    return artType;
  }

  public void setArtType(Integer artType) {
    this.artType = artType;
  }

  public Integer getSavePubType() {
    return savePubType;
  }

  public void setSavePubType(Integer savePubType) {
    this.savePubType = savePubType;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public Long getCurrentPsnId() {
    if (currentPsnId == null || currentPsnId == 0L) {
      currentPsnId = SecurityUtils.getCurrentUserId();
    }
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public Integer getPubGener() {
    return pubGener;
  }

  public void setPubGener(Integer pubGener) {
    this.pubGener = pubGener;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public int getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(int currentYear) {
    this.currentYear = currentYear;
  }

  public String getSearchType() {
    return searchType;
  }

  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  public boolean isValidPub() {
    return validPub;
  }

  public void setValidPub(boolean validPub) {
    this.validPub = validPub;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

  public PubSnsRecordFromEnum getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(PubSnsRecordFromEnum recordFrom) {
    this.recordFrom = recordFrom;
  }

}
