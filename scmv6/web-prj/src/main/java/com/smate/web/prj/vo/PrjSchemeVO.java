package com.smate.web.prj.vo;

import java.io.Serializable;

import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.prj.model.common.PrjScheme;

/**
 * 项目资助类别页面提示模型类
 * 
 */
public class PrjSchemeVO implements Serializable {
  private static final long serialVersionUID = 2220256272323981674L;

  // 资助类别ID
  private Long code;
  // 资助类别
  private String name;
  // 为了检索方便自定义的字符串结构
  private String orderCode;
  // 资助机构ID
  private Long agencyId;

  public PrjSchemeVO(PrjScheme ps, String lang) {
    this.code = ps.getId();
    this.name = StringUtils.containsIgnoreCase(lang, "en") ? ps.getEnName() : ps.getName();
    this.orderCode = ps.getCode();
    this.agencyId = ps.getAgencyId();
  }

  public Long getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getOrderCode() {
    return orderCode;
  }

  public Long getAgencyId() {
    return agencyId;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

}
