package com.smate.core.web.sns.action.tags;

import java.io.IOException;
import java.util.StringJoiner;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

/**
 * 拼接人员单位信息
 * 
 * @author wsn
 * @date May 15, 2019
 */
public class ShowPsnInsInfoTag extends BodyTagSupport {

  // 单位名称
  private String insName;
  // 部门
  private String department;
  // 职称
  private String position;
  // 拼接字符
  private String character;


  public int doStartTag() throws JspException {
    String insInfo = joinInsInfo(character, insName, department, position);
    try {
      pageContext.getOut().write(insInfo);
    } catch (IOException e) {
    }
    return EVAL_BODY_BUFFERED;
  }


  private String joinInsInfo(String character, String first, String second, String third) {
    StringJoiner dateJoiner = new StringJoiner(character);
    if (StringUtils.isNotBlank(first)) {
      dateJoiner.add(first.trim());
    }
    if (StringUtils.isNotBlank(second)) {
      dateJoiner.add(second.trim());
    }
    if (StringUtils.isNotBlank(third)) {
      dateJoiner.add(third.trim());
    }
    return dateJoiner.toString();
  }


  public String getInsName() {
    return insName;
  }


  public void setInsName(String insName) {
    this.insName = insName;
  }


  public String getDepartment() {
    return department;
  }


  public void setDepartment(String department) {
    this.department = department;
  }


  public String getPosition() {
    return position;
  }


  public void setPosition(String position) {
    this.position = position;
  }


  public String getCharacter() {
    return character;
  }


  public void setCharacter(String character) {
    this.character = character;
  }



}
