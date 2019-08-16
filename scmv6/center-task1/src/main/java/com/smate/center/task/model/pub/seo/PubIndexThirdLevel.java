package com.smate.center.task.model.pub.seo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 首页论文seo改造 第三层级
 */
@Entity
@Table(name = "pub_index_third_level")
public class PubIndexThirdLevel implements Serializable {


  private static final long serialVersionUID = 4190063185933714540L;

  // 成果名字 en_name
  private String title;
  // 第一层级分组 即英文名第一个字母
  private String firstLetter;
  // 排序标记
  private String orderMark;
  // 第二级别分组
  private Integer secondGroup;
  // 第三级别分组
  private Integer thirdGroup;
  // 成果id
  private Long pubId;


  public PubIndexThirdLevel() {}

  public PubIndexThirdLevel(Long pubId, String title) {
    this.pubId = pubId;
    this.title = title;
  }

  public PubIndexThirdLevel(Long pubId) {
    this.pubId = pubId;
  }

  @Id
  @Column(name = "pub_id")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "first_letter")
  public String getFirstLetter() {
    return firstLetter;
  }

  public void setFirstLetter(String firstLetter) {
    this.firstLetter = firstLetter;
  }

  @Column(name = "order_mark")
  public String getOrderMark() {
    return orderMark;
  }

  public void setOrderMark(String orderMark) {
    this.orderMark = orderMark;
  }

  @Column(name = "second_group")
  public Integer getSecondGroup() {
    return secondGroup;
  }

  public void setSecondGroup(Integer secondGroup) {
    this.secondGroup = secondGroup;
  }

  @Column(name = "third_group")
  public Integer getThirdGroup() {
    return thirdGroup;
  }

  public void setThirdGroup(Integer thirdGroup) {
    this.thirdGroup = thirdGroup;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }



}
