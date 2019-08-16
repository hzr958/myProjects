package com.smate.web.management.model.institution.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位经纬度.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "CONST_INS_COORDINATE")
public class ConstInsCoordinate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3758848781241098170L;
  // 主键机构ID
  private Long insId;
  // 经度
  private String longitude;
  // 纬度
  private String latitude;

  public ConstInsCoordinate() {
    super();
  }

  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "LONGITUDE")
  public String getLongitude() {
    return longitude;
  }

  @Column(name = "LATITUDE")
  public String getLatitude() {
    return latitude;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }
}
