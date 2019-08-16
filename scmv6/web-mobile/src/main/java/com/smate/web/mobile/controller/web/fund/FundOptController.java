package com.smate.web.mobile.controller.web.fund;

import java.util.HashMap;
import java.util.Map;

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

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.prj.vo.PrjOptVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PrjApiConsts;

/**
 * 移动端基金操作Controller
 * 
 * @author wsn
 * @date May 17, 2019
 */
@Controller
public class FundOptController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;



  /**
   * 移动端基金收藏、取消收藏操作
   * 
   * @param vo
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/prj/optfund/ajaxcollect", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object dealWithFundCollect(PrjOptVO vo) {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      Long fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(vo.getDes3FundId()), 0L);
      if (NumberUtils.isNotNullOrZero(fundId) && NumberUtils.isNotNullOrZero(psnId) && vo.getOptType() != null) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("encryptedFundId", vo.getDes3FundId());
        params.add("encryptedPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("collectOperate", vo.getOptType().toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.FUND_OPT_COLLECT,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端处理基金收藏、取消收藏操作异常， fundId={}, optType={}, psnId={}", vo.getDes3FundId(), vo.getOptType(), psnId,
          e);
      result.put("result", "error");
    }
    return result;
  }

}
