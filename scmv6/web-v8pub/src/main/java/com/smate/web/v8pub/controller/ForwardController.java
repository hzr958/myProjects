package com.smate.web.v8pub.controller;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2MoveUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 跳转进入某个URL，需要记录当前URL放入堆栈.
 * 
 * @author zk
 * 
 */
@Controller
public class ForwardController {

  @Autowired
  private HttpServletRequest request;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /*
   * private String forwardUrl; private String ownerUrl; private String switchKey;
   */
  @Value("${domainscm}")
  private String domain;
  private String isEdit;

  /**/

  /**
   * 跳转进入某个URL.
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/forwardUrl")
  public ModelAndView forwardUrl(String ownerUrl, String forwardUrl) throws Exception {
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
   * 用于群组成果编辑 跳转进入某个URL. 因为，这个编辑页，隐藏域复杂了， 为了简单，上一个url 直接从 refer中获取
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/ajaxforwardUrlRefer")
  @ResponseBody
  public String forwardUrlRefer(String forwardUrl) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    String preUrl = request.getHeader("Referer");
    preUrl = HtmlUtils.htmlUnescape(preUrl);
    Struts2MoveUtils.cacheOwnerQueryStringUrl(request, preUrl);
    if (StringUtils.isBlank(forwardUrl) || !forwardUrl.contains("http")) {
      forwardUrl = this.domain + forwardUrl;
    }
    forwardUrl = HtmlUtils.htmlUnescape(forwardUrl);
    map.put("forwardUrl", forwardUrl);
    return JacksonUtils.mapToJsonStr(map);
  }

  /**
   * 返回上一页.
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/backurl")
  public ModelAndView backUrl() throws Exception {
    String forwardUrl;
    HttpSession session = request.getSession();
    String isEdit = request.getParameter("isEdit");
    Stack<String> preUrls = Struts2MoveUtils.getPreUrlStack(session);
    String tempDomain = this.domain;
    String currentDomain = request.getHeader("host");
    if (preUrls == null || preUrls.size() == 0) {// scm-6269 成果保存超时后，返回列表
      if (StringUtils.isNoneBlank(request.getParameter("isPubOverTime"))
          && request.getParameter("isPubOverTime").equals("1")) {

        if (currentDomain.indexOf("nsfc.gov.cn") > -1) {
          currentDomain = currentDomain.substring(0, currentDomain.indexOf(".cn"));
          tempDomain = "http://" + currentDomain + ".cn";
        } else {
          tempDomain = this.domain;
        }
        forwardUrl = tempDomain + "/psnweb/homepage/show?module=pub";
      } else {
        forwardUrl = this.domain + "/psnweb/homepage/show?module=pub";
      }
    } else {
      forwardUrl = Struts2MoveUtils.backPreUrl(session, "/");
      forwardUrl = forwardUrl.replace("onlyImports=0", "onlyImports=1");
      // 如果是编辑成果，则不需要设置来源 isEditPub=false
      if (!forwardUrl.contains("isEditPub=true")) {
        forwardUrl = forwardUrl.replace("importSource=false", "importSource=true");
      }

      Struts2MoveUtils.reomvePreUrl(session);
    }
    if (forwardUrl.contains("?") && !forwardUrl.contains("frompage=edit")) {
      forwardUrl = forwardUrl + "&frompage=edit";
    } else if (!forwardUrl.contains("frompage=edit")) {
      forwardUrl = forwardUrl + "?frompage=edit";
    }
    // 编辑成果2018-11-27
    if (StringUtils.isNotBlank(isEdit) && isEdit.equals("true")) {
      forwardUrl = forwardUrl + "&isEdit=true";
    }
    ModelAndView model = new ModelAndView("pub/forward/forward_switch");
    model.addObject("forwardUrl", forwardUrl);
    return model;
  }
  /**
   * 返回上一页.
   *
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/prj/backurl")
  public ModelAndView backPrjUrl() throws Exception {
    String forwardUrl;
    HttpSession session = request.getSession();
    Stack<String> preUrls = Struts2MoveUtils.getPreUrlStack(session);
    String tempDomain = this.domain;
    String currentDomain = request.getHeader("host");
    if (preUrls == null || preUrls.size() == 0) {
        forwardUrl = this.domain + "/psnweb/homepage/show?module=prj";
    }else{
      forwardUrl = Struts2MoveUtils.backPreUrl(session, "/");
      //新增项目后， 继续编辑 有点问题
      if(forwardUrl.contains("prj/enter")){
        forwardUrl = this.domain + "/psnweb/homepage/show?module=prj";
      }
    }
    if (forwardUrl.contains("?") && !forwardUrl.contains("frompage=prjEdit")) {
      forwardUrl = forwardUrl + "&frompage=prjEdit";
    } else if (!forwardUrl.contains("frompage=prjEdit")) {
      forwardUrl = forwardUrl + "?frompage=prjEdit";
    }
    ModelAndView model = new ModelAndView("pub/forward/forward_switch");
    model.addObject("forwardUrl", forwardUrl);
    return model;
  }

  public String getIsEdit() {
    return isEdit;
  }

  public void setIsEdit(String isEdit) {
    this.isEdit = isEdit;
  }
}
