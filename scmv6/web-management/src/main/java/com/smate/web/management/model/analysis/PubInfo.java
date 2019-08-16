package com.smate.web.management.model.analysis;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.pub.model.ErrorField;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.management.model.analysis.rol.PubConfirmRolPub;
import com.smate.web.management.model.analysis.sns.PubErrorFields;
import com.smate.web.management.model.analysis.sns.PubFulltext;
import com.smate.web.management.model.analysis.sns.PubStatistics;


/**
 * 成果显示信息对象
 * 
 * @author tsz
 *
 */
public class PubInfo {
  // 数据准备
  private Publication pub; // 成果
  private PubConfirmRolPub pubrol;// 推荐成果
  private PubFulltext pubFulltext; // 全文
  private PubStatistics pubStatistics; // 成果统计数
  private List<PubErrorFields> pubErrorFields;// 成果缺失选项
  // 完整性检查字段.
  private Set<ErrorField> errorFields;
  // 显示数据
  private Long pubId; // 成果id
  private String fulltextImagePath; // 成果全文图片路径
  private String fulltextGif;// 成果全文小图片
  private Long fulltextFileid;// 全文id
  private String fullTextFileExt;// 全文附件后缀
  private Integer citedTimes; // 引用次数
  private Integer readCount; // 阅读次数
  private Integer awardCount; // 点赞次数
  private Integer shareCount; // 分享次数
  private Integer commentCount; // 评论次数
  private Integer publishYear;// 出版年份
  private String authorNames;// 作者
  private String title;// 成果名称,区分中英文
  private String briefDesc;// 来源,区分中英文
  private String des3PubId; // 成果加密id
  private Integer typeId;// 成果类型,见 const_pub_type
  private Integer articleType = 1;// 内容类型 publication=1,reference=2,file=3,project=4
  private Integer nodeId = ServiceConstants.SCHOLAR_NODE_ID_1;// 成果输入节点,默认1
  private Long groupId;// 群组
  private Long simpleStatus;// pubSimple任务处理进度
  private String des3ressendid;
  private String des3resrecid;
  private Long awardId;
  private Long shareId;
  private String domain;
  private Integer dataValidate;
  private Long psnId;// 成果作者
  private Long groupPubsId;// 群组成果id
  private boolean isExistsInGroup;// 成果是否已加入群组
  private String impactFactors;// 文章所属的期刊的影响因子
  private String zhTitle;
  private String enTitle;
  private String zhAbs;
  private String enAbs;
  private String zhKws;
  private String enKws;

  public PubInfo() {}

  public PubInfo(String zhTitle, String enTitle, String zhAbs, String enAbs, String zhKws, String enKws) {
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.zhAbs = zhAbs;
    this.enAbs = enAbs;
    this.zhKws = zhKws;
    this.enKws = enKws;
  }

  public Publication getPub() {
    return pub;
  }

  public void setPub(Publication pub) {
    this.pub = pub;
  }

  public PubFulltext getPubFulltext() {
    return pubFulltext;
  }

  public void setPubFulltext(PubFulltext pubFulltext) {
    this.pubFulltext = pubFulltext;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }



  public String getFulltextImagePath() {
    return fulltextImagePath;
  }

  public void setFulltextImagePath(String fulltextImagePath) {
    this.fulltextImagePath = fulltextImagePath;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public PubStatistics getPubStatistics() {
    return pubStatistics;
  }

  public void setPubStatistics(PubStatistics pubStatistics) {
    this.pubStatistics = pubStatistics;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getDes3PubId() {
    if (StringUtils.isBlank(this.des3PubId) && this.pubId != null) {
      return ServiceUtil.encodeToDes3(this.pubId.toString());
    } else {
      return des3PubId;
    }
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public PubConfirmRolPub getPubrol() {
    return pubrol;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setPubrol(PubConfirmRolPub pubrol) {
    this.pubrol = pubrol;
  }


  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Integer getNodeId() {
    return nodeId;
  }


  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getDes3ressendid() {
    return des3ressendid;
  }

  public void setDes3ressendid(String des3ressendid) {
    this.des3ressendid = des3ressendid;
  }

  public String getDes3resrecid() {
    return des3resrecid;
  }

  public void setDes3resrecid(String des3resrecid) {
    this.des3resrecid = des3resrecid;
  }

  public String getFulltextGif() {
    return fulltextGif;
  }

  public void setFulltextGif(String fulltextGif) {
    this.fulltextGif = fulltextGif;
  }

  public Long getFulltextFileid() {
    return fulltextFileid;
  }

  public void setFulltextFileid(Long fulltextFileid) {
    this.fulltextFileid = fulltextFileid;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public Long getSimpleStatus() {
    return simpleStatus;
  }

  public void setSimpleStatus(Long simpleStatus) {
    this.simpleStatus = simpleStatus;
  }

  public Long getAwardId() {
    return awardId;
  }

  public void setAwardId(Long awardId) {
    this.awardId = awardId;
  }

  public Long getShareId() {
    return shareId;
  }

  public void setShareId(Long shareId) {
    this.shareId = shareId;
  }

  public String getFullTextFileExt() {
    return fullTextFileExt;
  }

  public void setFullTextFileExt(String fullTextFileExt) {
    this.fullTextFileExt = fullTextFileExt;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public List<PubErrorFields> getPubErrorFields() {
    return pubErrorFields;
  }

  public void setPubErrorFields(List<PubErrorFields> pubErrorFields) {
    this.pubErrorFields = pubErrorFields;
  }

  public Set<ErrorField> getErrorFields() {
    return errorFields;
  }

  public void setErrorFields(Set<ErrorField> errorFields) {
    this.errorFields = errorFields;
  }

  public Integer getDataValidate() {
    return dataValidate;
  }

  public void setDataValidate(Integer dataValidate) {
    this.dataValidate = dataValidate;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGroupPubsId() {
    return groupPubsId;
  }

  public void setGroupPubsId(Long groupPubsId) {
    this.groupPubsId = groupPubsId;
  }

  public boolean getIsExistsInGroup() {
    return isExistsInGroup;
  }

  public void setIsExistsInGroup(boolean isExistsInGroup) {
    this.isExistsInGroup = isExistsInGroup;
  }

  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getZhAbs() {
    return zhAbs;
  }

  public void setZhAbs(String zhAbs) {
    this.zhAbs = zhAbs;
  }

  public String getEnAbs() {
    return enAbs;
  }

  public void setEnAbs(String enAbs) {
    this.enAbs = enAbs;
  }

  public String getZhKws() {
    return zhKws;
  }

  public void setZhKws(String zhKws) {
    this.zhKws = zhKws;
  }

  public String getEnKws() {
    return enKws;
  }

  public void setEnKws(String enKws) {
    this.enKws = enKws;
  }

}
