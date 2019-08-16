package com.smate.core.base.keywords.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.common.HtmlUtils;


/**
 * 关键词实体类.
 * 
 * @author zk
 * 
 */
public class DiscKeyword {


  /** 主键id. */
  private Long keyId;

  /** 学科代码id. */
  private Long discId;

  /** 关键词中文名. */
  private String zhKey;

  /** 关键词英文名. */
  private String enKey;

  /** 关键词拼音. */
  private String pinyin;

  /** 关键词长度. */
  private int wordLength;

  /** 字频. */
  private Long tf;

  /** 使用次数. */
  private Long usedTimes;

  /** 赞成票数 . */
  private Long approve;

  /** 反对票数 . */
  private Long opposition;

  /** 关键词创建人 . */
  private Long createdPsn;

  /** 状态：0待审核，1：审核通过，2：删除. */
  private int status;
  /** 截字中文关键词. */
  private String subZhKey;
  /** 截字英文关键词. */
  private String subEnKey;
  /** 列表显示首选英文. */
  private String firstEnKey;
  /** 截字首选英文. */
  private String subFirstEnkey;
  /** 当前人投票情况：0：未投票1：赞成票；2：反对票. */
  private int voteStatus;

  @Id
  @Column(name = "KEY_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DISC_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getKeyId() {
    return keyId;
  }

  public void setKeyId(Long keyId) {
    this.keyId = keyId;
  }

  @Column(name = "DISC_ID", nullable = false)
  public Long getDiscId() {
    return discId;
  }

  public void setDiscId(Long discId) {
    this.discId = discId;
  }

  @Column(name = "ZH_KEY")
  public String getZhKey() {
    return zhKey;
  }

  public void setZhKey(String zhKey) {
    this.zhKey = zhKey;
  }

  @Column(name = "EN_KEY")
  public String getEnKey() {
    return enKey;
  }

  public void setEnKey(String enKey) {
    this.enKey = enKey;
  }

  @Column(name = "PIN_YIN")
  public String getPinyin() {
    return pinyin;
  }

  public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
  }

  @Column(name = "WORD_LENGTH")
  public int getWordLength() {
    return wordLength;
  }

  public void setWordLength(int wordLength) {
    this.wordLength = wordLength;
  }

  @Column(name = "TF")
  public Long getTf() {
    return tf;
  }

  public void setTf(Long tf) {
    this.tf = tf;
  }

  @Column(name = "USE_TIMES")
  public Long getUsedTimes() {
    return usedTimes;
  }

  public void setUsedTimes(Long usedTimes) {
    this.usedTimes = usedTimes;
  }

  @Column(name = "APPROVE")
  public Long getApprove() {
    return approve;
  }

  public void setApprove(Long approve) {
    this.approve = approve;
  }

  @Column(name = "OPPOSITION")
  public Long getOpposition() {
    return opposition;
  }

  public void setOpposition(Long opposition) {
    this.opposition = opposition;
  }

  @Column(name = "CREATE_PSN")
  public Long getCreatedPsn() {
    return createdPsn;
  }

  public void setCreatedPsn(Long createdPsn) {
    this.createdPsn = createdPsn;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DiscKeyword other = (DiscKeyword) obj;
    if (keyId == null) {
      if (other.keyId != null)
        return false;
    } else if (!keyId.equals(other.keyId))
      return false;
    return true;
  }

  @Transient
  public String getSubZhKey() {
    subZhKey = this.zhKey;
    if (StringUtils.isNotBlank(subZhKey))
      subZhKey = HtmlUtils.toHtml(subZhKey);
    return subZhKey;
  }

  public void setSubZhKey(String subZhKey) {
    this.subZhKey = subZhKey;
  }

  @Transient
  public String getSubEnKey() {
    subEnKey = this.enKey;
    if (StringUtils.isNotBlank(subEnKey))
      subEnKey = HtmlUtils.toHtml(subEnKey);
    return subEnKey;
  }

  public void setSubEnKey(String subEnKey) {
    this.subEnKey = subEnKey;
  }

  @Transient
  public String getFirstEnKey() {
    return firstEnKey;
  }

  public void setFirstEnKey(String firstEnKey) {
    this.firstEnKey = firstEnKey;
  }

  @Transient
  public String getSubFirstEnkey() {
    subFirstEnkey = this.firstEnKey;
    if (StringUtils.isNotBlank(subFirstEnkey))
      subFirstEnkey = HtmlUtils.toHtml(subFirstEnkey);
    return subFirstEnkey;
  }

  public void setSubFirstEnkey(String subFirstEnkey) {
    this.subFirstEnkey = subFirstEnkey;
  }

  @Transient
  public int getVoteStatus() {
    return voteStatus;
  }

  public void setVoteStatus(int voteStatus) {
    this.voteStatus = voteStatus;
  }
}
