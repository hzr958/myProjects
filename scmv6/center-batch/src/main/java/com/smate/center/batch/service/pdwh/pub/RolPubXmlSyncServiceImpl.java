package com.smate.center.batch.service.pdwh.pub;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubXmlSyncEvent;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.RolPublicationXmlManager;

/**
 * SNS-ROl成果XML同步服务,后台抽取XML服务.
 * 
 * @author yamingd
 * 
 */
@Service("rolPubXmlSyncService")
@Transactional(rollbackFor = Exception.class)
public class RolPubXmlSyncServiceImpl implements RolPubXmlSyncService {

  // logger.
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private RolPublicationXmlManager rolPublicationXmlManager;

  /**
   * ###########该段代码废除，由个人端发送MQ消息到单位端，而不是直接拉数据.
   */
  @Override
  public void pullFromSNS(PubXmlSyncEvent req) throws ServiceException {

  }

  /**
   * ###########该段代码废除，由单位端发送MQ消息到个人端，而不是远程调用推数据.
   */
  @Override
  public void pushToSNS(PubXmlSyncEvent req) throws ServiceException {

  }

  @Override
  public Long reserviceFromSns(Long snsPubId, Long psnId, Long insId, String xmlData) throws ServiceException {
    logger.debug("reserviceFromSns开始:{},{}", new Date(), snsPubId);
    try {
      Long newPubId = this.rolPublicationXmlManager.saveXmlFromSNS(insId, psnId, snsPubId, xmlData);
      logger.debug("新ROL成果ID={}", newPubId);
      logger.debug("reserviceFromSns结束:{},{}", new Date(), snsPubId);
      return newPubId;
    } catch (Exception e) {
      logger.error("reserviceFromSns保存SNS成果XML到ROL错误。{}", snsPubId, e);
      throw new ServiceException(e);
    }

  }
}
