package com.smate.center.open.service.data.nsfc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.NsfcwsPerson;
import com.smate.center.open.service.common.IrisCommonService;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.NsfcwsPsnService;

/**
 * 根据人员ID返回人员的记录：
 * 
 * @author ajb 通过bean获得
 */
@Transactional(rollbackFor = Exception.class)
public class NsfcSearchPsnByPsnIDGoogleServiceImpl extends IsisNsfcDataHandleBase {


  @Autowired
  private NsfcwsPsnService nsfcwsPsnService;

  @Autowired
  private IrisCommonService irisCommonService;

  /**
   * 参数校验
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) {
    String verifyResult = null;

    return verifyResult;
  }

  /**
   * 数据操作
   * 
   * @throws Exception
   */
  @Override
  public String doHandlerIsisData(Map<String, Object> paramet) throws Exception {

    String psnListXml = "<persons></persons>";
    try {
      Long psnId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_PSNID).toString());
      NsfcwsPerson nsfcwsPerson = nsfcwsPsnService.getNsfcwsPersonByPsnId(psnId);
      if (nsfcwsPerson != null) {
        List<NsfcwsPerson> psnList = new ArrayList<NsfcwsPerson>();
        psnList.add(nsfcwsPerson);
        psnListXml = irisCommonService.buildGooglePsnListXmlStr(psnList);
      }
    } catch (Exception e) {
      logger.error("IRIS业务系统接口-通过用户名+单位名或邮箱检索Google用户出现异常：", e);
    }
    return psnListXml;

    // return WebServiceUtils.getResutlError(WebServiceUtils.SYNC_ROL_PROJECT);
  }


}
