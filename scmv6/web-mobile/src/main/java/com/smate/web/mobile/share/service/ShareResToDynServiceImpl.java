package com.smate.web.mobile.share.service;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.vo.SmateShareVO;
import com.smate.web.mobile.utils.RestUtils;

/**
 * 分享资源到首页动态
 * 
 * @author wsn
 * @date May 24, 2019
 */
@Service("shareResToDynService")
@Transactional(rollbackFor = Exception.class)
public class ShareResToDynServiceImpl extends ShareToSmateService {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private GrpDynShareService grpDynShareService;

  @Override
  boolean checkData(SmateShareVO vo) {
    if (vo.getOperatorType() == null) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INTERFACE_ERROR);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INTERFACE_ERROR);
      return false;
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  Map<String, Object> shareRes(SmateShareVO vo) {
    MultiValueMap<String, String> params = buildShareParams(vo);
    Map<String, Object> resultMap =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.SHARE_TO_DYN_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (resultMap == null || !"success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INTERFACE_ERROR);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INTERFACE_ERROR);
    }
    return resultMap;
  }

  private MultiValueMap<String, String> buildShareParams(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("dynType", StringUtils.isNotBlank(vo.getShareText()) ? "ATEMP" : "B2TEMP");
    params.add("dynText", StringUtils.trimToEmpty(vo.getShareText()));
    params.add("des3ResId", StringUtils.trimToEmpty(vo.getDes3ResId()));
    params.add("resType", SmateShareConstant.RES_TYPE_MAP.get(StringUtils.trimToEmpty(vo.getResType())).toString());
    params.add("operatorType", StringUtils.trimToEmpty(vo.getOperatorType().toString())); // operateType不太清楚有什么用
    params.add("des3psnId", StringUtils.trimToEmpty(vo.getDes3CurrentPsnId()));
    params.add("resInfoJson", StringUtils.trimToEmpty(vo.getResInfoJson()));
    return params;
  }

  @Override
  void otherDeals(SmateShareVO vo) {
    // 如果是群组动态那里点击的分享，则要更新记录群组动态分享信息
    grpDynShareService.updateGrpDynShareStatistics(vo);
  }

}
