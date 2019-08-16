package com.smate.web.mobile.controller.data.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.group.vo.GroupPubVO;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;

@RestController
public class GrpPubDataController {
  final protected Logger logger = LoggerFactory.getLogger(GrpPubDataController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 获取群组成果列表
   * 
   * @param pubVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/grppublist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object grpPubList(GroupPubVO pubVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      MultiValueMap<String, Object> params = getGrpPubListParam(pubVO);// 获取参数
      result = postData(params, GrpApiConsts.GRP_PUB_LIST);
    } catch (Exception e) {
      logger.error("获取推荐群组列表出错,psnid={}", psnId, e);
    }
    String status = Objects.toString(result.get("status"));
    if ("200".equals(status)) {
      return result;
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, status, Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 构造群组成果列表接口参数
   * 
   * @param pubVO
   * @return
   */
  private MultiValueMap<String, Object> getGrpPubListParam(GroupPubVO pubVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3GrpId", pubVO.getDes3GrpId());
    params.add("showPrjPub", pubVO.getShowPrjPub());
    params.add("showRefPub", pubVO.getShowRefPub());
    params.add("searchKey", pubVO.getSearchKey());
    params.add("publishYear", pubVO.getPublishYear());
    params.add("pubType", pubVO.getPubType());
    params.add("includeType", pubVO.getIncludeType());// 收录类型
    params.add("orderBy", pubVO.getOrderBy());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", pubVO.getPage().getParamPageNo().toString());
    return params;
  }

  public HashMap<String, Object> postData(MultiValueMap<String, Object> params, String url) throws Exception {
    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + url, params, Map.class);
    return result;
  }
}
