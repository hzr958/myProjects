package com.smate.web.mobile.share.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.vo.SmateShareVO;
import com.smate.web.mobile.utils.RestUtils;

/**
 * 分享资源到群组抽象类
 * 
 * @author wsn
 * @date May 24, 2019
 */
public abstract class ShareToSmateService {
  @Autowired
  private RestTemplate restTemplate;

  /**
   * 公共的参数检查
   * 
   * @return
   */
  boolean commonCheck(SmateShareVO vo) {
    // 分享的资源类型
    if (StringUtils.isBlank(vo.getResType())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_RESTYPE);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_RESTYPE);
      return false;
    }
    // 分享的资源ID
    if (StringUtils.isBlank(vo.getDes3ResId())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_RESID);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_RESID);
      return false;
    }
    // 移动端域名，调用分享接口的时候要
    if (StringUtils.isBlank(vo.getDomainMobile())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_DOMAINMOBILE);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_DOMAINMOBILE);
      return false;
    }
    // 当前登录人员ID
    if (StringUtils.isBlank(vo.getDes3CurrentPsnId())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_CURRENTPSNID);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_CURRENTPSNID);
      return false;
    }
    return true;
  }

  /**
   * 其他的参数检查
   * 
   * @param vo
   * @return
   */
  abstract boolean checkData(SmateShareVO vo);


  /**
   * 分享资源
   * 
   * @param vo
   */
  abstract Map<String, Object> shareRes(SmateShareVO vo);

  /**
   * 其他的一些处理,如同步资源统计数等，但注意不要影响PC端的， PC端很多是分享完后，再发请求进行同步的
   * 
   * @param vo
   */
  abstract void otherDeals(SmateShareVO vo);

  /**
   * 添加分享统计数
   * 
   * @param vo
   */
  @SuppressWarnings("unchecked")
  void updateShareStatistics(SmateShareVO vo) {
    // 构建分享所需参数
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3PsnId", StringUtils.trimToEmpty(vo.getDes3CurrentPsnId()));
    params.add("des3ResId", StringUtils.trimToEmpty(vo.getDes3ResId()));
    params.add("resType",
        Objects.toString(SmateShareConstant.RES_TYPE_MAP.get(StringUtils.trimToEmpty(vo.getResType())), ""));
    params.add("platform", SmateShareConstant.SHARE_TO_PLATFORM_MAP.get(StringUtils.trimToEmpty(vo.getShareTo())));
    params.add("shareText", StringUtils.trimToEmpty(vo.getShareText()));
    // 调用接口更新统计数
    Map<String, Object> resultMap =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.SHARE_UPDATE_STATISTICS_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (resultMap == null || !"success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
      vo.setWarnCode(SmateShareConstant.SHARE_ERROR_CODE_UPDATE_STATISTICS_ERROR);
      vo.setWarnMsg(SmateShareConstant.SHARE_ERROR_MSG_UPDATE_STATISTICS_ERROR);
    }
  }


  /**
   * 执行分享操作过程
   * 
   * @param vo
   */
  public Map<String, Object> doShare(SmateShareVO vo) {
    Map<String, Object> result = new HashMap<String, Object>();
    // 先校验参数
    if (commonCheck(vo) && checkData(vo)) {
      // 进行分享操作
      result = shareRes(vo);
      // 分享操作有问题就不用继续请求更新资源分享数了
      if (StringUtils.isNotBlank(vo.getErrorCode())) {
        vo.setNeedUpdateStatistics(false);
      }
      // 更新资源分享数，暂时分享文件不用更新分享数
      if (vo.getNeedUpdateStatistics() && !SmateShareConstant.SHARE_PSN_FILE_RES_TYPE.equals(vo.getResType())
          && !SmateShareConstant.SHARE_GROUP_FILE_RES_TYPE.equals(vo.getResType())) {
        updateShareStatistics(vo);
      }
      // 分享成功后还可以进行一些其他操作
      if (StringUtils.isBlank(vo.getErrorCode())) {
        otherDeals(vo);
      }
    }
    return result;
  }


}
