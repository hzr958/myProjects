package com.smate.web.management.model.other.fund;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基金常数表，列表左菜单和添加编辑下拉框.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_POSITION")
public class ConstFundPosition implements Serializable {
  private static final long serialVersionUID = 6368194804863961035L;
  // code字段：基金列表左菜单标识
  public static final String FUND_LEFT_MENU = "fund_left_menu";
  // 机构类别
  public static final Long AGENCY_TYPE = 1L;
  // 学位
  public static final Long DEGREE = 2L;
  // 职称
  public static final Long TITLE = 3L;
  // 语言类别
  public static final Long LANGUAGE = 180L;
  private Long id;
  private String nameZh;
  private String nameEn;
  private Integer seqNo;
  private Long parentId;
  private String code;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "NAME_ZH")
  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  @Column(name = "NAME_EN")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "PARENT_ID")
  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

}
