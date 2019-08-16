package com.smate.web.prj.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 项目操作Action页面参数模型类
 * 
 * @author houchuanjie
 * @date 2018年3月16日 下午3:16:53
 */
public class ProjectOptForm {
  // 跳转url
  private String forwardUrl;
  // 跳转的参数
  private String[] params;
  // 保存结果成功与否
  private boolean saveSuccess;
  // 编辑页面返回按钮返回类型
  private Integer backType;
  // 项目id
  private Long id;
  // 项目加密id
  private String des3Id;
  // 群组id
  private Long grpId;
  // 群组加密id
  private String des3GrpId;
  // 人员id
  private Long psnId;
  private String des3PsnId;
  // 项目所有人id
  private Long ownerPsnId;
  // 项目隐私设置
  private Integer authority;
  // 项目xml
  private String prjXml;
  // 显示在页面上的学科关键词名字
  private String disName;
  // 是否包含html标签
  private boolean isHtmlZhAbstract;
  private boolean isHtmlEnAbstract;
  // 项目资助机构类别名称模糊查询字段
  private String q;
  private String searchKey;
  // 限制模糊查询的结果数量
  private Integer limit;
  // 项目资助机构ID
  private Long agencyId;
  // 语言 zh_CN or en_US
  private String lang;
  // 排除参数
  private String excludeParam;
  private Integer resType; // 资源类型
  private Integer resNode = 1; // 节点
  private Integer recordFrom;
  private List<ConstDictionary> prjMoneyType;
  private String downloadUrl;
  private Integer discipline;
  private Map<String, List<Map<String, String>>> disciplineMap = new HashMap<>(); // 学科领域
  private Map<String, String> prjAttachDownload = new HashMap<>(); // 附件下载
  private String optType; // 操作类型， 赞（1），取消赞（0）

  public Map<String, String> getPrjAttachDownload() {
    return prjAttachDownload;
  }

  public void setPrjAttachDownload(Map<String, String> prjAttachDownload) {
    this.prjAttachDownload = prjAttachDownload;
  }

  public Integer getDiscipline() {
    return discipline;
  }

  public void setDiscipline(Integer discipline) {
    this.discipline = discipline;
  }

  public Map<String, List<Map<String, String>>> getDisciplineMap() {
    return disciplineMap;
  }

  public void setDisciplineMap(Map<String, List<Map<String, String>>> disciplineMap) {
    this.disciplineMap = disciplineMap;
  }

  public boolean getIsHtmlZhAbstract() {
    return isHtmlZhAbstract;
  }

  public void setIsHtmlZhAbstract(boolean isHtmlZhAbstract) {
    this.isHtmlZhAbstract = isHtmlZhAbstract;
  }

  public boolean getIsHtmlEnAbstract() {
    return isHtmlEnAbstract;
  }

  public void setIsHtmlEnAbstract(boolean isHtmlEnAbstract) {
    this.isHtmlEnAbstract = isHtmlEnAbstract;
  }

  /**
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id 要设置的 id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return des3Id
   */
  public String getDes3Id() {
    return des3Id;
  }

  /**
   * @param des3Id 要设置的 des3Id
   */
  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Long getPrjId() {
    return Optional.ofNullable(id).orElse(
        Optional.ofNullable(des3Id).map(des3Id -> NumberUtils.toLong(Des3Utils.decodeFromDes3(des3Id))).orElse(0L));
  }

  /**
   * @return prjXml
   */
  public String getPrjXml() {
    return prjXml;
  }

  /**
   * @param prjXml 要设置的 prjXml
   */
  public void setPrjXml(String prjXml) {
    this.prjXml = prjXml;
  }

  /**
   * @return ownerPsnId
   */
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  /**
   * @param ownerPsnId 要设置的 ownerPsnId
   */
  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  /**
   * @return authority
   */
  public Integer getAuthority() {
    return authority;
  }

  /**
   * @param authority 要设置的 authority
   */
  public void setAuthority(Integer authority) {
    this.authority = authority;
  }

