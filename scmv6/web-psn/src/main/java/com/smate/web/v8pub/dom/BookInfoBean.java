package com.smate.web.v8pub.dom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubBookTypeEnum;

/**
 * 书籍/著作/章节信息
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfoBean extends PubTypeInfoBean implements Serializable {

  private static final long serialVersionUID = 5566962778838780487L;

  private String name = new String(); // 书籍/著作名

  private PubBookTypeEnum type = PubBookTypeEnum.NULL; // 书籍/著作类型 1:专著 2:教科书
                                                       // 3:编著

  private String language = new String(); // 语种

  private String publisher = new String(); // 出版社

  private String ISBN = new String(); // 国际标准图书编号

  private Integer totalPages = 0; // 总页数

  private Integer totalWords = 0; // 总字数

  private String seriesName = new String(); // 丛书名

  private String editors = new String(); // 编辑

  private String chapterNo = new String(); // 章节号

  private String pageNumber = new String(); // 起始页码 或者文章号

  public PubBookTypeEnum getType() {
    return type;
  }

  public void setType(PubBookTypeEnum type) {
    this.type = type;
  }

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

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
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

  public String getSeriesName() {
    return seriesName;
  }

  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
  }

  public String getEditors() {
    return editors;
  }

  public void setEditors(String editors) {
    this.editors = editors;
  }

  public String getChapterNo() {
    return chapterNo;
  }

  public void setChapterNo(String chapterNo) {
    this.chapterNo = chapterNo;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
  }

  @Override
  public String toString() {
    return "BookInfoBean{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", publisher='" + publisher + '\''
        + ", ISBN='" + ISBN + '\'' + ", totalPages='" + totalPages + '\'' + ", totalWords='" + totalWords + '\''
        + ", seriesName='" + seriesName + '\'' + ", editors='" + editors + '\'' + ", chapterNo='" + chapterNo + '\''
        + ", pageNumber='" + pageNumber + '\'' + '}';
  }
}
