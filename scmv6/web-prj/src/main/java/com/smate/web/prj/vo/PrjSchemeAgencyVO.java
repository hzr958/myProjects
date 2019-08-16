package com.smate.web.prj.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.smate.web.prj.model.common.PrjSchemeAgency;

/**
 * 项目资助机构自动提示页面数据模型.
 *
 * @author houchuanjie
 * @date 2018年3月20日 下午8:25:13
 */

public class PrjSchemeAgencyVO implements Serializable {
  private static final long serialVersionUID = -8736478818236176946L;

  // 项目资助机构ID
  private Long code;
  // 项目资助机构
  private String name;
  // 为了检索方便自定义的字符串结构
  private String orderCode;

  /**
   * @param code
   * @param name
   * @param enName
   * @param orderCode
   */
  public PrjSchemeAgencyVO(Long code, String name, String orderCode) {
    super();
    this.code = code;
    this.name = name;
    this.orderCode = orderCode;
  }

  public PrjSchemeAgencyVO(PrjSchemeAgency psa, String lang) {
    this.code = psa.getId();
    this.name = StringUtils.containsIgnoreCase(lang, "en") ? psa.getEnName() : psa.getName();
    this.orderCode = psa.getCode();
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

  public void setCode(Long code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

}
