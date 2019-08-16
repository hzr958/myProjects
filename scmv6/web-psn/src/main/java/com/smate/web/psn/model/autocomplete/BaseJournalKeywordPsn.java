package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊关键词表，存储期刊被各文献库收录的信息.
 */
@Entity
@Table(name = "BASE_JOURNAL_KEYWORD_PSN")
public class BaseJournalKeywordPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5003742805018037860L;
  private PsnJnlKeyId id;
  private Integer frequency;

  @Id
  @AttributeOverrides({@AttributeOverride(name = "psnId", column = @Column(name = "PSN_ID")),
      @AttributeOverride(name = "jnlId", column = @Column(name = "JNL_ID"))})
  public PsnJnlKeyId getId() {
    return id;
  }

  public void setId(PsnJnlKeyId id) {
    this.id = id;
  }

  @Column(name = "FREQUENCY")
  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