  /**
   * @return disName
   */
  public String getDisName() {
    return disName;
  }

  /**
   * @param disName 要设置的 disName
   */
  public void setDisName(String disName) {
    this.disName = disName;
  }

  /**
   * @return grpId
   */
  public Long getGrpId() {
    return Optional.ofNullable(grpId).orElseGet(() -> {
      return Optional.ofNullable(des3GrpId).map(des3Id -> NumberUtils.toLong(Des3Utils.decodeFromDes3(des3Id)))
          .orElse(0L);
    });
  }

  /**
   * @param grpId 要设置的 grpId
   */
  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  /**
   * @return des3GrpId
   */
  public String getDes3GrpId() {
    return des3GrpId;
  }

  /**
   * @param des3GrpId 要设置的 des3GrpId
   */
  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  /**
   * @return prjMoneyType
   */
  public List<ConstDictionary> getPrjMoneyType() {
    return prjMoneyType;
  }

  /**
   * @param prjMoneyType 要设置的 prjMoneyType
   */
  public void setPrjMoneyType(List<ConstDictionary> prjMoneyType) {
    this.prjMoneyType = prjMoneyType;
  }

  /**
   * @return limit
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * @param limit 要设置的 limit
   */
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  /**
   * @return q
   */
  public String getQ() {
    if (StringUtils.isBlank(q)) {
      q = searchKey;
    }
    return q;
  }

  /**
   * @param q 要设置的 q
   */
  public void setQ(String q) {
    this.q = q;
  }

  /**
   * @return agencyId
   */
  public Long getAgencyId() {
    return agencyId;
  }

  /**
   * @param agencyId 要设置的 agencyId
   */
  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  /**
   * @return lang
   */
  public String getLang() {
    return lang;
  }

  /**
   * @param lang 要设置的 lang
   */
  public void setLang(String lang) {
    this.lang = lang;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public Integer getResNode() {
    return resNode;
  }

  public void setResNode(Integer resNode) {
    this.resNode = resNode;
  }

  /**
   * @return params
   */
  public String[] getParams() {
    return params;
  }

  /**
   * @param params 要设置的 params
   */
  public void setParams(String... params) {
    this.params = params;
  }

  /**
   * @return saveSuccess
   */
  public boolean isSaveSuccess() {
    return saveSuccess;
  }

  /**
   * @param saveSuccess 要设置的 saveSuccess
   */
  public void setSaveSuccess(boolean saveSuccess) {
    this.saveSuccess = saveSuccess;
  }

  /**
   * @return forwardUrl
   */
  public String getForwardUrl() {
    return forwardUrl;
  }

  /**
   * @param forwardUrl 要设置的 forwardUrl
   */
  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  /**
   * @return backType
   */
  public Integer getBackType() {
    return backType;
  }

  /**
   * @param backType 要设置的 backType
   */
  public void setBackType(Integer backType) {
    this.backType = backType;
  }

  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  /**
   * @return excludeParam
   */
  public String getExcludeParam() {
    return excludeParam;
  }

  /**
   * @param excludeParam 要设置的 excludeParam
   */
  public void setExcludeParam(String excludeParam) {
    this.excludeParam = excludeParam;
  }

  /**
   * @return psnId
   */
  public Long getPsnId() {
    return Optional.ofNullable(psnId).orElseGet(() -> Optional.ofNullable(des3PsnId)
        .map(des3id -> NumberUtils.toLong(Des3Utils.decodeFromDes3(des3id))).orElse(SecurityUtils.getCurrentUserId()));
  }

  /**
   * @param psnId 要设置的 psnId
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return des3PsnId
   */
  public String getDes3PsnId() {
    return des3PsnId;
  }

  /**
   * @param des3PsnId 要设置的 des3PsnId
   */
  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getOptType() {
    return optType;
  }

  public void setOptType(String optType) {
    this.optType = optType;
  }


}
