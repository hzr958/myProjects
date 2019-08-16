package com.smate.center.open.service.data.nsfc.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.model.nsfc.NsfcwsPerson;
import com.smate.center.open.service.common.IrisCommonService;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.GooglePsnService;
import com.smate.center.open.utils.xml.WebServiceUtils;

/**
 * 
 * @zjh 根据人员信息查询人员列表的记录
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SearchPsnList extends IsisNsfcDataHandleBase {

  @Autowired
  private GooglePsnService nsfcwsPsnService;
  @Autowired
  private IrisCommonService irisCommonService;

  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    String psnEmail = "";
    String psnOrgName = "";
    String psnName = "";
    if (dataParamet.size() > 0) {
      for (String key : dataParamet.keySet()) {
        if (key == "psnEmail") {
          psnEmail = dataParamet.get("psnEmail").toString();
        }
        if (key == "psnOrgName") {
          psnOrgName = dataParamet.get("psnOrgName").toString();
        }
        if (key == "psnName") {
          psnName = dataParamet.get("psnName").toString();
        }
      }
    }
    dataParamet.put("psnEmail", psnEmail);
    dataParamet.put("psnName", psnName);
    dataParamet.put("psnOrgName", psnOrgName);

    if ((StringUtils.isNotBlank(psnName) && StringUtils.isNotBlank(psnOrgName)) || StringUtils.isNotBlank(psnEmail)) {
      return null;
    } else {
      return WebServiceUtils.setResult3("-3", "Missing Parameter");
    }

  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {
    String psnListXml = "<persons></persons>";
    try {
      List<NsfcwsPerson> psnList = null;
      psnList = nsfcwsPsnService.getNsfcwsPerson(dataParamet.get("psnName").toString(),
          dataParamet.get("psnOrgName").toString(), dataParamet.get("psnEmail").toString());
      if (CollectionUtils.isNotEmpty(psnList)) {
        psnListXml = irisCommonService.buildGooglePsnListXmlStr(psnList);
      }
    } catch (Exception e) {
      throw new Exception("IRIS业务系统接口-通过用户名+单位名或邮箱检索Google用户出现异常：", e);
    }
    return psnListXml;
  }

}
