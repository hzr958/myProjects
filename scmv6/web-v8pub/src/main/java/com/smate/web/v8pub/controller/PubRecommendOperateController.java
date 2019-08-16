package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.sns.PubRecommendService;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

@Controller
public class PubRecommendOperateController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private PubRecommendService pubRecommendService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/recm/ajaxuninterested", method = RequestMethod.POST)
  @ResponseBody
  public String notViewPubRecommend(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (psnId > 0) {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + "/data/pub/opt/uninterestedremdpub", pubOperateVO, Map.class);
      }
    } catch (Exception e) {
      map.put("errmsg", "error");
      logger.error("首页动态推荐论文不感兴趣操作出错,psnId=" + psnId + ",pubId=" + pubOperateVO.getPubId(), e);
    }
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }
}
