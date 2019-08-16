package com.smate.center.task.v8pub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.PdwhPubDoiBakDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubDoiBakPO;
import com.smate.core.base.pub.util.PDFDataUtils;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * 基准库成果doi数据格式化
 * 
 * @author YJ
 *
 *         2019年3月26日
 */

@Service("pdwhPubDoiFormatService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubDoiFormatServiceImpl implements PdwhPubDoiFormatService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubDoiBakDAO pdwhPubDoiBakDAO;

  @Override
  public void formatPubDoi(PdwhDataTaskPO pubData) throws ServiceException {
    try {
      Long id = pubData.getPubId();
      if (NumberUtils.isNotNullOrZero(id)) {
        PdwhPubDoiBakPO pdwhPubDoiPO = pdwhPubDoiBakDAO.get(id);
        if (pdwhPubDoiPO != null) {
          String formatDoi = PDFDataUtils.matchDOI(pdwhPubDoiPO.getDoi());
          pdwhPubDoiPO.setFormatDoi(formatDoi);
          pdwhPubDoiBakDAO.saveOrUpdate(pdwhPubDoiPO);
        }
      }
    } catch (Exception e) {
      logger.error("格式化成果doi数据出错！", e);
      throw new ServiceException(e);
    }
  }

}
