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
import com.smate.web.mobile.v8pub.consts.MobileGrpFileApiConsts;

/**
 * 分享群组文件给联系人
 * 
 * @author wsn
 * @date May 24, 2019
 */
@Service("shareGrpFileToFriendService")
@Transactional(rollbackFor = Exception.class)
public class ShareGrpFileToFriendService extends ShareToSmateService {


  @Autowired
  private RestTemplate restTemplate;

  @Override
  boolean checkData(SmateShareVO vo) {
    // 校验好友ID
    if (StringUtils.isBlank(vo.getDes3FriendIds())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_RECEIVERS);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_RECEIVERS);
      return false;
    }
    // 校验群组ID
    if (StringUtils.isBlank(vo.getGrpResDes3GrpId())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_NULL_GRP_RES_GRPID);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_NULL_GRP_RES_GRPID);
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
        restTemplate.postForObject(vo.getDomainMobile() + MobileGrpFileApiConsts.FILE_SHARE_TO_FRIEND,
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
    params.add("psnId", Des3Utils.decodeFromDes3(vo.getDes3CurrentPsnId()));
    params.add("des3GrpId", StringUtils.trimToEmpty(vo.getGrpResDes3GrpId()));
    params.add("des3GrpFileId", StringUtils.trimToEmpty(vo.getDes3ResId()));
    params.add("des3ReceiverIds", StringUtils.trimToEmpty(vo.getDes3FriendIds()));
    // params.add("fileNames", groupFile.getFileName());
    params.add("textContent", StringUtils.trimToEmpty(vo.getShareText()));
    return params;
  }

}
