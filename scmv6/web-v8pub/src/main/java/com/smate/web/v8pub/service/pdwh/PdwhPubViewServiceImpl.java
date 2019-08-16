package com.smate.web.v8pub.service.pdwh;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubViewDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubViewPO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 基准库成果查看服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubViewServiceImpl implements PdwhPubViewService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubViewDAO pdwhPubViewDAO;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;

  @Override
  public void save(PdwhPubViewPO pubViewPO) throws ServiceException {
    try {
      pdwhPubViewDAO.save(pubViewPO);
    } catch (Exception e) {
      logger.error("成果查看服务类：添加成果查看记录异常,pubId=" + pubViewPO.getPdwhPubId(), e);
      throw new ServiceException(e);
    }

  }

  /**
   * 成果查看 增加查看记录
   */
  @Override
  public void pdwhViewOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException {
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      // 增加成果查看记录
      insertPdwhView(pdwhPubOperateVO);
      // 更新成果统计表 阅读数
      newPdwhPubStatisticsService.updateReadStatistics(pdwhPubId);
    } catch (Exception e) {
      logger.error("成果增加查看记录异常,pdwhPubId=" + pdwhPubId, e);
      throw new ServiceException(e);
    }
  }

  private void insertPdwhView(PdwhPubOperateVO pdwhPubOperateVO) {
    try {
      long formateDate = DateUtils.getDateTime(new Date());
      String ip = SpringUtils.getRemoteAddr();

      PdwhPubViewPO pdwhPubViewPO =
          pdwhPubViewDAO.findPdwhPubView(pdwhPubOperateVO.getPsnId(), pdwhPubOperateVO.getPdwhPubId(), formateDate, ip);
      if (pdwhPubViewPO == null) {
        pdwhPubViewPO = new PdwhPubViewPO();
        pdwhPubViewPO.setPdwhPubId(pdwhPubOperateVO.getPdwhPubId());
        pdwhPubViewPO.setViewPsnId(pdwhPubOperateVO.getPsnId());
        pdwhPubViewPO.setIp(ip);
        pdwhPubViewPO.setGmtCreate(new Date());
        pdwhPubViewPO.setFormateDate(formateDate);
        pdwhPubViewPO.setTotalCount(1l);
      } else {
        pdwhPubViewPO.setGmtCreate(new Date());
        long viewCount = pdwhPubViewPO.getTotalCount() == null ? 0 : pdwhPubViewPO.getTotalCount();
        pdwhPubViewPO.setTotalCount(viewCount + 1);
      }
      save(pdwhPubViewPO);
    } catch (Exception e) {
      logger.error("基准库成果查看记录插入异常,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
    }
  }

}
