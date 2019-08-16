package com.smate.web.prj.action.project;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.core.base.utils.struts2.Struts2MoveUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Stack;

/**
 * 跳转进入某个URL，需要记录当前URL放入堆栈.
 * 
 * @author zk
 * 
 */
@Controller
public class ForwardAction extends ActionSupport {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domain;
  private String forwardUrl;

  /**/

  /**
   * 跳转进入某个URL.
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/prj/forwardUrl")
  public ModelAndView forwardUrl(String ownerUrl, String forwardUrl) throws Exception {
    HttpServletRequest request = Struts2Utils.getRequest();
    Struts2MoveUtils.cacheOwnerQueryStringUrl(request, ownerUrl);
    String currentDomain = request.getHeader("host");
    forwardUrl = this.domain + forwardUrl;
    if (currentDomain.indexOf("nsfc.gov.cn") > -1) {
      currentDomain = currentDomain.substring(0, currentDomain.indexOf(".cn"));
      forwardUrl = "http://" + currentDomain + ".cn" + forwardUrl;

    }
    ModelAndView model = new ModelAndView("/pub/forward/forward_switch");
    model.addObject("forwardUrl", forwardUrl);
    return model;
  }


  /**
   * 返回上一页.
   * 
   * @return
   * @throws Exception
   */
  @Action ("/prj/backurl")
  public String backUrl() throws Exception {
    HttpServletRequest request = Struts2Utils.getRequest();
    HttpSession session = request.getSession();
    Stack<String> preUrls = Struts2MoveUtils.getPreUrlStack(session);
    forwardUrl = this.domain + "/psnweb/homepage/show?module=pub";
    return "backurl";
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }
}
