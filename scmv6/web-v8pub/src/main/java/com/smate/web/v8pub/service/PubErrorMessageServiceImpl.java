package com.smate.web.v8pub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dao.PubErrorMessageDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.PubErrorMessagePO;
import com.smate.web.v8pub.service.handler.PubDTO;

/**
 * 成果错误信息记录实现
 * 
 * @author YJ
 *
 *         2018年8月7日
 */
@Service(value = "pubErrorMessageService")
@Transactional(rollbackFor = Exception.class)
public class PubErrorMessageServiceImpl implements PubErrorMessageService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubErrorMessageDAO pubErrorMessageDAO;


  @Override
  public void save(PubDTO pub, String error) throws ServiceException {
    PubErrorMessagePO pubErrorMessagePO = null;
    try {
      Integer type = pub.pubHandlerName.contains("Pdwh") ? 1 : 0;
      Long psnId = NumberUtils.isNullOrZero(pub.psnId) ? 0L : pub.psnId;
      pubErrorMessagePO = new PubErrorMessagePO(pub.pubId, type, pub.pubHandlerName, psnId, error);
      pubErrorMessageDAO.save(pubErrorMessagePO);
    } catch (Exception e) {
      logger.error("保存成果错误信息出错！", pubErrorMessagePO);
      throw new ServiceException(e);
    }
  }


}
