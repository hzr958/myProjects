package com.smate.web.management.action.mail;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.mail.IrisszMailDetail;
import com.smate.web.management.service.mail.IrisszMailDetailService;

import net.sf.json.JSONObject;

public class IrisszMailDetailAction extends ActionSupport implements ModelDriven<IrisszMailDetail>, Preparable {
  private static final long serialVersionUID = 5613391078556341820L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private IrisszMailDetail irisszMailDetail;
  @Autowired
  private IrisszMailDetailService irisszMailDetailService;

  /**
   * 公司官网 申请试用功能
   * 
   * @return
   */
  @Action("/scmmanagement/newirismail")
  public String irisMail() {
    try {
      irisszMailDetailService.sendIrisMailDetail(irisszMailDetail);
      HttpServletResponse response = ServletActionContext.getResponse();
      response.setContentType("text/javascript");
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      Map<String, String> map = new HashMap<String, String>();
      map.put("result", "true");
      PrintWriter out = response.getWriter();
      JSONObject resultJSON = JSONObject.fromObject(map); // 根据需要拼装json
      String jsonpCallback = Struts2Utils.getParameter("jsonpCallback");// 客户端请求参数
      out.println(jsonpCallback + "(" + resultJSON.toString(1, 1) + ")");// 返回jsonp格式数据
      out.flush();
      out.close();
    } catch (Exception e) {
      logger.error("======公司官网 申请试用功能联系我们邮件出错", e);
    }
    return null;
  }


  @Override
  public void prepare() throws Exception {
    irisszMailDetail = new IrisszMailDetail();
  }


  @Override
  public IrisszMailDetail getModel() {
    return irisszMailDetail;
  }


}

