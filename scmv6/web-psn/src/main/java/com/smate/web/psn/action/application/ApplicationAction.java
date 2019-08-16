package com.smate.web.psn.action.application;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

@Results({@Result(name = "main", location = "/WEB-INF/jsp/application/app_main.jsp")

})
public class ApplicationAction extends ActionSupport {

  private static final long serialVersionUID = 8528467571979038832L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Action("/psnweb/application/main")
  public String application() {

    return "main";
  }

}
