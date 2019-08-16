package com.smate.web.mobile.controller.data.dyn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.service.FindResInfoService;
import com.smate.web.mobile.share.service.ShareToSmateService;
import com.smate.web.mobile.share.vo.SmateShareVO;

/**
 * 移动端资源分享控制器
 * 
 * @author wsn
 * @date May 23, 2019
 */
@Controller
public class MobileShareResDataController {

  private static final Logger logger = LoggerFactory.getLogger(MobileShareResDataController.class);
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;
  private Map<String, ShareToSmateService> shareServiceMap;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private ShareToSmateService shareResToDynService;
  @Autowired
  private ShareToSmateService shareResToFriendService;
  @Autowired
  private ShareToSmateService shareResToGrpService;
  @Autowired
  private FindResInfoService findResInfoService;


  /**
   * 移动端资源分享入口 "data/psn/share/res",
   * 
   * @param vo
   * @return
   */
  @RequestMapping(value = "/data/psn/share/res", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object shareResToSmate(SmateShareVO vo) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> result = new HashMap<String, Object>();
    String status = "error";
    try {
      // 需要分享到的模块
      if (StringUtils.isNotBlank(vo.getShareTo()) && shareServiceMap.containsKey(vo.getShareTo())
          && NumberUtils.isNotNullOrZero(psnId)) {
        vo.setDomainMobile(domainMobile);
        vo.setDes3CurrentPsnId(Des3Utils.encodeToDes3(psnId.toString()));
        result = shareServiceMap.get(vo.getShareTo()).doShare(vo);
        status = "success";
      } else {
        vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_CHECK_FALSE);
        vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_CHECK_FALSE);
      }
    } catch (Exception e) {
      logger.error("移动端分享资源异常，psnId={}, resId={}, resType={}, shareTo={}, grpId={}, friendId={}, shareText={}", psnId,
          vo.getDes3ResId(), vo.getResType(), vo.getShareTo(), vo.getDes3GrpId(), vo.getDes3FriendIds(),
          vo.getShareText(), e);
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INTERFACE_ERROR);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INTERFACE_ERROR);
    }
    result.put("result", status);
    result.put("error_code", vo.getErrorCode());
    result.put("error_msg", vo.getErrorMsg());
    result.put("warn_code", vo.getWarnCode());
    result.put("warn_msg", vo.getWarnMsg());
    return AppActionUtils.buildReturnInfo(result, 0,
        AppActionUtils.changeResultStatus(Objects.toString(status, "error")), vo.getErrorMsg());
  }



  /**
   * 执行每个RequestMapping之前都会先执行这个
   */
  @ModelAttribute
  private void initShareServiceMap() {
    if (shareServiceMap == null) {
      shareServiceMap = new HashMap<String, ShareToSmateService>();
      shareServiceMap.put(SmateShareConstant.SHARE_TO_DYNAMIC, shareResToDynService);
      shareServiceMap.put(SmateShareConstant.SHARE_TO_FRIEND, shareResToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_TO_GROUP, shareResToGrpService);
    }
  }
}
