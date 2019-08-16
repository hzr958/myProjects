package com.smate.core.web.sns.action.tags;

import java.io.IOException;
import java.util.StringJoiner;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

/**
 * 拼接开始结束时间
 * 
 * @author wsn
 * @date May 15, 2019
 */
public class StartToEndDateFormateTag extends BodyTagSupport {

  // 开始年份
  private String startYear;
  // 开始月份
  private String startMonth;
  // 开始日
  private String startDay;
  // 结束年份
  private String endYear;
  // 结束月份
  private String endMonth;
  // 结束日
  private String endDay;
  // 年月日时间连接符
  private String dateCharacter;
  // 开始结束时间的连接符
  private String character;


  public int doStartTag() throws JspException {
    String startDate = joinDate(dateCharacter, startYear, startMonth, startDay);
    String endDate = joinDate(dateCharacter, endYear, endMonth, endDay);
    String dateStr = joinDate(character, startDate, endDate, "");
    try {
      pageContext.getOut().write(dateStr);
    } catch (IOException e) {
    }
    return EVAL_BODY_BUFFERED;
  }


  private String joinDate(String character, String first, String second, String third) {
    StringJoiner dateJoiner = new StringJoiner(character);
    if (StringUtils.isNotBlank(first)) {
      dateJoiner.add(first.trim());
      if (StringUtils.isNotBlank(second)) {
        dateJoiner.add(second.trim());
        if (StringUtils.isNotBlank(third)) {
          dateJoiner.add(third.trim());
        }
      }
    }
    return dateJoiner.toString();
  }


  public String getStartYear() {
    return startYear;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public String getStartMonth() {
    return startMonth;
  }

  public void setStartMonth(String startMonth) {
    this.startMonth = startMonth;
  }

  public String getStartDay() {
    return startDay;
  }

  public void setStartDay(String startDay) {
    this.startDay = startDay;
  }

  public String getEndYear() {
    return endYear;
  }

  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }

  public String getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(String endMonth) {
    this.endMonth = endMonth;
  }

  public String getEndDay() {
    return endDay;
  }

  public void setEndDay(String endDay) {
    this.endDay = endDay;
  }

  public String getDataCharacter() {
    return dateCharacter;
  }

  public void setDateCharacter(String dateCharacter) {
    this.dateCharacter = dateCharacter;
  }

  public String getCharacter() {
    return character;
  }

  public void setCharacter(String character) {
    this.character = character;
  }


}
