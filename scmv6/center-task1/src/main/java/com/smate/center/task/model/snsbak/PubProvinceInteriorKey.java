package com.smate.center.task.model.snsbak;

import java.io.Serializable;

public class PubProvinceInteriorKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3433800115832598351L;

  private int provinceId;
  private int coopProvinceId;
  private int categoryId;
  private int pubYear;
  private int indexId;

  public PubProvinceInteriorKey(int provinceId, int coopProvinceId, int categoryId, int pubYear, int indexId) {
    super();
    this.provinceId = provinceId;
    this.coopProvinceId = coopProvinceId;
    this.categoryId = categoryId;
    this.pubYear = pubYear;
    this.indexId = indexId;
  }

  public PubProvinceInteriorKey() {
    super();
  }

  public int getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(int provinceId) {
    this.provinceId = provinceId;
  }

  public int getCoopProvinceId() {
    return coopProvinceId;
  }

  public void setCoopProvinceId(int coopProvinceId) {
    this.coopProvinceId = coopProvinceId;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public int getPubYear() {
    return pubYear;
  }

  public void setPubYear(int pubYear) {
    this.pubYear = pubYear;
  }

  @Override
  public int hashCode() {
    String str = this.provinceId + "-" + this.coopProvinceId + "-" + this.categoryId + "-" + this.pubYear;
    return str.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o != null && o instanceof PubProvinceInteriorKey) {
      PubProvinceInteriorKey ppik = (PubProvinceInteriorKey) o;
      return this.provinceId == ppik.getProvinceId() && this.coopProvinceId == ppik.getCoopProvinceId()
          && this.pubYear == ppik.getPubYear() && this.categoryId == ppik.getCategoryId();
    }
    return false;
  }

  public int getIndexId() {
    return indexId;
  }

  public void setIndexId(int indexId) {
    this.indexId = indexId;
  }
}
