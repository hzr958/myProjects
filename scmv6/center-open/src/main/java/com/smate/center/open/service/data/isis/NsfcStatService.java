package com.smate.center.open.service.data.isis;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;
import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjReport;
import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjRptPub;

/**
 * 向基金委提供项目结题数据-接口
 * 
 * @author hp
 * @date 2015-10-21
 */
public interface NsfcStatService {
  public List<NsfcPrjPubReport> getPrjPubReportList(String insName, Long rptYear);

  List<Map<String, Object>> getStatData2(Map<String, Object> param);

  List<Map<String, Object>> getStatData3(Map<String, Object> param);

  List<Map<String, Object>> getStatData4(Map<String, Object> param);

  List<Map<String, Object>> getStatData5(Map<String, Object> param);

  /** 从SNS取数据 */
  List<SnsNsfcPrjReport> getNsfcPrjReportList(Map<String, Object> dataMap);

  /** 从SNS取数据 */
  List<SnsNsfcPrjRptPub> getPubsByRptIds(Set<Long> rptIdSet);

  /**
   * 从SNS取数据
   * 
   * @throws Exception
   */
  Map<String, Object> getPubStatByIns(Map<String, Object> dataMap) throws Exception;


}
