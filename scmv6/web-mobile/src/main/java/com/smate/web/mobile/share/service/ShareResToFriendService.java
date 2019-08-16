package com.smate.web.mobile.share.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.vo.SmateShareVO;

/**
 * 分享资源给联系人
 * 
 * @author wsn
 * @date May 24, 2019
 */
@Service("shareResToFriendService")
@Transactional(rollbackFor = Exception.class)
public class ShareResToFriendService extends ShareToSmateService {

  @Autowired
  private GrpDynShareService grpDynShareService;
  @Autowired
  private ShareToSmateService shareAgencyToFriendService;
  @Autowired
  private ShareToSmateService shareFundToFriendService;
  @Autowired
  private ShareToSmateService shareGrpFileToFriendService;
  @Autowired
  private ShareToSmateService sharePdwhPubToFriendService;
  @Autowired
  private ShareToSmateService sharePrjToFriendService;
  @Autowired
  private ShareToSmateService sharePsnFileToFriendService;
  @Autowired
  private ShareToSmateService sharePsnPubToFriendService;
  private Map<String, ShareToSmateService> shareServiceMap;
  private ShareToSmateService shareService;

  @Override
  Map<String, Object> shareRes(SmateShareVO vo) {
    return shareService.shareRes(vo);
  }

  @Override
  void otherDeals(SmateShareVO vo) {
    shareService.otherDeals(vo);
    // 如果是群组动态那里点击的分享，则要更新记录群组动态分享信息
    grpDynShareService.updateGrpDynShareStatistics(vo);
  }

  @Override
  boolean checkData(SmateShareVO vo) {
    initShareServiceMap();
    if (!shareServiceMap.containsKey(vo.getResType())) {
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INCORRECT_RESTYPE);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INCORRECT_RESTYPE);
      return false;
    }
    shareService = shareServiceMap.get(vo.getResType());
    return true;
  }



  private void initShareServiceMap() {
    if (shareServiceMap == null) {
      shareServiceMap = new HashMap<String, ShareToSmateService>();
      shareServiceMap.put(SmateShareConstant.SHARE_AGENCY_RES_TYPE, shareAgencyToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_FUND_RES_TYPE, shareFundToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_PSN_FILE_RES_TYPE, sharePsnFileToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_GROUP_FILE_RES_TYPE, shareGrpFileToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_PDWH_PUB_RES_TYPE, sharePdwhPubToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_SNS_PUB_RES_TYPE, sharePsnPubToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_PRJ_RES_TYPE, sharePrjToFriendService);
    }
  }

}
