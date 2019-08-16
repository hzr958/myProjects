package com.smate.web.fund.action.pc;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.recommend.FundRecommendService;

/**
 * 个人主页基金推荐
 * 
 * @author wsn
 *
 */
@Results({@Result(name = "recommend_fund", location = "/WEB-INF/jsp/fund/recommend_fund_list.jsp")})
public class HomepageFundRecommendAction extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FundRecommendForm form;
  @Autowired
  private FundRecommendService fundRecommendService;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundRecommendForm();
    }
  }

  @Override
  public FundRecommendForm getModel() {
    return form;
  }

  /**
   * 获取基金推荐列表
   * 
   * @return
   */
  @Action("/prjweb/fundrecommend/ajaxshow")
  public String showFundRecommend() {
    try {
      // boolean isMySelf = isMySelf();
      // 个人主页基金推荐模块，最少显示3跳基金
      if (form.getPageSize() == null || form.getPageSize() == 0) {
        form.setPageSize(3);
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form = fundRecommendService.fundRecommendConditionsShow(form);
      // form = fundRecommendService.fundRecommendListSearch(form);
    } catch (Exception e) {
      logger.error("获取基金推荐模块数据出错，psnId = " + form.getPsnId(), e);
    }
    return "recommend_fund";
  }

  /**
   * 判断是否是本人
   * 
   * @return
   */
  private boolean isMySelf() {
    boolean isSelf = true;
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    if (form.getPsnId() == null || form.getPsnId() == 0) {
      if (form.getEncryptedPsnId() != null) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getEncryptedPsnId())));
      }
      if (form.getPsnId() == null) {
        form.setPsnId(currentPsnId);
      }
    }
    if (currentPsnId != null && currentPsnId != 0) {
      if (currentPsnId.longValue() == form.getPsnId().longValue()) {
        isSelf = true;
      } else {
        isSelf = false;
      }
    }
    form.setIsSelf(isSelf);
    return isSelf;
  }

}
