package com.smate.center.open.service.data.iris;

import java.util.Map;

/**
 * 合肥工业大学期刊、论文被收录统计接口
 * 
 * @author zll
 */


public interface HfutPubListStatsService {

  Map<String, Object> getPubListStats(Map<String, Object> pubList, Map<String, Object> dataMap);

}
