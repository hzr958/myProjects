package com.smate.center.task.service.pdwh.quartz;

import java.sql.Clob;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlListDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPublicationAllDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhPublicationAll;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.ConstPdwhPubRefDb;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * pdwh 成果服务.
 * 
 * @author changwenli
 * 
 */
@Service("publicationXmlPdwhService")
@Transactional(rollbackFor = Exception.class)
public class PublicationXmlPdwhServiceImp implements PublicationXmlPdwhService {

  private static final long serialVersionUID = 1851999260179475324L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubXmlListDao pdwhPubListDao;
  @Autowired
  private PdwhPublicationAllDao pdwhPublicationAllDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;


  @Override
  public PublicationXml getPdwhPubXmlById(Long pubId, Integer dbid) throws ServiceException {
    try {
      if (dbid == null || pubId == null)
        return null;
      Map<String, Object> map = null;
      if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, dbid)) {
        map = pdwhPubListDao.getIsiPubXml(pubId);
      }
      if (ConstPdwhPubRefDb.SCOPUS.equals(dbid)) {
        map = pdwhPubListDao.getScopusPubXml(pubId);
      }
      if (ConstPdwhPubRefDb.EI.equals(dbid)) {
        map = pdwhPubListDao.getEiPubXml(pubId);
      }
      if (ConstPdwhPubRefDb.CNKI.equals(dbid)) {
        map = pdwhPubListDao.getCnkiPubXml(pubId);
      }
      if (ConstPdwhPubRefDb.CNIPR.equals(dbid)) {
        map = pdwhPubListDao.getCniprPubXml(pubId);
      }
      if (ConstPdwhPubRefDb.WanFang.equals(dbid)) {
        map = pdwhPubListDao.getWangFangPubXml(pubId);
      }
      if (ConstPdwhPubRefDb.Cnkipat.equals(dbid)) { // cnkipat
        map = pdwhPubListDao.getCnkipatPubXml(pubId);
      }
      PublicationXml pubXml = null;
      if (map != null) {
        pubXml = new PublicationXml();
        pubXml.setId(NumberUtils.toLong(ObjectUtils.toString(map.get("PUB_ID"))));
        Clob clobXml = (Clob) map.get("XML_DATA");
        pubXml.setXmlData(HqlUtils.ClobToString(clobXml));
      }
      return pubXml;
    } catch (Exception e) {
      logger.error("获取基准库XML出错 pubId:{},dbid:{}", new Object[] {pubId, dbid}, e);
      throw new ServiceException("获取基准库XML出错");
    }
  }


  @SuppressWarnings("rawtypes")
  @Override
  public PdwhPublicationAll getPdwhPublicationAll(Long pubId, Integer dbid) throws ServiceException {
    PdwhPublicationAll puball = null;
    try {
      PublicationXml pubxml = this.getPdwhPubXmlById(pubId, dbid);
      if (pubxml == null)
        return null;
      puball = pdwhPublicationAllDao.getPubAll(pubId, dbid);
      if (puball != null) {
        PubXmlDocument doc = new PubXmlDocument(pubxml.getXmlData());
        puball.setFulltextUrl(doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url"));
      }
      /*
       * if (puball.getJnlId() != null) { Map map = baseJournalIfDao.getBjnlLastYearIf(puball.getJnlId());
       * if (map != null) { puball.setImpactFactors(ObjectUtils.toString(map.get("JOU_IF")) + "(" +
       * ObjectUtils.toString(map.get("IF_YEAR")) + ")"); } } ConstRefDb constRefDb =
       * constRefDbDao.get(NumberUtils.toLong(ObjectUtils.toString(dbid))); if (constRefDb != null) {
       * puball.setListInfo(ObjectUtils.toString(constRefDb.getCode())); }
       */
    } catch (Exception e) {
      logger.error("获取基准库成果详情出错 pubId:{},dbid:{}", new Object[] {pubId, dbid}, e);
    }
    return puball;
  }

  @Override
  public String getXmlStringByPubId(Long pubId) {
    return pdwhPubXmlDao.getXmlStringByPubId(pubId);
  }
}
