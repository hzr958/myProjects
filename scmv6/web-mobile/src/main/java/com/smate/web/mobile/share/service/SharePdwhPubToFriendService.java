package com.smate.web.mobile.share.service;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.vo.SmateShareVO;
import com.smate.web.mobile.utils.RestUtils;

/**
 * 分享基准库成果给联系人
 * 
 * @author wsn
 * @date May 24, 2019
 */
@Service("sharePdwhPubToFriendService")
@Transactional(rollbackFor = Exception.class)
public class SharePdwhPubToFriendService extends ShareToSmateService {


  @Autowired
  private RestTemplate restTemplate;

  @Override
  boolean checkData(SmateShareVO vo) {
    // 校验好友ID和接收者email
    if (StringUtils.isBlank(vo.getDes3FriendIds()) && StringUtils.isBlank(vo.getReceiverEmails())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_RECEIVERS);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_RECEIVERS);
      return false;
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  Map<String, Object> shareRes(SmateShareVO vo) {
    // 构建分享所需参数
    MultiValueMap<String, String> params = this.buildShareParams(vo);
    // 调用接口进行分享
    Map<String, Object> resultMap =
        restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.SHARE_PDWH_PUB_TO_FRIENDS_URL,
            RestUtils.buildPostRequestEntity(params), Map.class);
    if (resultMap == null || !"success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INTERFACE_ERROR);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INTERFACE_ERROR);
    }
    return resultMap;
  }

  @Override
  void otherDeals(SmateShareVO vo) {

  }


  /**
   * 构建分享所需参数
   * 
   * @param vo
   * @return
   */
  private MultiValueMap<String, String> buildShareParams(SmateShareVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", StringUtils.trimToEmpty(Des3Utils.decodeFromDes3(vo.getDes3CurrentPsnId())));
    params.add("des3PubIds", StringUtils.trimToEmpty(vo.getDes3ResId()));
    params.add("des3ReceiverIds", StringUtils.trimToEmpty(vo.getDes3FriendIds()));
    params.add("content", StringUtils.trimToEmpty(vo.getShareText()));
    params.add("receiverEmails", StringUtils.trimToEmpty(vo.getReceiverEmails()));
    params.add("msgType", "7");
    params.add("smateInsideLetterType", "pdwhpub");
    return params;
  }

}
