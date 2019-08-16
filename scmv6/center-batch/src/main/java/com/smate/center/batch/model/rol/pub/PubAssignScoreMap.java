package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 成果匹配结果列表.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignScoreMap implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1761099634269399261L;
  // 作者序号
  private Integer seqNo;
  private long pubId;
  private Long insId;
  // 作者名
  private String name = null;
  // 作者简写
  private String initName = null;
  // 作者全称
  private String fullName = null;
  // scopus 0：作者fistname只有一个字母，1：作者fistname有多个字母
  private Integer nameType = 0;
  private Map<Long, PubAssignScoreDetail> detailMap = new HashMap<Long, PubAssignScoreDetail>();

  public PubAssignScoreMap() {
    super();
  }

  public PubAssignScoreMap(Integer seqNo, long pubId, Long insId) {
    super();
    this.seqNo = seqNo;
    this.pubId = pubId;
    this.insId = insId;
  }

  public PubAssignScoreMap(Integer seqNo, long pubId, Long insId, String initName, String fullName) {
    super();
    this.seqNo = seqNo;
    this.pubId = pubId;
    this.insId = insId;
    this.initName = initName;
    this.fullName = fullName;
  }

  public PubAssignScoreMap(Integer seqNo, long pubId, Long insId, String name, Integer nameType) {
    super();
    this.seqNo = seqNo;
    this.pubId = pubId;
    this.insId = insId;
    this.name = name;
    this.nameType = nameType;
  }

  public long getPubId() {
    return pubId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setPubId(long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNameType() {
    return nameType;
  }

  public void setNameType(Integer nameType) {
    this.nameType = nameType;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public String getInitName() {
    return initName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setInitName(String initName) {
    this.initName = initName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Map<Long, PubAssignScoreDetail> getDetailMap() {
    return detailMap;
  }

  public void setDetailMap(Map<Long, PubAssignScoreDetail> detailMap) {
    this.detailMap = detailMap;
  }

}
