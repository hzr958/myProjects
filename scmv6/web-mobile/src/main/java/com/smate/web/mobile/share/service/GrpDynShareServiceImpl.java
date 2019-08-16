package com.smate.web.mobile.share.service;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 群组动态分享处理实现
 * 
 * @author wsn
 * @date Jun 19, 2019
 */
@Service("grpDynShareService")
@Transactional(rollbackFor = Exception.class)
public class GrpDynShareServiceImpl implements GrpDynShareService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;

  @SuppressWarnings("unchecked")
  @Override
  public void updateGrpDynShareStatistics(SmateShareVO vo) {
    try {
      if (SmateShareConstant.FROM_GRP_DYN.equals(vo.getFromPage()) && StringUtils.isNotBlank(vo.getDes3DynId())) {
        // 构建接口参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", StringUtils.trimToEmpty(vo.getDes3CurrentPsnId()));
        params.add("des3DynId", StringUtils.trimToEmpty(vo.getDes3DynId()));
        params.add("ShareContent", StringUtils.trimToEmpty(vo.getShareText()));
        // 调用接口更新分享记录
        Map<String, Object> resultMap =
            restTemplate.postForObject(vo.getDomainMobile() + SmateShareConstant.SHARE_GRP_RECORD,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (resultMap == null || !"success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          vo.setWarnCode(SmateShareConstant.SHARE_ERROR_CODE_GRP_DYN_SHARE_UPDATE);
          vo.setWarnMsg(SmateShareConstant.SHARE_ERROR_MSG_GRP_DYN_SHARE_UPDATE);
        }
      }
    } catch (Exception e) {
      logger.error("更新群组动态分享操作统计数异常, dynId={}, psnId={}", vo.getDes3DynId(), vo.getDes3CurrentPsnId(), e);
      vo.setWarnCode(SmateShareConstant.SHARE_ERROR_CODE_GRP_DYN_SHARE_UPDATE);
      vo.setWarnMsg(SmateShareConstant.SHARE_ERROR_MSG_GRP_DYN_SHARE_UPDATE);
    }
  }

}
