package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * 成果匹配上下文.
 * 
 * @author liqinghua
 * 
 */
public class PubAssginMatchContext implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6776083976903198164L;

  // 单位ID.
  private Long insId;
  // ----------------start 以下数据是成果匹配人员使用------------
  // 成果DBID.
  private Long dbId;
  // 成果ID.
  private Long pubId;
  // 单位成果.
  private PublicationRol pub;
  // KEY为成果SEQNO.
  private Map<Integer, PubAssignScoreMap> pubAssignScoreMap = new HashMap<Integer, PubAssignScoreMap>();
  // seqNo为空的作者匹配详情.
  private Map<Long, PubAssignScoreDetail> nullSeqScoreMap = new HashMap<Long, PubAssignScoreDetail>();
  // 评分配置记录.
  private SettingPubAssignScoreWrap settingPubAssignMatchScore;
  // ----------------end 以下数据是成果匹配人员使用------------

  // ----------------start 以下数据是人员匹配成果使用----------
  // 待匹配的人员ID
  private Long psnId;
  // KEY为PUB_ID
  private Map<Long, Map<Integer, PubAssignScoreMap>> psnAssignScoreMap =
      new HashMap<Long, Map<Integer, PubAssignScoreMap>>();
  private Boolean hasMatchedPsn = false;
  // 人员匹配成果类型，ref:PsnMatchTypeConstants
  private int psnMatchType = 1;

  // ----------------end 以下数据是人员匹配成果使用------------

  public Long getDbId() {
    return dbId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Map<Integer, PubAssignScoreMap> getPubAssignScoreMap() {
    return pubAssignScoreMap;
  }

  public void setPubAssignScoreMap(Map<Integer, PubAssignScoreMap> pubAssignScoreMap) {
    this.pubAssignScoreMap = pubAssignScoreMap;
  }

  public int getPsnMatchType() {
    return psnMatchType;
  }

  public void setPsnMatchType(int psnMatchType) {
    this.psnMatchType = psnMatchType;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public PublicationRol getPub() {
    return pub;
  }

  public void setPub(PublicationRol pub) {
    this.pub = pub;
  }

  public SettingPubAssignScoreWrap getSettingPubAssignMatchScore() {
    return settingPubAssignMatchScore;
  }

  public void setSettingPubAssignMatchScore(SettingPubAssignScoreWrap settingPubAssignMatchScore) {
    this.settingPubAssignMatchScore = settingPubAssignMatchScore;
  }

  /**
   * 获取匹配上的用户ID列表.
   * 
   * @return
   */
  public Set<Long> getMatchedPsnIds() {

    Set<Long> matchPsnIds = new HashSet<Long>();
    if (!CollectionUtils.isEmpty(pubAssignScoreMap)) {
      Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
      while (iter.hasNext()) {
        matchPsnIds.addAll(pubAssignScoreMap.get(iter.next()).getDetailMap().keySet());
      }
    }
    return matchPsnIds;
  }

  /**
   * 是否有匹配上的用户.
   * 
   * @return
   */
  public boolean hasMatchedPsn() {

    if (!hasMatchedPsn && getMatchedPsnIds().size() > 0) {
      hasMatchedPsn = true;
    }
    return hasMatchedPsn;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Map<Long, Map<Integer, PubAssignScoreMap>> getPsnAssignScoreMap() {
    return psnAssignScoreMap;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPsnAssignScoreMap(Map<Long, Map<Integer, PubAssignScoreMap>> psnAssignScoreMap) {
    this.psnAssignScoreMap = psnAssignScoreMap;
  }

  /**
   * 是否是导入ISI成果.
   * 
   * @return
   */
  public boolean isIsiImport() {

    return PubXmlDbUtils.isIsiDb(this.dbId);
  }

  /**
   * 是否是导入scopus成果.
   * 
   * @return
   */
  public boolean isScopusImport() {

    return PubXmlDbUtils.isScopusDb(this.dbId);
  }

  /**
   * 是否是导入CNKI成果.
   * 
   * @return
   */
  public boolean isCnkiImport() {

    return PubXmlDbUtils.isCnkiDb(this.dbId);
  }

  /**
   * 是否是导入Cnipr成果.
   * 
   * @return
   */
  public boolean isCniprImport() {

    return PubXmlDbUtils.isCNIPRDb(this.dbId);
  }

  /**
   * 是否是导入CnkiPat成果.
   * 
   * @return
   */
  public boolean isCnkiPatImport() {

    return PubXmlDbUtils.isCnkipatDb(this.dbId);
  }

  /**
   * 是否是导入PUBMED成果.
   * 
   * @return
   */
  public boolean isPubMedImport() {

    return PubXmlDbUtils.isPubMedDb(this.dbId);
  }

  /**
   * 是否是导入EI成果.
   * 
   * @return
   */
  public boolean isEiImport() {

    return PubXmlDbUtils.isEiDb(this.dbId);
  }

  public Map<Long, PubAssignScoreDetail> getNullSeqScoreMap() {
    return nullSeqScoreMap;
  }

  public void setNullSeqScoreMap(Map<Long, PubAssignScoreDetail> nullSeqScoreMap) {
    this.nullSeqScoreMap = nullSeqScoreMap;
  }

}
