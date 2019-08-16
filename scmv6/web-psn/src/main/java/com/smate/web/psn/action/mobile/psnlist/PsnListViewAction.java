package com.smate.web.psn.action.mobile.psnlist;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;

/**
 * 人员列表显示Action
 *
 * @author wsn
 *
 */
@Results({@Result(name = "psnList", location = "/WEB-INF/jsp/mobile/psnlist/mobile_psn_list.jsp"),
    @Result(name = "mobile_keyword_identify_psn", location = "/WEB-INF/jsp/mobile/homepage/keyword_identify_psn.jsp"),
    @Result(name = "identify_psn_list", location = "/WEB-INF/jsp/mobile/homepage/keyword_identify_psn_list.jsp")})
public class PsnListViewAction extends WechatBaseAction
    implements ModelDriven<PsnListViewForm>, Preparable, Serializable {

  private static final long serialVersionUID = -20940854223370123L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private PsnListViewForm form;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;

  /**
   * 进入人员列表页面
   * 
   * @return
   */
  @Action("/psnweb/mobile/psnlistview")
  public String loadPsnList() {
    try {

      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/mobile/psnlistview" + this.handleRequestParams());
      }
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {// 增加他人主页研究领域人员列表的查询
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      psnListViewService.getPsnListViewInfo(form);
    } catch (Exception e) {
      logger.error("进入人员列表页面出错， psnId=" + form.getPsnId(), e);
    }
    return "psnList";
  }

  /**
   * 移动端获取关键词认同人员列表
   * 
   * @return
   */
  @Action("/psnweb/outside/mobile/ajaxidentifypsn")
  public String getKeywordIdentifyPsnList() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/outside/mobile/identifypsn" + this.handleRequestParams());
      }
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      if (form.getPsnId().longValue() == SecurityUtils.getCurrentUserId().longValue()) {
        form.setIsOthers(0);
      }
      psnListViewService.getPsnListViewInfo(form);
    } catch (Exception e) {
      logger.error("查看人员列表出错， 人员列表类型serviceType=" + form.getServiceType(), e);
    }
    return "identify_psn_list";
  }

  /**
   * 移动端进入关键词认同人员列表
   * 
   * @return
   */
  @Action("/psnweb/outside/mobile/identifypsn")
  public String showKeywordIdentifyPsn() {
    return "mobile_keyword_identify_psn";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnListViewForm();
    }
  }

  @Override
  public PsnListViewForm getModel() {

    return form;
  }
}
