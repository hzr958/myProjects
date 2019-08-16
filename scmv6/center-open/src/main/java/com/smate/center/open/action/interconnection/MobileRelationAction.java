package com.smate.center.open.action.interconnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.interconnection.MobileRelationForm;

/**
 * 移动端关联科研之友
 * 
 * @author aijiangbin
 */
@Results({@Result(name = "mobile_relation", location = "/WEB-INF/jsp/interconnection/mobile_relation.jsp"),
    @Result(name = "mobile_relation_no_authority",
        location = "/WEB-INF/jsp/interconnection/mobile_relation_no_authority.jsp"),})
public class MobileRelationAction extends ActionSupport implements ModelDriven<MobileRelationForm>, Preparable {

  private static final long serialVersionUID = 1L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private MobileRelationForm form;

  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private OpenCacheService openCacheService;

  /**
   * 进入帐号关联页面
   * 
   * @return
   */
  @Action("/open/mobilerelation/show")
  public String showMobileRelation() {
    try {
      if (checkMobileRelationParams()) {
        return "mobile_relation";
      }
    } catch (Exception e) {
      logger.error("进入移动端帐号关联页面出错", e);
    }
    return "mobile_relation_no_authority";
  }

  /**
   * 检查参数
   * 
   * @return
   */
  public boolean checkMobileRelationParams() {
    if (StringUtils.isBlank(form.getToken()) || StringUtils.isBlank(form.getBack())
        || StringUtils.isBlank(form.getType()) || StringUtils.isBlank(form.getZhfirstName())
        || StringUtils.isBlank(form.getZhlastName())) {
      logger.debug("缺少必要的参数");
      return false;
    }
    String key = form.getToken() + "_" + form.getType();
    Object obj = openCacheService.get(OpenConsts.DYN_TOKEN_CACHE, key);
    if (obj != null) {
      // 动态token验证成功，换成正常token
      form.setToken(obj.toString());
      return true;
    }
    logger.debug("动态token失效");
    return false;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new MobileRelationForm();
    }
  }

  @Override
  public MobileRelationForm getModel() {
    return form;
  }

}
