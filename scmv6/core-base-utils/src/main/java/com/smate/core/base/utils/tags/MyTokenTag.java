package com.smate.core.base.utils.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.TokenTag;

import com.opensymphony.xwork2.util.ValueStack;

public class MyTokenTag extends TokenTag {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
    return new MyToken(stack, req, res);
  }

}
