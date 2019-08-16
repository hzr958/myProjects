package com.smate.center.batch.model.rol.pub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.Page;


/**
 * 成果提交检索.
 * 
 * @author liqinghua
 * 
 */
@SuppressWarnings("unchecked")
public class PubSubmissionSearchForm extends BasePublicationRolForm {

  /**
   * 
   */
  private static final long serialVersionUID = 2817693423716578548L;

  // 成果类型
  private List<ConstPubType> pubTypes;
  // 成果状态
  private List<ConstDictionary> pubStatus;

  /**
   * 发表年份(开始).
   */
  private Integer fromYear;
  /**
   * 发表年份(结束).
   */
  private Integer toYear;
  /**
   * 成果类型Id.
   */
  private Integer typeId;

  private Integer status;

  private String pubIds;

  private Set<Long> pubIdsSet;

  private Page page;

  private String actionMsg;

  private String orderBy;

  private String des3PsnId;

  private Long psnId;
  private String psnName;

  private String snsDomain;

  public void copyForm(PubSubmissionSearchForm result) {

    this.pubTypes = result.getPubTypes();
    this.pubStatus = result.getPubStatus();
    this.fromYear = result.getFromYear();
    this.toYear = result.getToYear();
    this.typeId = result.getTypeId();
    this.status = result.getStatus();
    this.pubIds = result.getPubIds();
    this.pubIdsSet = result.getPubIdsSet();
    this.page = result.getPage();
    this.psnId = result.getPsnId();
    this.psnName = result.getPsnName();
    this.snsDomain = result.getSnsDomain();
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Integer getFromYear() {
    return fromYear;
  }

  public Integer getToYear() {
    return toYear;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setFromYear(Integer fromYear) {
    this.fromYear = fromYear;
  }

  public void setToYear(Integer toYear) {
    this.toYear = toYear;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public List<ConstPubType> getPubTypes() {
    return pubTypes;
  }

  public List<ConstDictionary> getPubStatus() {
    return pubStatus;
  }

  public void setPubTypes(List<ConstPubType> pubTypes) {
    this.pubTypes = pubTypes;
  }

  public void setPubStatus(List<ConstDictionary> pubStatus) {
    this.pubStatus = pubStatus;
  }

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public String getActionMsg() {
    return actionMsg;
  }

  public void setActionMsg(String actionMsg) {
    this.actionMsg = actionMsg;
  }

  /**
   * 获取成果ID set结果集.
   * 
   * @return
   */
  public Set<Long> getPubIdsSet() {

    if (pubIdsSet == null && pubIds != null && pubIds.matches(ServiceConstants.IDPATTERN)) {
      pubIdsSet = new HashSet<Long>();
      String[] strPubIds = pubIds.split(",");
      for (String strpubId : strPubIds) {
        pubIdsSet.add(Long.valueOf(strpubId));
      }
    }
    return pubIdsSet;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }

}
