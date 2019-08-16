package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 通用关键词，主要是用于英文通用关键词过滤.
 * 
 * @author lqh
 * 
 */
@Entity
@Table(name = "KEYWORDS_DIC_COMWORD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class KeywordsDicComWord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3867200137519313685L;
  private Long id;
  private String word;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "WORD")
  public String getWord() {
    return word;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setWord(String word) {
    this.word = word;
  }

}
