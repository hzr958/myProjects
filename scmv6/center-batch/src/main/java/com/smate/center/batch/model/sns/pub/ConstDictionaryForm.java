package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.List;

/**
 * 常量编辑.
 * 
 * @author liqinghua
 * 
 */
public class ConstDictionaryForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8671968131456547296L;

  private List<ConstCategory> categoryList;
  private List<ConstDictionary> dictionaryList;
  private String dictionary;
  private String category;
  private String code;
  private String cnName;
  private String twName;
  private String enName;
  private String seqNo;
  private String description;
  private String addResult;

  public String getCnName() {
    return cnName;
  }

  public String getTwName() {
    return twName;
  }

  public String getEnName() {
    return enName;
  }

  public void setCnName(String cnName) {
    this.cnName = cnName;
  }

  public void setTwName(String twName) {
    this.twName = twName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getDictionary() {
    return dictionary;
  }

  public String getCategory() {
    return category;
  }

  public String getCode() {
    return code;
  }

  public String getSeqNo() {
    return seqNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDictionary(String dictionary) {
    this.dictionary = dictionary;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ConstCategory> getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(List<ConstCategory> categoryList) {
    this.categoryList = categoryList;
  }

  public List<ConstDictionary> getDictionaryList() {
    return dictionaryList;
  }

  public void setDictionaryList(List<ConstDictionary> dictionaryList) {
    this.dictionaryList = dictionaryList;
  }

  public String getAddResult() {
    return addResult;
  }

  public void setAddResult(String addResult) {
    this.addResult = addResult;
  }

}
