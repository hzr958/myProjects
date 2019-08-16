package com.smate.web.mobile.controller.data.dyn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.vo.sns.ShareOperateVO;

/**
 * 分享资源操作接口
 * 
 * @author Administrator
 *
 */
@RestController
public class ShareOperateController {
  private static final Logger logger = LoggerFactory.getLogger(ShareOperateController.class);

  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/dyn/updatesharestatic", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object updateShareStatic(@ModelAttribute ShareOperateVO shareOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    Map<String, String> resultMap = new HashMap<>();
    if (checkParam(shareOperateVO, params)) {
      /*
       * resultMap = restTemplate.postForObject(domainMobile + PubApiInfoConsts.UPDATE_RES_STATIC,
       * JacksonUtils.jsonObjectSerializer(shareOperateVO), Map.class);
       */
      resultMap = restTemplate.postForObject(domainMobile + PubApiInfoConsts.UPDATE_RES_STATIC,
          RestUtils.buildPostRequestEntity(params), Map.class);
    } else {
      resultMap.put("status", "error");
      resultMap.put("errorMsg", "des3ResId或shareType参数异常");
    }
    return AppActionUtils.buildReturnInfo(resultMap, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("status"), "error")),
        Objects.toString(resultMap.get("errorMsg"), ""));
  }

  /**
   * 参数校验
   * 
   * @param param
   * @return
   */
  private boolean checkParam(ShareOperateVO shareOperateVO, MultiValueMap<String, String> params) {
    String des3ResId = shareOperateVO.getDes3ResId();// 加密的资源id
    Integer resType = shareOperateVO.getResType();// 分享的资源类型
    Integer platform = shareOperateVO.getPlatform();// 分享平台 ； 微信；微博；等
    // 见：SharePlatformEnum
    if (StringUtils.isNotBlank(des3ResId) && resType != null && platform != null) {
      if (DynamicConstants.SHAER_RESTYPE_MAP.get(resType) == null) {
        return false;
      }
      params.add("des3ResId", des3ResId);
      params.add("resType", resType.toString());
      params.add("des3PsnId", Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
      params.add("platform", platform.toString());
      return true;
    }
    return false;
  }
}
