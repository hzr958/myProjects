package com.smate.center.open.service.data.nsfc.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.model.nsfc.NsfcReschPrjRptPub;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.PubResearchReportService;
import com.smate.center.open.utils.xml.WebServiceUtils;
/*
 * @zjh 获取研究成果报告成果信息
 */

@Transactional(rollbackFor = Exception.class)
public class GetProjectReportFinalPubs extends IsisNsfcDataHandleBase {
  @Autowired
  private PubResearchReportService pubResearchReportService;

  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    String verifyResult = null;
    if ("".equals(dataParamet.get("nsfcPrjId")) || dataParamet.get("nsfcPrjId") == null) {
      verifyResult = WebServiceUtils.setResut2("-3", "Missing Parameter");
    }
    return verifyResult;
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {

    List<NsfcReschPrjRptPub> list =
        pubResearchReportService.getProjectFinalPubs(NumberUtils.toLong(dataParamet.get("nsfcPrjId").toString()),
            NumberUtils.toLong(dataParamet.get("rptYear").toString()));
    // 研究成果报告成果信息，以XML的格式返回
    return pubResearchReportService.genResultXml(list, "isis_0",
        NumberUtils.toLong(dataParamet.get("nsfcPrjId").toString()),
        NumberUtils.toLong(dataParamet.get("rptYear").toString()));
  }


}
