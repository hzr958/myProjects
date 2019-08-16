package com.smate.web.mobile.controller.web.psn;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
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

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.psn.vo.PsnOperateVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PsnApiConsts;

/**
 * 人员操作控制器
 * 
 * @author wsn
 * @date May 20, 2019
 */
@Controller
public class MobilePsnOptController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;


  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/friend/ajaxadd")
  @ResponseBody
  public Object ajaxDealWithDynAward(PsnOperateVO psnOperateVO) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(psnOperateVO.getReqFriendIds())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3Id", psnOperateVO.getReqFriendIds());
        params.add("des3PsnId", Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.PSN_REQ_FRIEND,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("进入移动端群组详情异常，friendIds={}, currentPsnId={}", psnOperateVO.getReqFriendIds(),
          SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    return result;
  }
}
