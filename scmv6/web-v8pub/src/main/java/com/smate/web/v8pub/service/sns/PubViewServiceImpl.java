package com.smate.web.v8pub.service.sns;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.web.v8pub.dao.sns.PubViewDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubViewPO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果查看服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubViewServiceImpl implements PubViewService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubViewDAO pubViewDAO;
  @Autowired
  private PubStatisticsService newPubStatisticsService;

  @Override
  public void save(PubViewPO pubViewPO) throws ServiceException {
    try {
      pubViewDAO.save(pubViewPO);
    } catch (Exception e) {
      logger.error("成果查看服务类：添加成果查看记录异常,pubId=" + pubViewPO.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  /**
   * 成果查看 增加查看记录
   */
  @Override
  public void viewOpt(PubOperateVO pubOperateVO) throws ServiceException {
    Long pubId = pubOperateVO.getPubId();
    try {
      // 增加成果查看记录
      insertView(pubOperateVO);
      // 更新成果统计表 阅读数
      newPubStatisticsService.updateReadStatistics(pubId);

    } catch (Exception e) {
      logger.error("成果增加查看记录异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  private void insertView(PubOperateVO pubOperateVO) {
    try {
      long formateDate = DateUtils.getDateTime(new Date());
      String ip = SpringUtils.getRemoteAddr();
      PubViewPO pubViewPO = pubViewDAO.findPubView(pubOperateVO.getPsnId(), pubOperateVO.getPubId(), formateDate, ip);
      if (pubViewPO == null) {
        pubViewPO = new PubViewPO();
        pubViewPO.setPubId(pubOperateVO.getPubId());
        pubViewPO.setViewPsnId(pubOperateVO.getPsnId());
        pubViewPO.setIp(ip);
        pubViewPO.setGmtCreate(new Date());
        pubViewPO.setFormateDate(formateDate);
        pubViewPO.setTotalCount(1l);
      } else {
        pubViewPO.setGmtCreate(new Date());
        pubViewPO.setFormateDate(formateDate);
        long viewCount = pubViewPO.getTotalCount() == null ? 0 : pubViewPO.getTotalCount();
        pubViewPO.setTotalCount(viewCount + 1);
      }
      save(pubViewPO);
    } catch (Exception e) {
      logger.error("个人库成果查看记录插入异常,pubId=" + pubOperateVO.getPubId(), e);
    }
  }

}
