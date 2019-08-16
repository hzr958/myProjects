package com.smate.center.open.service.pdwh.jnl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.pdwh.jnl.JnlLevelDao;

/**
 * 期刊级别SERVICE.
 * 
 * @author xys
 *
 */
@Service("jnlLevelService")
@Transactional(rollbackFor = Exception.class)
public class JnlLevelServiceImpl implements JnlLevelService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JnlLevelDao jnlLevelDao;

  @Override
  public Map<String, Object> getJnlLevelStats(List<Long> jids) throws Exception {
    try {
      Map<String, Object> jnlLevelStatsMap = new HashMap<String, Object>();
      int intJnlNum = 0;
      int homeCoreJnlNum = 0;
      int otherJnlNum = 0;
      List<Long> intJnlJids = jnlLevelDao.getIntJnlJids(jids);
      List<Long> homeCoreJnlJids = jnlLevelDao.getHomeCoreJnlJids(jids);
      if (!CollectionUtils.isEmpty(intJnlJids)) {
        intJnlNum = intJnlJids.size();
      }
      if (!CollectionUtils.isEmpty(homeCoreJnlJids)) {
        homeCoreJnlNum = homeCoreJnlJids.size();
      }
      for (Long jid : jids) {
        if ((CollectionUtils.isEmpty(intJnlJids) || !intJnlJids.contains(jid))
            && (CollectionUtils.isEmpty(homeCoreJnlJids) || !homeCoreJnlJids.contains(jid))) {
          otherJnlNum++;
        }
      }
      jnlLevelStatsMap.put("intJnlNum", intJnlNum);
      jnlLevelStatsMap.put("homeCoreJnlNum", homeCoreJnlNum);
      jnlLevelStatsMap.put("otherJnlNum", otherJnlNum);
      return jnlLevelStatsMap;
    } catch (Exception e) {
      logger.error("获取期刊级别统计数出现异常了喔", e);
      throw new Exception("获取期刊级别统计数出现异常了喔", e);
    }
  }

  @Override
  public Map<String, Object> getJnlLevelJids(List<Long> jids) throws Exception {
    try {
      Map<String, Object> jnlLevelJidsMap = new HashMap<String, Object>();
      List<Long> intJnlJids = jnlLevelDao.getIntJnlJids(jids);
      List<Long> homeCoreJnlJids = jnlLevelDao.getHomeCoreJnlJids(jids);
      List<Long> otherJnlJids = new ArrayList<Long>();
      for (Long jid : jids) {
        if ((CollectionUtils.isEmpty(intJnlJids) || !intJnlJids.contains(jid))
            && (CollectionUtils.isEmpty(homeCoreJnlJids) || !homeCoreJnlJids.contains(jid))) {
          otherJnlJids.add(jid);
        }
      }
      jnlLevelJidsMap.put("intJnlJids", intJnlJids);
      jnlLevelJidsMap.put("homeCoreJnlJids", homeCoreJnlJids);
      jnlLevelJidsMap.put("otherJnlJids", otherJnlJids);
      return jnlLevelJidsMap;
    } catch (Exception e) {
      logger.error("获取期刊级别jids出现异常了喔", e);
      throw new Exception("获取期刊级别jids出现异常了喔", e);
    }
  }

}
