package com.smate.web.v8pub.service.sns.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.sns.group.GrpStatisticsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GrpStatistics;

@Service(value = "groupStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class GroupStatisticsServiceImpl implements GroupStatisticsService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpStatisticsDAO grpStatisticsDAO;

  @Override
  public GrpStatistics get(Long pubId) throws ServiceException {
    GrpStatistics grpStatistics = null;
    try {
      grpStatistics = grpStatisticsDAO.get(pubId);
      return grpStatistics;
    } catch (Exception e) {
      logger.error("群组统计数服务：获取群组统计数对象出错！", grpStatistics);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(GrpStatistics grpStatistics) throws ServiceException {
    try {
      grpStatisticsDAO.save(grpStatistics);
    } catch (Exception e) {
      logger.error("群组统计数服务：保存群组统计数对象出错！", grpStatistics);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(GrpStatistics grpStatistics) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(GrpStatistics grpStatistics) throws ServiceException {
    try {
      grpStatisticsDAO.saveOrUpdate(grpStatistics);
    } catch (Exception e) {
      logger.error("群组统计数服务：保存或更新群组统计数对象出错！", grpStatistics);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(GrpStatistics grpStatistics) throws ServiceException {
    // TODO Auto-generated method stub

  }

}
