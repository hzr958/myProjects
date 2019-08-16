package com.smate.core.base.utils.tags;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Token;
import org.apache.struts2.views.annotations.StrutsTag;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "myToken", tldTagClass = "com.iris.scm.scmweb.web.struts2.tag.MyTokenTag",
    description = "Stop double-submission of forms")
public class MyToken extends Token {

  public MyToken(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
    super(stack, request, response);
  }

}
