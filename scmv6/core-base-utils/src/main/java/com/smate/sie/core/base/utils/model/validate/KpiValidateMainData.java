package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_VALIDATE_MAIN_DATA")
public class KpiValidateMainData implements Serializable {


  private static final long serialVersionUID = 1961889422357819095L;

  /* 生成凭证号，表主键 */
  @Id
  @Column(name = "UUID")
  private String uuId;

  /* 待验证数据 */
  @Column(name = "DATA")
  private String data;

  public KpiValidateMainData() {
    super();
  }

  public KpiValidateMainData(String uuId) {
    super();
    this.uuId = uuId;
  }


  public KpiValidateMainData(String uuId, String data) {
    super();
    this.uuId = uuId;
    this.data = data;
  }


  public String getUuId() {
    return uuId;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }


}
