package com.smate.core.base.consts.model.psnname;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员姓名中 多音字中文姓氏与其对应的拼音，首字母大写 （eg : 单 -》Shan）
 * 
 * @author SYL
 *
 */
@Entity
@Table(name = "CONST_PSN_LASTNAME_PY")
public class ConstPsnLastNamePy implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * 主键id
   */
  private Long id;
  /**
   * 中文姓氏
   */
  private String zhWord;
  /**
   * 姓氏对应的拼音（首字母大写）
   */
  private String pinyinWord;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "ZH_WORD")
  public String getZhWord() {
    return zhWord;
  }

  public void setZhWord(String zhWord) {
    this.zhWord = zhWord;
  }

  @Column(name = "PINYIN_WORD")
  public String getPinyinWord() {
    return pinyinWord;
  }

  public void setPinyinWord(String pinyinWord) {
    this.pinyinWord = pinyinWord;
  }



}
