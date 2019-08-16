package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 关键词字典，用于拆分成果标题、摘要等使用.
 * 
 * @author lqh
 * 
 */
@Entity
@Table(name = "KEYWORDS_DIC")
public class KeywordsDic implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7343956563734902196L;

  private Long id;
  // 关键词
  private String keyword;
  // 关键词格式后的
  private String kwtxt;
  // 连接特征字符
  private String fturesWd;
  // 连接特征字符hashcode
  private Long fturesHash;
  // 关键词hashcode
  private Long kwhash;
  // 关键词反序hashcode
  private Long kwRhash;
  // 英文1，中文2
  private Integer type;
  // 关键词单词个数
  private Integer wlen;

  public KeywordsDic() {
    super();
  }

  public KeywordsDic(Long id, String keyword, String kwtxt) {
    super();
    this.id = id;
    this.keyword = keyword;
    this.kwtxt = kwtxt;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KEYWORD_TEXT")
  public String getKwtxt() {
    return kwtxt;
  }

  @Column(name = "FTURES_WORD")
  public String getFturesWd() {
    return fturesWd;
  }

  @Column(name = "FTURES_HASH")
  public Long getFturesHash() {
    return fturesHash;
  }

  @Column(name = "KW_HASH")
  public Long getKwhash() {
    return kwhash;
  }

  @Column(name = "KW_RHASH")
  public Long getKwRhash() {
    return kwRhash;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  @Column(name = "WORD_LEN")
  public Integer getWlen() {
    return wlen;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setWlen(Integer wlen) {
    this.wlen = wlen;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwtxt(String kwtxt) {
    this.kwtxt = kwtxt;
  }

  public void setFturesWd(String fturesWd) {
    this.fturesWd = fturesWd;
  }

  public void setFturesHash(Long fturesHash) {
    this.fturesHash = fturesHash;
  }

  public void setKwhash(Long kwhash) {
    this.kwhash = kwhash;
  }

  public void setKwRhash(Long kwRhash) {
    this.kwRhash = kwRhash;
  }

}
