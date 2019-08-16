package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 著作/书籍章节
 * 
 * @author ZSJ
 *
 * @date 2019年2月11日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfoDTO extends PubTypeInfoDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2464402956388764120L;

  private String name; // 书籍/著作名

  private String typeCode; // 书籍/著作类型 1:专著 2:教科书// 3:编著

  private String typeName; // 书籍/著作类型 1:专著 2:教科书// 3:编著

  private String languageCode; // 语种
  private String languageName;

  private String publisher; // 出版社

  private String ISBN; // 国际标准图书编号

  private String publishStatusCode; // 状态

  private String publishStatusName;

  private Integer totalWords = 0; // 总字数

  private String startPage; // 起始页码

  private String endPage;

  private String articleNo;

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getISBN() {
    return ISBN;
  }

  public void setISBN(String ISBN) {
    this.ISBN = ISBN;
  }

  public Integer getTotalWords() {
    return totalWords;
  }

  public void setTotalWords(Integer totalWords) {
    this.totalWords = totalWords;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getPublishStatusName() {
    return publishStatusName;
  }

  public String getPublishStatusCode() {
    return publishStatusCode;
  }

  public void setPublishStatusCode(String publishStatusCode) {
    this.publishStatusCode = publishStatusCode;
  }

  public String getStartPage() {
    return startPage;
  }

  public String getEndPage() {
    return endPage;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public void setPublishStatusName(String publishStatusName) {
    this.publishStatusName = publishStatusName;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  public String getArticleNo() {
    return articleNo;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  public String getLanguageName() {
    return languageName;
  }

  public void setLanguageName(String languageName) {
    this.languageName = languageName;
  }

  @Override
  public String toString() {
    return "BookInfoBean [name=" + name + ", typeCode=" + typeCode + ", typeName=" + typeName + ", languageCode="
        + languageCode + ", publisher=" + publisher + ", ISBN=" + ISBN + ", publishStatusCode=" + publishStatusCode
        + ", publishStatusName=" + publishStatusName + ", totalWords=" + totalWords + ", startPage=" + startPage
        + ", endPage=" + endPage + ", articleNo=" + articleNo + "]";
  }

}
