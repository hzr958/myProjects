package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author lichangwen
 * 
 */
public class DeptSelect implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2240966601008878448L;

  private Long deptIdOther;
  private String deptNameOther;

  public DeptSelect() {
    super();
  }

  public DeptSelect(Long deptIdOther, String deptNameOther) {
    super();
    this.deptIdOther = deptIdOther;
    this.deptNameOther = deptNameOther;
  }

  public Long getDeptIdOther() {
    return deptIdOther;
  }

  public void setDeptIdOther(Long deptIdOther) {
    this.deptIdOther = deptIdOther;
  }

  public String getDeptNameOther() {
    return deptNameOther;
  }

  public void setDeptNameOther(String deptNameOther) {
    this.deptNameOther = deptNameOther;
  }

  @Override
  public String toString() {

    return ToStringBuilder.reflectionToString(this);
  }

}
