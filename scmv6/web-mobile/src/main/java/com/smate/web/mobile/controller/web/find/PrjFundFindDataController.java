package com.smate.web.mobile.controller.web.find;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.prj.vo.FundOperateVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PrjApiConsts;

/**
 * @ClassName PrjFundFindDataController
 * @Description 基金发现
 * @Author YWL
 * @Date 2018/12/20
 * @Version v1.0
 */

@Controller
public class PrjFundFindDataController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;

  /**
   * 基金发现地区筛选
   */
  @RequestMapping(value = "/prj/mobile/fundfindregion", produces = "application/json;charset=UTF-8")
  public ModelAndView showAgencycondition(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L) {
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDFIND_REGION,
            RestUtils.buildPostRequestEntity(params), Map.class);
        model.addObject("regionList", result.get("regionList"));
        model.setViewName("/prj/fundfind/wechat_fundfind_regioncondition");
      }
    } catch (Exception e) {
      logger.error("mobile资助机构地区筛选列表异常,psnId=" + psnId, e);
    }
    return model;
  }

  /**
   * 基金发现界面
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = "/prj/mobile/fundfindmain")
  public ModelAndView showFundAgencyList(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    model.addObject("searchAreaCodes", prjOperateVO.getSearchAreaCodes());
    model.addObject("regionCodesSelect", prjOperateVO.getSearchRegionCodes());
    model.addObject("seniorityCodeSelect", prjOperateVO.getSearchseniority());
    model.addObject("scienceCodesSelect", prjOperateVO.getScienceCodesSelect());
    model.addObject("searchKey", prjOperateVO.getSearchKey());
    model.setViewName("/prj/fundfind/wechat_fundfind_main");
    return model;
  }

  /**
   * 基金发现条件检索
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = "/prj/mobile/findfundcondition")
  public ModelAndView showFundFindCondition(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    model.addObject("searchAreaCodes", prjOperateVO.getSearchAreaCodes());
    model.addObject("regionCodesSelect", prjOperateVO.getSearchRegionCodes());
    model.addObject("seniorityCodeSelect", prjOperateVO.getSearchseniority());
    model.addObject("scienceCodesSelect", prjOperateVO.getScienceCodesSelect());
    model.addObject("searchKey", prjOperateVO.getSearchKey());
    model.setViewName("/prj/fundfind/wechat_fundfind_conditions");
    return model;
  }

  /**
   * 基金发现列表
   */
  @RequestMapping(value = "/prj/mobile/fundfindlist", produces = "application/json;charset=UTF-8")
  public ModelAndView ajaxShowFundFindList(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        params.add("psnId", psnId.toString());
        params.add("regionCodesSelect", prjOperateVO.getSearchRegionCodes());
        params.add("seniorityCodeSelect", prjOperateVO.getSearchseniority());
        params.add("scienceCodesSelect", prjOperateVO.getScienceCodesSelect());
        params.add("searchKey", prjOperateVO.getSearchKey());
        String pageNum = prjOperateVO.getPage().getParamPageNo().toString();
        params.add("pageNum", pageNum);
        params.add("pageSize", "10");
        result =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDFIND_FUNDFINDLIST,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if ("success".equals(result.get("status"))) {
          BeanUtils.copyProperties(prjOperateVO, result.get("result"));
          prjOperateVO.setPageNum(pageNum);
          model.addObject("resultList", result.get("resultList"));
          model.addObject("prjOperateVO", prjOperateVO);
        }
        model.setViewName("/prj/fundfind/wechat_fundfind_sub");
      }
    } catch (Exception e) {
      logger.error("mobile基金发现列表异常,psnId=" + psnId, e);
    }
    return model;
  }

}
