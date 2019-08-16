package com.smate.web.mobile.controller.web.prj;

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
 * 项目操作Controller
 * 
 * @author wsn
 * @date May 17, 2019
 */
@Controller
public class PrjOptController {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;


  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/prj/opt/ajaxaward", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object dealWithPrjAward(PrjOptVO vo) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long prjId = NumberUtils.toLong(Des3Utils.decodeFromDes3(vo.getDes3Id()), 0L);
      if (NumberUtils.isNotNullOrZero(prjId) && vo.getOptType() != null && NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3Id", vo.getDes3Id());
        params.add("resType", "4");
        params.add("optType", vo.getOptType().toString());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_OPT_AWARD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端处理项目赞、取消赞操作异常， prjId={}, optType={}", e);
      result.put("result", "error");
    }
    return result;
  }
}
