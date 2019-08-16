package com.smate.center.task.model.pub.seo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 首页论文seo改造 第一层级
 * 
 * @author TSZ
 * 
 */
@Entity
@Table(name = "pub_index_first_level")
// @org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class PubIndexFirstLevel implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8332754322600923777L;
  // Id
  private Long id;
  // 第一级别分组
  private String firstGroup;
  // 分组标签
  private String firstLabel;

  // 第二级别分组
  private Integer secondGroup;

  @Id
  @Column(name = "fl_id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "first_group")
  public String getFirstGroup() {
    return firstGroup;
  }

  public void setFirstGroup(String firstGroup) {
    this.firstGroup = firstGroup;
  }

  @Column(name = "first_label")
  public String getFirstLabel() {
    return firstLabel;
  }

  public void setFirstLabel(String firstLabel) {
    this.firstLabel = firstLabel;
  }

  @Column(name = "second_group")
  public Integer getSecondGroup() {
    return secondGroup;
  }

  public void setSecondGroup(Integer secondGroup) {
    this.secondGroup = secondGroup;
  }

}
