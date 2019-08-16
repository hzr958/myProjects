package com.smate.web.v8pub.po.pdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 高新产业分类
 * 
 * @author YJ
 *
 *         2019年5月23日
 */

@Entity
@Table(name = "CATEGORY_HIGHTECH_INDUSTRY")
public class CategoryHightechIndustry implements Serializable {

  private static final long serialVersionUID = 1823974965550318636L;

  @Id
  @Column(name = "CODE")
  private String code; // 产业code，主键

  @Column(name = "NAME")
  private String name; // 产业名

  @Column(name = "ENAME")
  private String ename; // 英文产业名

  @Column(name = "PARENT_CODE")
  private String parentCode; // 父亲产业code

  @Column(name = "CODE_LEVEL")
  private Integer codeLevel; // 产业水平度

  @Transient
  private Long isEnd; // 是否最低产业

  public CategoryHightechIndustry() {

  }

  public CategoryHightechIndustry(String code, String name, String ename, String parentCode, Integer codeLevel,
      Long isEnd) {
    this.code = code;
    this.name = name;
    this.ename = ename;
    this.parentCode = parentCode;
    this.codeLevel = codeLevel;
    this.isEnd = isEnd;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getParentCode() {
    return parentCode;
  }

  public void setParentCode(String parentCode) {
    this.parentCode = parentCode;
  }

  public Integer getCodeLevel() {
    return codeLevel;
  }

  public void setCodeLevel(Integer codeLevel) {
    this.codeLevel = codeLevel;
  }

  public Long getIsEnd() {
    return isEnd;
  }

  public void setIsEnd(Long isEnd) {
    this.isEnd = isEnd;
  }

  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }


}
