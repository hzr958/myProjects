package com.smate.core.base.consts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 关键字-学科 库.
 * 
 * @author zzx
 * 
 */
@Entity
@Table(name = "CONST_KEY_DISCODES")
public class ConstKeyDisc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2467297195382733818L;

  private Long id;
  private String keyWord;
  private String discCodes;
  private String searchKey;

  public ConstKeyDisc() {
    super();
  }

  public ConstKeyDisc(String keyWord, String discCodes) {
    super();
    this.keyWord = keyWord;
    this.discCodes = discCodes;
  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_KEY_DISCODES", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "KEY_WORD")
  public String getKeyWord() {
    return keyWord;
  }

  public void setKeyWord(String keyWord) {
    this.keyWord = keyWord;
  }

  @Column(name = "DISC_CODES")
  public String getDiscCodes() {
    return discCodes;
  }

  public void setDiscCodes(String discCodes) {
    this.discCodes = discCodes;
  }

  @Column(name = "SEARCH_KEY")
  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

}
