package com.smate.center.task.service.rol.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubSourceDbDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.rol.quartz.PublicationListRolDao;
import com.smate.center.task.dao.rol.quartz.RolPubIdTmpDao;
import com.smate.center.task.dao.rol.quartz.RolPubXmlDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubSourceDb;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.rol.quartz.PublicationListRol;
import com.smate.center.task.model.rol.quartz.RolPubIdTmp;
import com.smate.center.task.model.rol.quartz.RolPubXml;
import com.smate.center.task.single.dao.rol.pub.PubPdwhRolRelationDao;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;

@Service("handlePubListService")
@Transactional(rollbackFor = Exception.class)
public class HandlePubListServiceImpl implements HandlePubListService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RolPubIdTmpDao rolPubIdTmpDao;
  @Autowired
  private PubPdwhRolRelationDao pubPdwhRolRelationDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private RolPubXmlDao rolPubXmlDao;
  @Autowired
  private PdwhPubSourceDbDao pdwhPubSourceDbDao;
  @Autowired
  private PublicationListRolDao publicationListRolDao;

  @Override
  public List<RolPubIdTmp> getRolPubId(Integer size) {
    return rolPubIdTmpDao.getRolPubId(size);
  }

  @Override
  public Long getPdwhPubId(Long rolPubId) {
    return pubPdwhRolRelationDao.getPdwhPubId(rolPubId);
  }

  @Override
  public PdwhPubXml getPdwhPubXml(Long pdwhPubId) {
    return pdwhPubXmlDao.get(pdwhPubId);
  }

  @Override
  public RolPubXml getRolPubXml(Long rolPubId) {
    return rolPubXmlDao.get(rolPubId);
  }

  @Override
  public PdwhPubSourceDb getPdwhPubSourceDb(Long pdwhPubId) {
    return pdwhPubSourceDbDao.getPubSourceDb(pdwhPubId);
  }

  @Override
  public void fillPubList(PubXmlDocument rolXmldocument, PdwhPubSourceDb pdwhPubSourceDb) {
    if (pdwhPubSourceDb.getSci() != null && pdwhPubSourceDb.getSci() != 0) {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", "1");
    } else {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci", "0");
    }
    if (pdwhPubSourceDb.getIstp() != null && pdwhPubSourceDb.getIstp() != 0) {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", "1");
    } else {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp", "0");
    }
    if (pdwhPubSourceDb.getSsci() != null && pdwhPubSourceDb.getSsci() != 0) {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", "1");
    } else {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci", "0");
    }
    if (pdwhPubSourceDb.getEi() != null && pdwhPubSourceDb.getEi() != 0) {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "1");
    } else {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei", "0");
    }
    if (pdwhPubSourceDb.getCnki() != null && pdwhPubSourceDb.getCnki() != 0) {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_cnki", "1");
    } else {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_cnki", "0");
    }
    if (pdwhPubSourceDb.getCnkiPat() != null && pdwhPubSourceDb.getCnkiPat() != 0) {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_cnkipat", "1");
    } else {
      rolXmldocument.setXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_cnkipat", "0");
    }

  }

  @Override
  public void saveRolPubXml(RolPubXml rolPubXml) {
    try {
      rolPubXmlDao.save(rolPubXml);
    } catch (Exception e) {
      logger.error("HandlePubListTask保存成果xml出错 pubId:" + rolPubXml.getPubId(), e);
    }

  }

  @Override
  public void praseSourcePubList(PubXmlDocument rolXmldocument) {
    if (rolXmldocument.existsNode(PubXmlConstants.PUB_LIST_XPATH)) {
      PublicationListRol pubList = publicationListRolDao.get(rolXmldocument.getPubId());
      if (pubList == null) {
        pubList = new PublicationListRol();
        pubList.setId(rolXmldocument.getPubId());
      }
      String listEI = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei");
      if (StringUtils.isNotBlank(listEI)) {
        pubList.setListEi(NumberUtils.toInt(listEI));
      } else {
        pubList.setListEi(0);
      }
      String listEISource = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ei_source");
      if (StringUtils.isNotBlank(listEISource)) {
        pubList.setListEiSource(NumberUtils.toInt(listEISource));
      } else {
        pubList.setListEiSource(0);
      }
      String listSCI = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci");
      if (StringUtils.isNotBlank(listSCI)) {
        pubList.setListSci(NumberUtils.toInt(listSCI));
      } else {
        pubList.setListSci(0);
      }
      String listSCISource = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_sci_source");
      if (StringUtils.isNotBlank(listSCISource)) {
        pubList.setListSciSource(NumberUtils.toInt(listSCISource));
      } else {
        pubList.setListSciSource(0);
      }
      String listISTP = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp");
      if (StringUtils.isNotBlank(listISTP)) {
        pubList.setListIstp(NumberUtils.toInt(listISTP));
      } else {
        pubList.setListIstp(0);
      }
      String listISTPSource = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_istp_source");
      if (StringUtils.isNotBlank(listISTPSource)) {
        pubList.setListIstpSource(NumberUtils.toInt(listISTPSource));
      } else {
        pubList.setListIstpSource(0);
      }
      String listSSCI = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci");
      if (StringUtils.isNotBlank(listSSCI)) {
        pubList.setListSsci(NumberUtils.toInt(listSSCI));
      } else {
        pubList.setListSsci(0);
      }
      String listSSCISource = rolXmldocument.getXmlNodeAttribute(PubXmlConstants.PUB_LIST_XPATH, "list_ssci_source");
      if (StringUtils.isNotBlank(listSSCISource)) {
        pubList.setListSsciSource(NumberUtils.toInt(listSSCISource));
      } else {
        pubList.setListSsciSource(0);
      }
      publicationListRolDao.save(pubList);
    }
  }

  @Override
  public void saveOptResult(RolPubIdTmp rolPubIdTmp, Integer status) {
    rolPubIdTmp.setStatus(status);
    rolPubIdTmpDao.updateStatus(rolPubIdTmp);
  }

}
