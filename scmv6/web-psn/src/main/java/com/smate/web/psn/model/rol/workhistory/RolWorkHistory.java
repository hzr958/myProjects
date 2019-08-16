package com.smate.web.psn.model.rol.workhistory;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 工作经历--------ROL库.
 * 
 * 
 */
@Entity
@Table(name = "PSN_WORK_HISTORY")
public class RolWorkHistory implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1924817833645055332L;
  /**
   * 主健.
   */
  private Long id;
  /**
   * 工作经历id
   */
  private Long workId;

  /**
   * 人员id
   */
  private Long psnId;

  /**
   * 机构id
   */
  private Long insId;

  /**
   * 到现在.
   */
  private Long isActive;

  /**
   * 开始年份.
   */
  private Long fromYear;
  /**
   * 开始月份.
   */
  private Long fromMonth;
  /**
   * 截至年份.
   */
  private Long toYear;
  /**
   * 截至月份.
   */
  private Long toMonth;

  public RolWorkHistory() {

  }

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_WORK_HISTORY", allocationSize = 1)
  public Long getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @return isActive
   */
  @Column(name = "IS_ACTIVE")
  public Long getIsActive() {
    return isActive;
  }

  /**
   * @param isActive
   */
  public void setIsActive(Long isActive) {
    this.isActive = isActive;
  }

  /**
   * @return fromYear
   */
  @Column(name = "FROM_YEAR")
  public Long getFromYear() {
    return fromYear;
  }

  /**
   * @param fromYear
   */
  public void setFromYear(Long fromYear) {
    this.fromYear = fromYear;
  }

  /**
   * @return fromMonth
   */
  @Column(name = "FROM_MONTH")
  public Long getFromMonth() {
    return fromMonth;
  }

  /**
   * @param fromMonth
   */
  public void setFromMonth(Long fromMonth) {
    this.fromMonth = fromMonth;
  }

  /**
   * @return toYear
   */
  @Column(name = "TO_YEAR")
  public Long getToYear() {
    return toYear;
  }

  /**
   * @param toYear
   */
  public void setToYear(Long toYear) {
    this.toYear = toYear;
  }

  /**
   * @return toMonth
   */
  @Column(name = "TO_MONTH")
  public Long getToMonth() {
    return toMonth;
  }

  /**
   * @param toMonth
   */
  public void setToMonth(Long toMonth) {
    this.toMonth = toMonth;
  }

  @Column(name = "WORK_ID")
  public Long getWorkId() {
    return workId;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public static void main(String args[]) {
    boolean isNumber = false;
    // 定义一个boolean值，用来表示是否包含数字
    String str = "房间奥文件aa。";
    // 假设有一个字符串
    char[] ch = str.toCharArray();
    for (int i = 0; i < ch.length; i++) {

      boolean b = (ch[i] + " ").matches("[\u4e00-\u9faf] ");

      System.out.println(b);

    }

    String s =
        "欧阳、太史、端木、上官、司马、东方、独孤、南宫、万俟、闻人、夏侯、诸葛、尉迟、公羊、赫连、澹台、皇甫、宗政、濮阳、公冶、太叔、申屠、公孙、慕容、仲孙、钟离、长孙、宇文、司徒、鲜于、司空、闾丘、子车、亓官、司寇、巫马、公西、颛孙、壤驷、公良、漆雕、乐正、宰 父、谷梁、拓跋、夹谷、轩辕、令狐、段干、百里、呼延、东郭、南门、羊舌、微生、公户、公玉、公仪、梁丘、公仲、公上、公门、公山、公坚、左丘、公伯、西门、公祖、第五、公乘、贯丘、公皙、南荣、东里、东宫、仲长、子书、子桑、即墨、达奚、褚师";
    String[] ary = s.split("、");
    System.out.println(ary.length);

  }

}
