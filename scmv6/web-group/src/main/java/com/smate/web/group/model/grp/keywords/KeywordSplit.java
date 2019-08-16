package com.smate.web.group.model.grp.keywords;

import java.io.Serializable;
import java.util.List;

public class KeywordSplit implements Serializable, Comparable<KeywordSplit> {

  /**
   * 
   */
  private static final long serialVersionUID = 9220627464741090843L;
  // 词库ID
  private Long dicId;
  // 关键词
  private String keyword;
  // 格式化后得关键词
  private String kwtxt;
  // 关键词hash
  private Long kwHash;
  // 关键词反序hash
  private Long kwRhash;
  // 关键词字数
  private int wlen;
  // 1中文，2英文
  private Integer type;
  // 在关键词中出现次数
  private int kwNum = 0;
  // 标题中出现次数
  private int titleNum = 0;
  // 摘要中出现次数
  private int absNum = 0;
  // 频率.
  private Integer freq = 0;
  // 权重
  private Double weight = 0D;
  // 热词id
  private Long hotKeyId;
  // 对上的热词列表
  private List<Long> hotKeyIds;
  // 热词相关词列表
  private List<Object[]> hotKwRelList;

  public KeywordSplit() {
    super();
  }

  public KeywordSplit(Long dicId, String keyword, String kwtxt, Long kwHash, Long kwRhash, int wlen, Integer type) {
    super();
    this.dicId = dicId;
    this.keyword = keyword;
    this.kwtxt = kwtxt;
    this.kwHash = kwHash;
    this.kwRhash = kwRhash;
    this.wlen = wlen;
    this.type = type;
  }

  public KeywordSplit(String keyword, String kwtxt, Long kwHash, Long kwRhash, int wlen, Integer type) {
    super();
    this.keyword = keyword;
    this.kwtxt = kwtxt;
    this.kwHash = kwHash;
    this.kwRhash = kwRhash;
    this.wlen = wlen;
    this.type = type;
  }

  public Long getDicId() {
    return dicId;
  }

  public String getKeyword() {
    return keyword;
  }

  public String getKwtxt() {
    return kwtxt;
  }

  public Long getKwHash() {
    return kwHash;
  }

  public Long getKwRhash() {
    return kwRhash;
  }

  public Double getWeight() {
    return weight;
  }

  public void setDicId(Long dicId) {
    this.dicId = dicId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwtxt(String kwtxt) {
    this.kwtxt = kwtxt;
  }

  public void setKwHash(Long kwHash) {
    this.kwHash = kwHash;
  }

  public void setKwRhash(Long kwRhash) {
    this.kwRhash = kwRhash;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Integer getFreq() {
    return freq;
  }

  public void setFreq(Integer freq) {
    this.freq = freq;
  }

  public Long getHotKeyId() {
    return hotKeyId;
  }

  public void setHotKeyId(Long hotKeyId) {
    this.hotKeyId = hotKeyId;
  }

  public List<Object[]> getHotKwRelList() {
    return hotKwRelList;
  }

  public void setHotKwRelList(List<Object[]> hotKwRelList) {
    this.hotKwRelList = hotKwRelList;
  }

  public Integer getType() {
    return type;
  }

  public int getKwNum() {
    return kwNum;
  }

  public int getTitleNum() {
    return titleNum;
  }

  public int getAbsNum() {
    return absNum;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setKwNum(int kwNum) {
    this.kwNum = kwNum;
  }

  public void setTitleNum(int titleNum) {
    this.titleNum = titleNum;
  }

  public void setAbsNum(int absNum) {
    this.absNum = absNum;
  }

  public int getWlen() {
    return wlen;
  }

  public void setWlen(int wlen) {
    this.wlen = wlen;
  }

  public List<Long> getHotKeyIds() {
    return hotKeyIds;
  }

  public void setHotKeyIds(List<Long> hotKeyIds) {
    this.hotKeyIds = hotKeyIds;
  }

  @Override
  public int compareTo(KeywordSplit o) {
    // 先比权重
    if (o.getWeight() > this.getWeight()) {
      return 1;
    } else if (o.getWeight() < this.getWeight()) {
      return -1;
    }
    // 再比长度
    if (o.getWlen() > this.getWlen()) {
      return 1;
    } else if (o.getWlen() < this.getWlen()) {
      return -1;
    }
    return 0;
  }
}
