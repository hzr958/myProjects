package com.smate.sie.center.task.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.center.task.v8pub.sns.po.PubStatisticsPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;

/**
 * 基准库成果与个人成果关系表 操作，查询个人版成果统计数v_pub_statistics
 * 
 * @author ztg
 */
@Service("pubPdwhSnsRelationService")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhSnsRelationServiceImpl implements PubPdwhSnsRelationService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;

  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;

  @Override
  public List<PubStatisticsPO> getPubStatisticsPOList(Long pdwhPubId) {
    List<PubStatisticsPO> list = null;
    try {
      List<Long> snsPubIdList = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pdwhPubId);// 根据基准库成果pdwhPubId获取关联的个人库成果snsPubId
                                                                                         // List
      if (snsPubIdList == null || snsPubIdList.size() == 0) {
        return null;
      }
      list = pubStatisticsDAO.getPubStatisticsPOList(snsPubIdList);// 根据snsPubId的list集合获取PubStatisticsPO的list集合
    } catch (Exception e) {
      logger.error("根据基准库成果pdwhPubId获取个人库成果统计数结果失败, pdwhPubId:", pdwhPubId, e);
    }
    return list;
  }

}
