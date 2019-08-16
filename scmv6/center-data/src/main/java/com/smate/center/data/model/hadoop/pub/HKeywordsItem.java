package com.smate.center.data.model.hadoop.pub;

/**
 * 关键词推荐form
 * 
 * @author lhd
 *
 */
public class HKeywordsItem {

  private String key1;// 单个关键词
  private double key1Val = 0;// 单个关键词次数
  private String key1Sha1;// 加密的单个关键词
  private String key2;// 单个关键词
  private double key2Val = 0;// 单个关键词次数
  private String key2Sha1;// 加密的单个关键词
  private String key12Sha1;// 加密的组合关键词
  private double key12Val = 0;// 组合关键词次数
  private double key12Jaccard = 0;// (AB/A+B-AB) 公式值
  private String cId = "";// 学部id,存在多个用;分割

  public String getKey1() {
    return key1;
  }

  public void setKey1(String key1) {
    this.key1 = key1;
  }

  public double getKey1Val() {
    return key1Val;
  }

  public void setKey1Val(double key1Val) {
    this.key1Val = key1Val;
  }

  public String getKey1Sha1() {
    return key1Sha1;
  }

  public void setKey1Sha1(String key1Sha1) {
    this.key1Sha1 = key1Sha1;
  }

  public String getKey2() {
    return key2;
  }

  public void setKey2(String key2) {
    this.key2 = key2;
  }

  public double getKey2Val() {
    return key2Val;
  }

  public void setKey2Val(double key2Val) {
    this.key2Val = key2Val;
  }

  public String getKey2Sha1() {
    return key2Sha1;
  }

  public void setKey2Sha1(String key2Sha1) {
    this.key2Sha1 = key2Sha1;
  }

  public String getKey12Sha1() {
    return key12Sha1;
  }

  public void setKey12Sha1(String key12Sha1) {
    this.key12Sha1 = key12Sha1;
  }

  public double getKey12Val() {
    return key12Val;
  }

  public void setKey12Val(double key12Val) {
    this.key12Val = key12Val;
  }

  public double getKey12Jaccard() {
    return key12Jaccard;
  }

  public void setKey12Jaccard(double key12Jaccard) {
    this.key12Jaccard = key12Jaccard;
  }

  public String getcId() {
    return cId;
  }

  public void setcId(String cId) {
    this.cId = cId;
  }

}
