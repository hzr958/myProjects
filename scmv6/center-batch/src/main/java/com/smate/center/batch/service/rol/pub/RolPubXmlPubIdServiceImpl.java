package com.smate.center.batch.service.rol.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.RolPubXmlPubIdDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.RolPubXml;
import com.smate.center.batch.model.rol.pub.RolPubXmlPubId;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;


/**
 * 成果xml去掉版权标识服务
 * 
 * @author zk
 * 
 */
@Service("rolPubXmlPubIdService")
@Transactional(rollbackFor = Exception.class)
public class RolPubXmlPubIdServiceImpl implements RolPubXmlPubIdService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RolPubXmlPubIdDao rolPubXmlPubIdDao;
  @Autowired
  private RolPubXmlService rolPubXmlService;

  /**
   * 获取未处理数字
   */
  @Override
  public List<RolPubXmlPubId> gets(Long startPubId, Integer size) throws ServiceException {
    return rolPubXmlPubIdDao.gets(startPubId, size);
  }

  /**
   * 开始处理pubXml
   */
  @Override
  public void handlePubXml(List<RolPubXmlPubId> pubIds) throws ServiceException {

    for (RolPubXmlPubId pubId : pubIds) {
      RolPubXml pubXml = rolPubXmlService.getPubXml(pubId.getPubId());
      if (pubXml != null && StringUtils.isNotBlank(pubXml.getPubXml())) {
        try {
          PubXmlDocument xmlDocument;
          xmlDocument = new PubXmlDocument(pubXml.getPubXml());
          String enAbstract = xmlDocument.getXmlNodeAttribute("/publication", "en_abstract");
          xmlDocument = deleteCopyRight(xmlDocument, enAbstract, "en_abstract");
          String zhAbstract = xmlDocument.getXmlNodeAttribute("/publication", "zh_abstract");
          xmlDocument = deleteCopyRight(xmlDocument, zhAbstract, "zh_abstract");
          rolPubXmlService.savePubXml(pubId.getPubId(), xmlDocument.getXmlString());
          pubId.setStatus(1);
        } catch (DocumentException e) {
          logger.error("处理pubxml的copyright出错！", e);
        }
      } else {
        pubId.setStatus(2);
      }
      pubId.setCreateDate(new Date());
      rolPubXmlPubIdDao.save(pubId);
    }
  }

  /**
   * 删除版权信息 删除最后两百字符内所有的版权信息
   * 
   * @param pubXml
   * @return
   * @throws ServiceException
   */
  private PubXmlDocument deleteCopyRight(PubXmlDocument xmlDocument, String enAbstract, String localAbstract)
      throws ServiceException {

    if (StringUtils.isNotBlank(enAbstract)) {
      int len = enAbstract.length();
      String firstHalf = "";
      String lastHalf = "";
      if (len > 200) {
        firstHalf = enAbstract.substring(0, len - 200); // 取两百
        lastHalf = enAbstract.substring(len - 200);
      } else {
        lastHalf = enAbstract;
      }
      int copyPos = StringUtils.indexOfAny(lastHalf,
          new String[] {"All rights reserved", "Copyright", "(C)", "(c)", "©", "&copy;"});

      if (copyPos > -1) {
        lastHalf = lastHalf.substring(0, copyPos);
        int dotPos = StringUtils.lastIndexOf(lastHalf, ".");
        if (dotPos > -1) {
          lastHalf = lastHalf.substring(0, dotPos + 1);

          xmlDocument.setXmlNodeAttribute("/publication", localAbstract, firstHalf + lastHalf);
        }
      }
    }

    return xmlDocument;
  }

}
