package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author lichangwen
 * 
 */
public class LeftMenuForm implements Serializable {

  private static final long serialVersionUID = 6563481532264408565L;
  private Integer menuType;
  private String id;
  private String name;
  private int count;
  private List<LeftMenuForm> TypeList;
  private List<LeftMenuForm> YearList;
  private List<LeftMenuForm> disList;
  private List<LeftMenuForm> qaList;

  public Integer getMenuType() {
    return menuType;
  }

  public void setMenuType(Integer menuType) {
    this.menuType = menuType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<LeftMenuForm> getTypeList() {
    return TypeList;
  }

  public void setTypeList(List<LeftMenuForm> typeList) {
    TypeList = typeList;
  }

  public List<LeftMenuForm> getYearList() {
    return YearList;
  }

  public void setYearList(List<LeftMenuForm> yearList) {
    YearList = yearList;
  }

  public List<LeftMenuForm> getDisList() {
    return disList;
  }

  public void setDisList(List<LeftMenuForm> disList) {
    this.disList = disList;
  }

  public List<LeftMenuForm> getQaList() {
    return qaList;
  }

  public void setQaList(List<LeftMenuForm> qaList) {
    this.qaList = qaList;
  }

}
