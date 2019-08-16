package com.smate.center.data.model.pub;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class JiangxiPubProvinceInteriorKey implements WritableComparable<JiangxiPubProvinceInteriorKey> {

  private Integer provinceId;
  private Integer coopProvinceId;
  private Integer scmCategoryId;
  private Integer pubYear;

  public Integer getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Integer provinceId) {
    this.provinceId = provinceId;
  }

  public Integer getCoopProvinceId() {
    return coopProvinceId;
  }

  public void setCoopProvinceId(Integer coopProvinceId) {
    this.coopProvinceId = coopProvinceId;
  }

  public Integer getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Integer scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public JiangxiPubProvinceInteriorKey() {

  }

  public JiangxiPubProvinceInteriorKey(Integer provinceId, Integer coopProvinceId, Integer scmCategoryId,
      Integer pubYear) {
    this.provinceId = provinceId;
    this.coopProvinceId = coopProvinceId;
    this.scmCategoryId = scmCategoryId;
    this.pubYear = pubYear;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this.provinceId = in.readInt();
    this.coopProvinceId = in.readInt();
    this.scmCategoryId = in.readInt();
    this.pubYear = in.readInt();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.provinceId);
    out.writeInt(this.coopProvinceId);
    out.writeInt(this.scmCategoryId);
    out.writeInt(this.pubYear);
  }

  // 按省份，合作省份，合作年份排序
  @Override
  public int compareTo(JiangxiPubProvinceInteriorKey o) {
    final Integer prs = this.provinceId - o.provinceId;
    if (prs != 0) {
      return prs;
    }
    final Integer cprs = this.coopProvinceId - o.coopProvinceId;
    if (cprs != 0) {
      return cprs;
    }
    final Integer scrs = this.pubYear - o.pubYear;
    if (scrs != 0) {
      return scrs;
    }
    return this.scmCategoryId - o.scmCategoryId;
  }

  @Override
  public int hashCode() {
    String str = this.provinceId + "-" + this.coopProvinceId + "-" + this.pubYear + "-" + this.scmCategoryId;
    return str.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof JiangxiPubProvinceInteriorKey) {
      JiangxiPubProvinceInteriorKey nO = (JiangxiPubProvinceInteriorKey) o;
      return nO.provinceId == this.provinceId && nO.coopProvinceId == this.coopProvinceId
          && nO.scmCategoryId == this.scmCategoryId && nO.pubYear == this.pubYear;
    }
    return false;
  }
}
