package com.smate.center.open.model.group;



import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 群组学科关键词表.
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Entity
@Table(name = "GROUP_KEY_DISC")
public class GroupKeyDisc implements Serializable {

  private static final long serialVersionUID = 8429961759125451153L;
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_KEY_DISC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "Category_Id")
  private Long categoryId;// 对应新建的discipline中的second_category_id

  @Column(name = "GROUP_ID")
  private Long groupId;
  // 学科领域
  @Column(name = "DISCIPLINES")
  private String disciplines;
  // 学科代码1
  @Column(name = "DISCIPLINE_1")
  private Long discipline1;

  // 学到代码
  @Column(name = "DISC_CODES")
  private String discCodes;

  // =====================中文关键字===================
  // 关键字1
  @Column(name = "KEY_WORDS_1")
  private String keyWords1;
  @Column(name = "KEY_WORDS")
  private String keyWords;

  @Transient
  private List<String[]> kewWordsList;
  // ==================================================

  // =====================英文关键字===================
  // 英文关键字1
  @Column(name = "EN_KEY_WORDS_1")
  private String enKeyWords1;
  @Column(name = "EN_KEY_WORDS")
  private String enKeyWords;
  // 英文关键字集
  @Transient
  private List<String[]> enKeyWordsList;

  // ==================================================
  public Long getGroupId() {
    return groupId;
  }

  public GroupKeyDisc() {
    super();
  }

  public GroupKeyDisc(Long id, Long groupId, String disciplines, Long discipline1, String discCodes, String keyWords1,
      String keyWords, List<String[]> kewWordsList, String enKeyWords1, String enKeyWords,
      List<String[]> enKeyWordsList) {
    super();
    this.id = id;
    this.groupId = groupId;
    this.disciplines = disciplines;
    this.discipline1 = discipline1;
    this.discCodes = discCodes;
    this.keyWords1 = keyWords1;
    this.keyWords = keyWords;
    this.kewWordsList = kewWordsList;
    this.enKeyWords1 = enKeyWords1;
    this.enKeyWords = enKeyWords;
    this.enKeyWordsList = enKeyWordsList;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public Long getDiscipline1() {
    return discipline1;
  }

  public void setDiscipline1(Long discipline1) {
    this.discipline1 = discipline1;
  }

  public String getKeyWords1() {
    return keyWords1;
  }

  public void setKeyWords1(String keyWords1) {
    this.keyWords1 = keyWords1;
  }

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public List<String[]> getKewWordsList() {
    return kewWordsList;
  }

  public void setKewWordsList(List<String[]> kewWordsList) {
    this.kewWordsList = kewWordsList;
  }

  public String getEnKeyWords1() {
    return enKeyWords1;
  }

  public void setEnKeyWords1(String enKeyWords1) {
    this.enKeyWords1 = enKeyWords1;
  }

  public String getEnKeyWords() {
    return enKeyWords;
  }

  public void setEnKeyWords(String enKeyWords) {
    this.enKeyWords = enKeyWords;
  }

  public List<String[]> getEnKeyWordsList() {
    return enKeyWordsList;
  }

  public void setEnKeyWordsList(List<String[]> enKeyWordsList) {
    this.enKeyWordsList = enKeyWordsList;
  }

  public String getDiscCodes() {
    return discCodes;
  }

  public void setDiscCodes(String discCodes) {
    this.discCodes = discCodes;
  }
}
