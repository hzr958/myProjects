package com.smate.web.management.model.patent;

import java.io.Serializable;

/**
 * 专利数据信息显示
 * 
 * @author Administrator
 *
 */
public class PatentForm implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 2699710407295108397L;
  private Long id;
  private int mId;// 菜单Id
  private String subject;// 所属学科
  private String patentType;// 专利类型
  private String cdata;// 统计对比数据
  private String mdata;// 矩阵数据
  private String measures;// 矩阵数据
  private String planning;// 矩阵数据
  private String cmax;// 矩阵图颜色范围峰值
  private String tsvDataName;

  public String getCdata() {
    return cdata;
  }

  public void setCdata(String cdata) {
    this.cdata = cdata;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getPatentType() {
    return patentType;
  }

  public void setPatentType(String pantentType) {
    this.patentType = pantentType;
  }

  public String getMdata() {
    return mdata;
  }

  public void setMdata(String mdata) {
    this.mdata = mdata;
  }

  public String getMeasures() {
    return measures;
  }

  public void setMeasures(String measures) {
    this.measures = measures;
  }

  public String getPlanning() {
    return planning;
  }

  public void setPlanning(String planning) {
    this.planning = planning;
  }

  public String getCmax() {
    return cmax;
  }

  public void setCmax(String cmax) {
    this.cmax = cmax;
  }

  public String getTsvDataName() {
    return tsvDataName;
  }

  public void setTsvDataName(String tsvDataName) {
    this.tsvDataName = tsvDataName;
  }

}
