package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.util.List;

/**
 * 论文推荐：关键词匹配所用form.
 */

public class JnlKwForm implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2061507433874094999L;

  /**
   * 1个人研究领域关键词相同次数>=1
   */
  public static final int KW_COUNT = 1;
  /**
   * 2发表期刊
   */
  public static final int PUBLISHED_JNL = 2;

  /**
   * 3或阅读期刊
   */
  public static final int READ_JNL = 3;

  /**
   * 4或合作者发表的期刊相同
   */
  public static final int COOPERATOR_JNL = 1;
  private Long jnlId;
  private String issn;
  // 关键词匹配次数
  private Integer kwtf;
  // 期刊级别，默认为4级[1,2,3,4]
  private Integer grade;
  // 1个人研究领域关键词相同次数>=1，2或发表期刊，3或阅读期刊，4或合作者发表的期刊相同
  private Integer type;
  // 期刊关键词
  private List<String> kws;

  public JnlKwForm() {
    super();
    this.kwtf = 0;
    this.grade = 4;
  }

  public JnlKwForm(Long jnlId, String issn, Integer kwtf) {
    super();
    this.jnlId = jnlId;
    this.issn = issn;
    this.kwtf = kwtf;
  }

  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public Integer getKwtf() {
    return kwtf;
  }

  public void setKwtf(Integer kwtf) {
    this.kwtf = kwtf;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public List<String> getKws() {
    return kws;
  }

  public void setKws(List<String> kws) {
    this.kws = kws;
  }

}
