package com.smate.web.mobile.v8pub.vo.pdwh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 成果录入或编辑用
 * 
 * @author wsn
 * @date 2018年7月24日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingImportPubVO<T extends PubTypeInfoDTO> implements Serializable {

  private static final long serialVersionUID = -8809494905090896880L;

  private Long pubId; // 成果ID
  private String des3PubId; // 加密的成果Id
  private String jsonPub; // 成果json数据
  private Integer pubType; // 成果类型，参考const_pub_type表
  private String title; // 成果标题
  private String summary; // 摘要信息
  private String keywords; // 关键词
  private Long countryId; // 国家ID
  private String remarks; // 备注--------------现在新编辑录入页面没有该输入框了
  private String briefDesc;// 成果简短描述
  private String authorNames; // 作者名称
  private String doi; // 数字身份标志DOI
  private List<PubSituationDTO> situations = new ArrayList<PubSituationDTO>(); // 成果引用收录情况
  private PubFulltextDTO fullText = new PubFulltextDTO(); // 成果全文
  private String organization; // 单位组织
  private String fundInfo; // 基金信息
  private List<PubMemberDTO> members; // 成果作者
  private T pubTypeInfo; // 不同类型的成果特有的一些信息
  private String srcFulltextUrl; // 全文链接
  private String modifyDate; // 最近的修改时间
  private String typeName; // 成果类型名称
  private Integer authorMatch; // 待导入成果是否匹配上当前登录人员， 1：匹配上了，0：未匹配上
  // ------------------------从xml中获取的额外的信息,后面可以看下定义一个类来放这些-----------------begin
  private String authorsNamesSpec; // 人员姓名简写
  private String sourceDbCode; // xml中的sourDbCode，从哪个库检索出来的
  private String original; // 期刊名称
  private String pubYear; // 成果年份
  private String publishDate; // 发表时间
  private String tmpSourceUrl;
  private String sourceUrl;
  // ------------------------从xml中获取的额外的信息,后面可以看下定义一个类来放这些-----------------end

  private Integer citations; // 引用次数
  private Integer seqNo; // 系列号 从1开始
  // 记录来源 0, "手工录入" 1, "数据库导入" 2, "文件导入" 3, "基准库导入"
  private Integer recordFrom;
  private String pubHandlerName;// 保存来源saveSnsPubHandler updateSnsPubHandler
  private List<Accessory> accessorys = new ArrayList<>(); // 附件

  private String des3PsnId;
  private Integer pubGenre = 1; // 成果打类别 1：个人成果 2：群组成果 3：基准库导入成果
  private Integer permission; // 成果权限，4：隐私，7：公开
  private Long dupPubId; // 重复的成果ID
  private boolean EIIncluded; // 被EI收录了，
  private boolean SCIEIncluded; // 被SCIE收录了，
  private boolean ISTPIncluded; // 被ISTP收录了，
  private boolean SSCIIncluded; // 被SSCI收录了，
  private Long grpId; // 群组Id
  private String des3GrpId; // 加密群组ID
  private Integer isProjectPub; // 群组成果1 群组文献0

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public String getAuthorsNamesSpec() {
    return authorsNamesSpec;
  }

  public void setAuthorsNamesSpec(String authorsNamesSpec) {
    this.authorsNamesSpec = authorsNamesSpec;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getJsonPub() {
    return jsonPub;
  }

  public void setJsonPub(String jsonPub) {
    this.jsonPub = jsonPub;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public List<PubSituationDTO> getSituations() {
    return situations;
  }

  public void setSituations(List<PubSituationDTO> situations) {
    this.situations = situations;
  }

  public PubFulltextDTO getFullText() {
    return fullText;
  }

  public void setFullText(PubFulltextDTO fulltext) {
    this.fullText = fulltext;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public List<PubMemberDTO> getMembers() {
    return members;
  }

  public void setMembers(List<PubMemberDTO> members) {
    this.members = members;
  }

  public T getPubTypeInfo() {
    return pubTypeInfo;
  }

  public void setPubTypeInfo(T pubTypeInfo) {
    this.pubTypeInfo = pubTypeInfo;
  }

  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }

  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }

  public String getModifyDate() {
    return modifyDate;
  }

  public void setModifyDate(String modifyDate) {
    this.modifyDate = modifyDate;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Integer getAuthorMatch() {
    return authorMatch;
  }

  public void setAuthorMatch(Integer authorMatch) {
    this.authorMatch = authorMatch;
  }

  public String getSourceDbCode() {
    return sourceDbCode;
  }

  public void setSourceDbCode(String sourceDbCode) {
    this.sourceDbCode = sourceDbCode;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getPubYear() {
    return pubYear;
  }

  public void setPubYear(String pubYear) {
    this.pubYear = pubYear;
  }

  public String getPubHandlerName() {
    return pubHandlerName;
  }

  public void setPubHandlerName(String pubHandlerName) {
    this.pubHandlerName = pubHandlerName;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Integer getPubGenre() {
    return pubGenre;
  }

  public void setPubGenre(Integer pubGenre) {
    this.pubGenre = pubGenre;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public Long getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(Long dupPubId) {
    this.dupPubId = dupPubId;
  }

  public boolean getEIIncluded() {
    return EIIncluded;
  }

  public void setEIIncluded(boolean eIIncluded) {
    EIIncluded = eIIncluded;
  }

  public boolean getSCIEIncluded() {
    return SCIEIncluded;
  }

  public void setSCIEIncluded(boolean sCIEIncluded) {
    SCIEIncluded = sCIEIncluded;
  }

  public boolean getISTPIncluded() {
    return ISTPIncluded;
  }

  public void setISTPIncluded(boolean iSTPIncluded) {
    ISTPIncluded = iSTPIncluded;
  }

  public boolean getSSCIIncluded() {
    return SSCIIncluded;
  }

  public void setSSCIIncluded(boolean sSCIIncluded) {
    SSCIIncluded = sSCIIncluded;
  }

  public List<Accessory> getAccessorys() {
    return accessorys;
  }

  public void setAccessorys(List<Accessory> accessorys) {
    this.accessorys = accessorys;
  }

  public Long getGrpId() {
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

  public Integer getIsProjectPub() {
    return isProjectPub;
  }

  public void setIsProjectPub(Integer isProjectPub) {
    this.isProjectPub = isProjectPub;
  }

  public String getTmpSourceUrl() {
    return tmpSourceUrl;
  }

  public void setTmpSourceUrl(String tmpSourceUrl) {
    this.tmpSourceUrl = tmpSourceUrl;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

}
