package com.smate.center.open.service.data.nsfc.impl;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.center.open.service.common.IrisCommonService;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.pub.NsfcwsPubService;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.model.Page;

/**
 * 根据人员ID返回该人员公开的成果
 * 
 * @author tsz
 *
 */
public class NsfcSearchPubsListByPsnServiceImpl extends IsisNsfcDataHandleBase {

  @Autowired
  private NsfcwsPubService nsfcwsPubService;
  @Autowired
  private IrisCommonService irisCommonService;


  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    try {
      String verifyResult = null;
      Object pageSize = dataParamet.get("pageSize");
      if (pageSize == null || NumberUtils.isNumber(pageSize.toString()) || Integer.valueOf(pageSize.toString()) < 1) {
        verifyResult = WebServiceUtils.setResut2("-3", "获取成果列表 分页参数（pageSize）不正确");
        return verifyResult;
      }
      Object pageNum = dataParamet.get("pageNum");
      if (pageNum == null || NumberUtils.isNumber(pageNum.toString()) || Integer.valueOf(pageNum.toString()) < 1) {
        verifyResult = WebServiceUtils.setResut2("-3", "获取成果列表 分页参数（pageNum）不正确");
        return verifyResult;
      }
      return verifyResult;
    } catch (Exception e) {
      logger.error("获取成果列表 参数校验失败", e);
      throw new Exception("获取成果列表 参数校验失败", e);
    }
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {

    String pubListXml = "<publications></publications>";
    Long psnId = Long.valueOf(dataParamet.get(OpenConsts.MAP_PSNID).toString());
    try {
      String keywords = dataParamet.get("keywords") == null ? null : dataParamet.get("keywords").toString();
      String excludedPubIDS =
          dataParamet.get("excludedPubIDS") == null ? null : dataParamet.get("excludedPubIDS").toString();
      String sortType = dataParamet.get("sortType") == null ? null : dataParamet.get("sortType").toString();
      int pageSize = Integer.valueOf(dataParamet.get("pageSize").toString());
      int pageNum = Integer.valueOf(dataParamet.get("pageNum").toString());
      Page<NsfcwsPublication> page =
          nsfcwsPubService.getPsnPubByPage(psnId, keywords, excludedPubIDS, sortType, pageSize, pageNum);
      if (CollectionUtils.isNotEmpty(page.getResult())) {
        pubListXml = irisCommonService.buildGooglePubListXmlStr(page.getResult());
      }
      return pubListXml;
    } catch (Exception e) {
      logger.error(String.format("人员psnId=%s查询成果列表出现异常：", psnId), e);
      throw new Exception(String.format("psnId=%s查询成果列表出现异常：", psnId), e);
    }
  }

}
