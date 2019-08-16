package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果关键词去重
 * 
 * @author warrior
 * 
 */
@Entity
@Table(name = "PUBLICATION_ALL_KEYWORDS_DUP")
public class PubAllkeywordDup implements Serializable {

  private static final long serialVersionUID = 6308608780212810309L;
  // 关键词Hash值
  @Id
  @Column(name = "KEYWORD_HASH")
  private Long keywordHash;
  // 出现的频数
  @Column(name = "COUNT")
  private Long count;

  public Long getKeywordHash() {
    return keywordHash;
  }

  public void setKeywordHash(Long keywordHash) {
    this.keywordHash = keywordHash;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

}
