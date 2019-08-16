package com.smate.web.mobile.controller.data.fund;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.prj.vo.FundOperateVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PrjApiConsts;

/**
 * @ClassName APPFundFindController
 * @Description 基金发现APP接口
 * @Author YWL
 * @Date 2018/12/20
 * @Version v1.0
 */

@Controller
public class APPFundFindController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private HttpServletRequest request;

  /**
   * 基金发现地区条件筛选
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/fundfindregion", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAgencycondition(FundOperateVO prjOperateVO) {
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
      }
    } catch (Exception e) {
      logger.error("mobile资助机构地区筛选列表异常,psnId=" + psnId, e);
    }
    if (result != null) {
      return AppActionUtils.buildReturnInfo(result.get("regionList"), (Integer) result.get("totalCount"),
          AppActionUtils.changeResultStatus("success"), Objects.toString(result.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 基金发现--基金列表
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/fundfindlist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getagencydetail(FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L) {
        if (NumberUtils.isNotNullOrZero(psnId)) {
          params.add("psnId", psnId.toString());
          params.add("regionCodesSelect", prjOperateVO.getSearchRegionCodes());
          params.add("seniorityCodeSelect", prjOperateVO.getSearchseniority());
          params.add("scienceCodesSelect", prjOperateVO.getScienceCodesSelect());
          params.add("searchKey", prjOperateVO.getSearchKey());
          String pageNum = prjOperateVO.getPage().getParamPageNo().toString();
          params.add("pageNum", pageNum);
          params.add("pageSize", "10");
          result = (HashMap<String, Object>) restTemplate.postForObject(
              domainMobile + PrjApiConsts.PRJ_FUNDFIND_FUNDFINDLIST, RestUtils.buildPostRequestEntity(params),
              Map.class);
          BeanUtils.copyProperties(prjOperateVO, result.get("result"));
        }
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    if ("success".equals(result.get("status"))) {
      return AppActionUtils.buildReturnInfo(result.get("resultList"), (Integer) prjOperateVO.getTotalCount(),
          AppActionUtils.changeResultStatus("success"), Objects.toString(result.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(result.get("errmsg"), ""));
    }
  }

}
