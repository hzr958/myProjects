package com.smate.web.mobile.controller.data.prj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.vo.PrjListVO;


/**
 * 编辑代表项目页面
 * 
 * @author ltl
 *
 */
@Controller
public class RepresentPrjDataController {
  final protected Logger logger = LoggerFactory.getLogger(RepresentPrjDataController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 保存代表项目
   * 
   * @return
   */
  @RequestMapping(value = "/data/prj/represent/saverepresentprj", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object saveRepresentRrj(PrjListVO prjListVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    String addToRepresentPrjIds = prjListVO.getAddToRepresentPrjIds();
    if (psnId > 0 && StringUtils.isNotBlank(addToRepresentPrjIds) && addToRepresentPrjIds.split(",").length <= 5) {
      String representUrl = PubApiInfoConsts.REPRESENT_SAVE_PRJ;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));

      param.add("addToRepresentPrjIds", addToRepresentPrjIds);
      resultMap = postFormUrl(param, representUrl);// 调接口查询代表项目
    } else {
      resultMap.put("result", "error");
      resultMap.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(null, null,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("result"), "error")),
        Objects.toString(resultMap.get("errmsg"), ""));
  }

  /**
   * 项目列表条件设置页面(包括项目和代表项目添加的)
   * 
   * @return
   */
  @RequestMapping(value = "/data/prj/enterprjcondition", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object enterAddRrjCondition(PrjListVO prjListVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    List<Map<String, Object>> resultList = new ArrayList<>();
    String searchDes3PsnId = prjListVO.getSearchDes3PsnId();
    if (StringUtils.isBlank(searchDes3PsnId)) {
      searchDes3PsnId = Des3Utils.encodeToDes3(psnId.toString());
    }
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("searchDes3PsnId", searchDes3PsnId);
    String representUrl = PubApiInfoConsts.REPRESENT_PRJ_CONDITION_AGENCY;
    resultMap = postFormUrl(param, representUrl);// 调接口查询代表项目资助机构
    return AppActionUtils.buildReturnInfo(resultMap.get("resultList"), 0, AppActionUtils.changeResultStatus("success"),
        Objects.toString(resultMap.get("errmsg"), ""));
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> postJsonUrl(MultiValueMap param, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return (Map<String, Object>) restTemplate.postForObject(domainMobile + url, HttpEntity, Object.class);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> postFormUrl(MultiValueMap param, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return (Map<String, Object>) restTemplate.postForObject(domainMobile + url, HttpEntity, Object.class);
  }
}
