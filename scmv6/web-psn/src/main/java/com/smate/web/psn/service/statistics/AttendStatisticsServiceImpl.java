package com.smate.web.psn.service.statistics;



import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.date.DateUtils;
import com.smate.web.psn.dao.statistics.AttentionStatisticsDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.statistics.AttentionStatistics;
import com.smate.web.psn.service.user.UserService;


/**
 * 统计模块
 * 
 * @author Scy
 * 
 */
@Service("attendStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class AttendStatisticsServiceImpl implements AttendStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private UserService userService;
  @Autowired
  private AttentionStatisticsDao attentionStatisticsDao;



  @Override
  public void addAttRecord(Long psnId, Long attPsnId, Integer action) throws ServiceException {
    try {
      if (psnId.equals(attPsnId)) {
        logger.warn("自己关注自己的记录不予以保存，psnId=" + psnId + ",attPsnId=" + attPsnId);
        return;
      }
      String ip = userService.findLoginIpByPsnId(psnId);
      AttentionStatistics attentionStatistics = new AttentionStatistics();
      attentionStatistics.setPsnId(psnId);
      attentionStatistics.setAttPsnId(attPsnId);
      attentionStatistics.setAction(action);
      Date nowDate = new Date();
      attentionStatistics.setCreateDate(nowDate);
      long formateDate = DateUtils.getDateTime(nowDate);
      attentionStatistics.setFormateDate(formateDate);
      attentionStatistics.setIp(ip);

      // 老系统没有同步取消关注的任务
      /*
       * try { StatisticsSyncMessage message = new StatisticsSyncMessage();
       * message.setAction(StatisticsSyncConstant.ADD_RECORD);
       * message.setAttentionStatistics(attentionStatistics); statisticsSyncProducer.syncMessage(message);
       * } catch (Exception e) { logger.error("JMS同步保存关注记录出错！PsnId=" + psnId + " attPsnId=" + attPsnId +
       * " action=" + action, e); }
       */

      attentionStatisticsDao.save(attentionStatistics);

    } catch (Exception e) {
      logger.error("保存关注记录出错！PsnId=" + psnId + " attPsnId=" + attPsnId + " action=" + action, e);
      throw new ServiceException(e);
    }
  }


}
