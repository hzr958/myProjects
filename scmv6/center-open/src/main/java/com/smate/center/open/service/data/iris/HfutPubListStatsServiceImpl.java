package com.smate.center.open.service.data.iris;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.dao.iris.HfutPubListStatsDao;

/**
 * 合肥工业大学期刊、论文被收录统计实现
 * 
 * @author zll
 *
 */
@Service("hfutPubListServeice")
public class HfutPubListStatsServiceImpl implements HfutPubListStatsService {

  @Autowired
  private HfutPubListStatsDao hfutPubListDao;


  @Override
  public Map<String, Object> getPubListStats(Map<String, Object> pubListStats, Map<String, Object> dataMap) {
    return hfutPubListDao.getPubListStats(pubListStats, dataMap);
  }


}
