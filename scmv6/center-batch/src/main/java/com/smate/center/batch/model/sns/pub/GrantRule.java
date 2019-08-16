package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 业务类别申请书成果填报规则
 * 
 * @author scy
 * 
 */
@Entity
@Table(name = "GRANT_RULE")
public class GrantRule implements Serializable {

  private static final long serialVersionUID = -6869566282168269008L;
  // 主键
  private Long id;
  // 业务类别code
  private Long code;
  // 业务名称
  private String nameKey;
  // 规则
  private String rules;
  // 申报年份
  private Integer year;
  // 提示信息
  private String helpTip;
  // 路径
  private String url;
  // 成果按什么排序
  private String orderBy;
  // 是否是自定义的成果类型
  private boolean isUserDefined;
  // 只有在是自定义类型下才有效，拥有什么类型（用逗号隔开）
  private String typeValues;
  // 默认提交的类型（只有在是自定义类型下才有效）
  private Integer defaultValue;

  public GrantRule() {
    super();
  }

  public GrantRule(Long id, Long code, String nameKey, String rules, Integer year, String helpTip, String url) {
    super();
    this.id = id;
    this.code = code;
    this.nameKey = nameKey;
    this.rules = rules;
    this.year = year;
    this.helpTip = helpTip;
    this.url = url;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PROPOSAL_RULE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "CODE")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Column(name = "RULES")
  public String getRules() {
    return rules;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

  @Column(name = "YEAR")
  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "NAME_KEY")
  public String getNameKey() {
    return nameKey;
  }

  public void setNameKey(String nameKey) {
    this.nameKey = nameKey;
  }

  @Column(name = "HELP_TIP")
  public String getHelpTip() {
    return helpTip;
  }

  public void setHelpTip(String helpTip) {
    this.helpTip = helpTip;
  }

  @Column(name = "ORDER_BY")
  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  @Column(name = "ISUSERDEFINED")
  public boolean getIsUserDefined() {
    return isUserDefined;
  }

  public void setIsUserDefined(boolean isUserDefined) {
    this.isUserDefined = isUserDefined;
  }

  @Column(name = "TYPE_VALUES")
  public String getTypeValues() {
    return typeValues;
  }

  public void setTypeValues(String typeValues) {
    this.typeValues = typeValues;
  }

  @Column(name = "DEFAULT_VALUE")
  public Integer getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Integer defaultValue) {
    this.defaultValue = defaultValue;
  }

}
