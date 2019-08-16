package com.smate.center.open.service.nsfc;

import java.util.List;

import com.smate.center.open.model.nsfc.NsfcReschPrjRptPub;

/**
 * 
 * @zjh 获取研究成果报告service
 *
 */
public interface PubResearchReportService {

  //// 根据年份和项目编号获取成果研究报告
  List<NsfcReschPrjRptPub> getProjectFinalPubs(Long nsfcPrjId, Long rptYear) throws Exception;

  // 获得研究报告的版本号
  Long getReschRptVersionId(Long nsfcPrjId, Long rptYear) throws Exception;

  // 获取对用的结题报告
  boolean validateTop5PubAttachment(Long nsfcPrjId, Long rptYear) throws Exception;

  // 产生对应的xml格式的数据
  String genResultXml(List<NsfcReschPrjRptPub> pubs, String resultCode, Long nsfcPrjId, Long rptYear) throws Exception;

}
