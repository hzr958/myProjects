package com.smate.core.base.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.number.NumberUtils;


public class DateFormatTag extends BodyTagSupport {
  private static final long serialVersionUID = 1L;
  private String dateStr = "";
  private String splitChar = "";

  public int doStartTag() throws JspException {
    String date = "";

    if (StringUtils.isNotBlank(dateStr)) {
      dateStr = dateStr.trim();
      String dateNumber = dateStr.replaceAll("[-/.]", "");
      if (dateNumber.matches("^\\d+$")) {
        String[] datas = dateStr.split("[-/.]");
        if (datas.length == 2) {
          int num1 = NumberUtils.toInt(datas[0]);
          int num2 = NumberUtils.toInt(datas[1]);
          if (num1 > num2) {
            date = getStrNum(num1) + splitChar + getStrNum(num2);
          } else {
            date = getStrNum(num2) + splitChar + getStrNum(num1);
          }
        }
        if (datas.length == 3) {
          int num1 = NumberUtils.toInt(datas[0]);
          int num2 = NumberUtils.toInt(datas[1]);
          int num3 = NumberUtils.toInt(datas[2]);
          if (num1 > num3) {
            date = getStrNum(num1) + splitChar + getStrNum(num2) + splitChar + getStrNum(num3);
          } else {
            date = getStrNum(num3) + splitChar + getStrNum(num2) + splitChar + getStrNum(num1);
          }
        }
        if (datas.length == 1) {
          date = dateStr;
        }
      }
    }

    try {
      pageContext.getOut().write(date);
    } catch (IOException e) {
    }
    return EVAL_BODY_BUFFERED;
  }

  private String getStrNum(int num) {
    if (num > 0 && num < 10) {
      return "0" + num;
    }
    return num + "";
  }

  public String getDateStr() {
    return dateStr;
  }

  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

  public String getSplitChar() {
    return splitChar;
  }

  public void setSplitChar(String splitChar) {
    this.splitChar = splitChar;
  }

}
