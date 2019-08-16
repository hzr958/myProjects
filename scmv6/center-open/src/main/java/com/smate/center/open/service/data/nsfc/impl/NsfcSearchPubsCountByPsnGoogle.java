package com.smate.center.open.service.data.nsfc.impl;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.pub.NsfcwsPubService;
import com.smate.core.base.utils.constant.ServiceConstants;



/**
 * 根据人员ID返回该人员公开的成果记录总数：
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class NsfcSearchPubsCountByPsnGoogle extends IsisNsfcDataHandleBase {
  @Autowired
  private NsfcwsPubService nsfcwsPubService;

  /**
   * 这里没有要验证的参数
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    // String psnID, String keywords, String excludedPubIDS, String psnGuidID, String pubTypes


    return null;
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {

    String excludedPubIDS = null;
    String keywords = null;
    int pubCount = 0;
    if (dataParamet.get(OpenConsts.MAP_EXCLUDEDPUBIDS) != null) {
      excludedPubIDS = dataParamet.get(OpenConsts.MAP_EXCLUDEDPUBIDS).toString();
    }
    if (dataParamet.get(OpenConsts.MAP_KEYWORDS) != null) {
      keywords = dataParamet.get(OpenConsts.MAP_KEYWORDS).toString();
    }

    Long psnId = NumberUtils.toLong(dataParamet.get(OpenConsts.MAP_PSNID).toString());

    try {

      if (StringUtils.isNotBlank(excludedPubIDS) && !excludedPubIDS.matches(ServiceConstants.IDPATTERN)) {
        excludedPubIDS = null;
      }
      pubCount = nsfcwsPubService.getPsnPubCount(psnId, keywords, excludedPubIDS).intValue();
      return genResultXml(pubCount);
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-获取Google用户psnId={2}成果数出现异常：", psnId), e);
    }
    return null;

  }


  private String genResultXml(long count) {

    String countS = Long.toString(count);

    String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><count>" + countS + "</count>";

    return result;
  }

}
