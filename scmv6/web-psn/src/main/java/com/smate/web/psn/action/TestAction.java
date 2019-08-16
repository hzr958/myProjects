package com.smate.web.psn.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Results({@Result(name = "test", location = "/content/test.jsp")})
public class TestAction extends ActionSupport {

  private static final long serialVersionUID = -2700156214728128232L;

  @Action("/psnweb/test")
  public String main() throws Exception {
    LOG.debug("正在进入SNS首页,尚未登录...");
    return "test";

  }

  @Action("/psnweb/testbaseinfo")
  public String testBaseInfo() throws Exception {
    LOG.debug("正在进入SNS首页,尚未登录...");
    return "test";

  }

}
