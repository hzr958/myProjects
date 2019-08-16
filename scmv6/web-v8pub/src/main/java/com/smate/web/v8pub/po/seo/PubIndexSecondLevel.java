package com.smate.web.v8pub.po.seo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 首页论文seo改造 第二层级
 * 
 * @author TSZ
 * 
 */
@Entity
@Table(name = "pub_index_second_level")
public class PubIndexSecondLevel implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8332754322600923777L;
  // 第一级别分组
  private String firstGroup;
  // 第二级别分组
  private Integer secondGroup;
  // 第三级别分组
  private Integer thirdGroup;
  private String secondLabel;
  // 成果id
  private Long id;

  public PubIndexSecondLevel() {}


  public PubIndexSecondLevel(Long id) {
    this.id = id;
  }

  @Id
  @Column(name = "sl_id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long Id) {
    this.id = Id;
  }


  @Column(name = "second_group")
  public Integer getSecondGroup() {
    return secondGroup;
  }

  public void setSecondGroup(Integer secondGroup) {
    this.secondGroup = secondGroup;
  }

  @Column(name = "first_group")
  public String getFirstGroup() {
    return firstGroup;
  }


  public void setFirstGroup(String code) {
    this.firstGroup = code;
  }

  @Column(name = "third_group")
  public Integer getThirdGroup() {
    return thirdGroup;
  }


  public void setThirdGroup(Integer thirdGroup) {
    this.thirdGroup = thirdGroup;
  }

  @Column(name = "second_label")
  public String getSecondLabel() {
    return secondLabel;
  }


  public void setSecondLabel(String secondLabel) {
    this.secondLabel = secondLabel;
  }


  public static long getSerialversionuid() {
    return serialVersionUID;
  }


}
