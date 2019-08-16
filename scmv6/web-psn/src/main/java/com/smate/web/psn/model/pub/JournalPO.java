package com.smate.web.psn.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;

/**
 * 期刊信息实体
 * 
 * @author tsz
 */
@Entity
@Table(name = "JOURNAL")
public class JournalPO implements Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = 1370428911808601621L;

  private Long id;
  /**
   * 期刊中文名.
   */
  private String zhName;

  /**
   * 期刊英文名.
   */
  private String enName;
  /**
   * ISSN.
   */
  private String issn;

  /**
   * 英文别名.
   */
  private String enameAlias;

  /**
   * 中文别名.
   */
  private String zhNameAlias;

  /**
   * 添加人.
   */
  private Long addPsnId;

  // 是否同步基准库数据 任务同步pdwh数据用
  private Integer matchBaseStatus;
  // 基础期刊id
  private Long matchBaseJnlId;

  public JournalPO() {
    super();
  }

  public JournalPO(Long id) {
    super();
    this.id = id;
  }

  public JournalPO(Long id, String zhName, String enName, String issn) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.issn = issn;
  }

  public JournalPO(String zhName, String enName, String issn, Long matchBaseJnlId) {
    super();
    this.zhName = zhName;
    this.enName = enName;
    this.issn = issn;
    this.matchBaseJnlId = matchBaseJnlId;
  }

  public JournalPO(Long id, String zhName, String enName, String issn, Long matchBaseJnlId) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.issn = issn;
    this.matchBaseJnlId = matchBaseJnlId;
  }

  public JournalPO(Long id, String zhName, String enName, String issn, String enameAlias, String zhNameAlias,
      Long addPsnId, Integer matchBaseStatus, Long matchBaseJnlId) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.issn = issn;
    this.enameAlias = enameAlias;
    this.zhNameAlias = zhNameAlias;
    this.addPsnId = addPsnId;
    this.matchBaseStatus = matchBaseStatus;
    this.matchBaseJnlId = matchBaseJnlId;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "JID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_JOURNAL_SNS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  /**
   * @param issn the issn to set
   */
  public void setIssn(String issn) {
    this.issn = issn;
  }

  /**
   * @return the issn
   */
  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  /**
   * @param addPsnId
   */
  public void setAddPsnId(Long addPsnId) {
    this.addPsnId = addPsnId;
  }

  /**
   * @return
   */
  @Column(name = "REG_PSN_ID")
  public Long getAddPsnId() {
    return addPsnId;
  }

  public String toString() {
    return String.format("Journal(%s,%s,%s)", this.id, this.enName, this.zhName);
  }

  @Column(name = "MATCH_BASE_STATUS")
  public Integer getMatchBaseStatus() {
    return matchBaseStatus;
  }

  public void setMatchBaseStatus(Integer matchBaseStatus) {
    this.matchBaseStatus = matchBaseStatus;
  }

  @Column(name = "MATCH_BASE_JNL_ID")
  public Long getMatchBaseJnlId() {
    return matchBaseJnlId;
  }

  public void setMatchBaseJnlId(Long matchBaseJnlId) {
    this.matchBaseJnlId = matchBaseJnlId;
  }

  @Column(name = "ZNAME_ALIAS")
  public String getZhNameAlias() {
    if (StringUtils.isNotBlank(this.zhName)) {
      zhNameAlias = JnlFormateUtils.getStrAlias(this.zhName);
    }
    return zhNameAlias;
  }

  public void setZhNameAlias(String zhNameAlias) {
    this.zhNameAlias = zhNameAlias;
  }

  /**
   * @param enameAlias the enameAlias to set
   */
  public void setEnameAlias(String enameAlias) {
    this.enameAlias = enameAlias;
  }

  /**
   * @return the enameAlias
   */
  @Column(name = "ename_alias")
  public String getEnameAlias() {
    if (StringUtils.isNotBlank(this.enName))
      enameAlias = JnlFormateUtils.getStrAlias(this.enName);
    return enameAlias;
  }


}
