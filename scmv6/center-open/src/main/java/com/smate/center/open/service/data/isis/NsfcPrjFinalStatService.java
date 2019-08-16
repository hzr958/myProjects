package com.smate.center.open.service.data.isis;


import java.util.List;
import java.util.Map;

import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;

/**
 * 向基金委提供项目结题数据-接口
 * 
 * @author hp
 * @date 2015-10-21
 */
public interface NsfcPrjFinalStatService {
  public List<NsfcPrjPubReport> getPrjPubReportList(String insName, Long rptYear);

  public void saveNsfcPrjPubReport(List<Map<String, Object>> reportList, Map<Long, String> jidMap,
      Map<Long, Long> hxjMap, List<Long> inCategoryJidList);

  public List<Map<String, Object>> getStatData2(Map<String, Object> params);

  public List<Map<String, Object>> getStatData3(Map<String, Object> params);

  public List<Map<String, Object>> getStatData4(Map<String, Object> params);

  public List<Map<String, Object>> getStatData5(Map<String, Object> params);
}
