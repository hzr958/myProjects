package com.smate.sie.core.base.utils.pub.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 著作/书籍章节
 * 
 * @author sjzhou
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfoBean extends PubTypeInfoBean {

  private String name = new String(); // 书籍/著作名

  private String typeCode = new String(); // 书籍/著作类型 1:专著 2:教科书// 3:编著

  private String typeName = new String(); // 书籍/著作类型 1:专著 2:教科书// 3:编著

  private String languageCode = new String(); // 语种
  private String languageName = new String(); // 语种

  private String publisher = new String(); // 出版社

  private String ISBN = new String(); // 国际标准图书编号

  private String publishStatusCode = new String(); // 状态

  private String publishStatusName = new String();

  private Integer totalWords = 0; // 总字数

  private String startPage = new String(); // 起始页码

  private String endPage = new String();

  private String articleNo = new String();

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

  public String getTypeCode() {
    return typeCode;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getPublishStatusName() {
    return publishStatusName;
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

  public String getPublishStatusCode() {
    return publishStatusCode;
  }

  public void setPublishStatusCode(String publishStatusCode) {
    this.publishStatusCode = publishStatusCode;
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

  public String getLanguageCode() {
    return languageCode;
  }

  public String getLanguageName() {
    return languageName;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  public void setLanguageName(String languageName) {
    this.languageName = languageName;
  }

  @Override
  public String toString() {
    return "BookInfoBean [name=" + name + ", typeCode=" + typeCode + ", typeName=" + typeName + ", languageCode="
        + languageCode + ", languageName=" + languageName + ", publisher=" + publisher + ", ISBN=" + ISBN
        + ", publishStatusCode=" + publishStatusCode + ", publishStatusName=" + publishStatusName + ", totalWords="
        + totalWords + ", startPage=" + startPage + ", endPage=" + endPage + ", articleNo=" + articleNo + "]";
  }

}
