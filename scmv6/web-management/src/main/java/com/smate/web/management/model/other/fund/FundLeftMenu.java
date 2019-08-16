package com.smate.web.management.model.other.fund;


import java.io.Serializable;
import java.util.List;

/**
 * 基金管理左菜单
 * 
 * @author lichangwen
 * 
 */
public class FundLeftMenu implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4099685103749626591L;

  private Long id;
  private String name;
  private Integer count;
  private List<FundLeftMenu> list;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public List<FundLeftMenu> getList() {
    return list;
  }

  public void setList(List<FundLeftMenu> list) {
    this.list = list;
  }

}
