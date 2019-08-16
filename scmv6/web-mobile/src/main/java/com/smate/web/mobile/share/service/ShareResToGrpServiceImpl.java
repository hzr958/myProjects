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
 * 分享资源到群组
 * 
 * @author wsn
 * @date May 24, 2019
 */
@Service("shareResToGrpService")
@Transactional(rollbackFor = Exception.class)
public class ShareResToGrpServiceImpl extends ShareToSmateService {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private GrpDynShareService grpDynShareService;


  @Override
  boolean checkData(SmateShareVO vo) {
    // 分享到的群组Id
    if (StringUtils.isBlank(vo.getDes3GrpId())) {
      vo.setErrorCode("4");
      vo.setErrorMsg("the des3GrpId is null");
      return false;
    }
    vo.setTempType(SmateShareConstant.GROUP_TMEP_TYPE_MAP.get(vo.getResType()));
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  Map<String, Object> shareRes(SmateShareVO vo) {
    MultiValueMap<String, String> params = buildShareParams(vo);
    Map<String, Object> resultMap =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.SHARE_TO_GROUP_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (resultMap == null || !"success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INTERFACE_ERROR);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INTERFACE_ERROR);
    }
    return resultMap;
  }

  private MultiValueMap<String, String> buildShareParams(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3ReceiverGrpId", StringUtils.trimToEmpty(vo.getDes3GrpId()));
    params.add("dynContent", StringUtils.trimToEmpty(vo.getShareText()));
    params.add("des3ResId", StringUtils.trimToEmpty(vo.getDes3ResId()));
    params.add("tempType", StringUtils.trimToEmpty(vo.getTempType()));
    params.add("resType", StringUtils.trimToEmpty(vo.getResType()));
    params.add("des3Psnid", StringUtils.trimToEmpty(vo.getDes3CurrentPsnId()));
    return params;
  }

  @Override
  void otherDeals(SmateShareVO vo) {
    // 如果是群组动态那里点击的分享，则要更新记录群组动态分享信息
    grpDynShareService.updateGrpDynShareStatistics(vo);

  }


}
