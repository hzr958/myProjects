package com.smate.center.task.model.snsbak;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BDSP_COOP_CITY_INTERNTIONAL")
public class BdspCoopCityInternational implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2557981527450653564L;
  private Long id;
  private Integer indexId;
  private Integer pubYear;
  private Integer categoryId;
  private Integer cityId;
  private Integer countryId;
  private Integer count;
  private Date updateTime;

  public BdspCoopCityInternational() {
    super();
  }

  public BdspCoopCityInternational(Integer indexId, Integer pubYear, Integer categoryId, Integer cityId,
      Integer countryId, Integer count, Date updateTime) {
    super();
    this.indexId = indexId;
    this.pubYear = pubYear;
    this.categoryId = categoryId;
    this.cityId = cityId;
    this.countryId = countryId;
    this.count = count;
    this.updateTime = updateTime;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_READ_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INDEX_ID")
  public Integer getIndexId() {
    return indexId;
  }

  public void setIndexId(Integer indexId) {
    this.indexId = indexId;
  }

  @Column(name = "YEAR_ID")
  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  @Column(name = "TECHFILED_ID")
  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  @Column(name = "CITY_ID")
  public Integer getCityId() {
    return cityId;
  }

  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  @Column(name = "COUNTRY_ID")
  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  @Column(name = "RESULT")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
