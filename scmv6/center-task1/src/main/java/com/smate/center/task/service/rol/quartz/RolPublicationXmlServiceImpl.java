package com.smate.center.task.service.rol.quartz;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.single.service.pub.PubXmlStoreService;

@Service("rolPublicationXmlService")
@Transactional(rollbackFor = Exception.class)
public class RolPublicationXmlServiceImpl implements RolPublicationXmlService {
  @Resource(name = "rolPubXmlStoreService")
  private PubXmlStoreService pubXmlStoreService;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public PublicationXml getById(Long pubId) throws ServiceException {
    try {
      return pubXmlStoreService.get(pubId);
    } catch (Exception e) {
      logger.error("获取xml失败", e);
      throw new ServiceException(e);

    }

  }

  @Override
  public PublicationXml save(Long pubId, String xml) throws ServiceException {
    try {
      Document doc = DocumentHelper.parseText(xml);
      return pubXmlStoreService.save(pubId, xml);
    } catch (Exception e) {
      logger.error("保存XML失败" + ":" + xml, e);
      throw new ServiceException("保存XML失败");
    }

  }

}
