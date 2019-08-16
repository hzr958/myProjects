package com.smate.sie.web.application.model.validate;

import java.io.Serializable;

/**
 * vip产品信息
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
public class VipProduct implements Serializable {

  private Long id; // 主键
  private String productName; // 收费产品名称
  private double price; // 收费产品价格
  private Integer status; // 收费产品是否有效
  private String description; // 收费产品描述

  public VipProduct() {
    super();
  }

  public VipProduct(Long id, String productName, double price, Integer status, String description) {
    super();
    this.id = id;
    this.productName = productName;
    this.price = price;
    this.status = status;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


}
