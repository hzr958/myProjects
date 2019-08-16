package com.smate.core.base.utils.model.consts;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 系统常量.
 * 
 * @author new
 * 
 */
@Entity
@Table(name = "CONST_DICTIONARY")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ConstDictionary2 implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5024006350858378526L;

  private ConstDictionaryKey2 key;

  /**
   * 英文描述.
   */
  private String enUsName;
  /**
   * 中文描述.
   */
  private String zhCnName;

  /**
   * 繁体中文.
   */
  private String zhTwName;

  /**
   * 排序.
   */
  private String seqNo;

  private ConstCategory2 constCategory;

  private boolean editFlag;

  public ConstDictionary2() {}

  public ConstDictionary2(String category, String code, String enUsName, String zhCnName, String zhTwName,
      String seqNo) {
    super();
    this.key = new ConstDictionaryKey2(category, code);
    this.enUsName = enUsName;
    this.zhCnName = zhCnName;
    this.zhTwName = zhTwName;
    this.seqNo = seqNo;
  }

  /**
   * 联合主键.
   * 
   * @return key
   */
  @EmbeddedId
  public ConstDictionaryKey2 getKey() {
    return key;
  }

  /**
   * @param key
   */
  public void setKey(ConstDictionaryKey2 key) {
    this.key = key;
  }

  /**
   * @return
   */
  @Column(name = "EN_US_CAPTION")
  public String getEnUsName() {
    return enUsName;
  }

  /**
   * @param enUsName
   */
  public void setEnUsName(String enUsName) {
    this.enUsName = enUsName;
  }

  /**
   * @return
   */
  @Column(name = "ZH_CN_CAPTION")
  public String getZhCnName() {
    return zhCnName;
  }

  /**
   * @param sccaption
   */
  public void setZhCnName(String zhCnName) {
    this.zhCnName = zhCnName;
  }

  /**
   * @return
   */
  @Column(name = "SEQ_NO")
  public String getSeqNo() {
    return seqNo;
  }

  /**
   * @param seqNo
   */
  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  public void setZhTwName(String zhTwName) {
    this.zhTwName = zhTwName;
  }

  @Column(name = "ZH_TW_CAPTION")
  public String getZhTwName() {
    return zhTwName;
  }

  @Transient
  public ConstCategory2 getConstCategory() {
    return constCategory;
  }

  public void setConstCategory(ConstCategory2 constCategory) {
    this.constCategory = constCategory;
  }

  @Transient
  public boolean isEditFlag() {
    return editFlag;
  }

  public void setEditFlag(boolean editFlag) {
    this.editFlag = editFlag;
  }

}
