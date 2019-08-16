package com.smate.web.fund.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.recommend.FundRecommendService;

@RestController
public class InitPsnFundRecommendAction extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {
  @Autowired
  private FundRecommendService fundRecommendService;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FundRecommendForm form;

  /**
   * 初始化人员的基金推荐条件
   * 
   * @param params
   * @return
   */
  @Action("/prjdata/initpsnrecommedfund")
  public Object initPsnRecommedFund() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      String psnIds = form.getPsnIds();
      if (StringUtils.isNotEmpty(psnIds)) {
        String[] psnIdArr = psnIds.split(",");
        for (String psnId : psnIdArr) {
          fundRecommendService.initPsnRecommendFund(NumberUtils.toLong(psnId));
          result.put("status", "success");
        }
      }
    } catch (Exception e) {
      result.put("status", "error");
      logger.error("初始化人员的基金推荐条件出错psnId = {}", form.getPsnId(), e);
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

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
}
