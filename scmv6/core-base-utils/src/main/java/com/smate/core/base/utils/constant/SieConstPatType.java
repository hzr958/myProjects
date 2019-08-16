package com.smate.core.base.utils.constant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 专利类型表
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "CONST_PAT_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SieConstPatType implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8531789533149982L;

  // 主键标识
  private Integer typeId;
  // 中文名称
  private String zhName;
  // 英文名称
  private String enName;
  // 1: enabled; 0: disabled
  private Integer enable;
  // 排序
  private Integer seqNo;

  private String name;

  @Id
  @Column(name = "TYPE_ID")
  public Integer getTypeId() {
    return typeId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "ENABLED")
  public Integer getEnable() {
    return enable;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setEnable(Integer enable) {
    this.enable = enable;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Transient
  public String getName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if (StringUtils.isBlank(name)) {
      if ("zh".equals(language)) {
        this.name = StringUtils.isNotBlank(zhName) ? zhName : enName;
      } else {
        this.name = StringUtils.isNotBlank(enName) ? enName : zhName;
      }
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
