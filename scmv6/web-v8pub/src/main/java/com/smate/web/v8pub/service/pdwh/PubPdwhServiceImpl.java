package com.smate.web.v8pub.service.pdwh;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.exception.DAOException;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.CollectedPubDao;
import com.smate.web.v8pub.dao.sns.PubFulltextPsnRcmdDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubFulltextPsnRcmd;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 基准成果基础信息服务实现类
 * 
 * @author houchuanjie
 * @date 2018/06/01 18:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubPdwhServiceImpl implements PubPdwhService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private CollectedPubDao collectedPubDao;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;
  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;

  @Override
  public PubPdwhPO get(Long pubId) throws ServiceException {
    try {
      return pubPdwhDAO.get(pubId);
    } catch (DAOException e) {
      logger.error("查询成果出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubPdwhPO pubPdwhPO) throws ServiceException {
    try {
      this.pubPdwhDAO.save(pubPdwhPO);
    } catch (DAOException e) {
      logger.error("保存成果出错！{}", pubPdwhPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubPdwhPO pubPdwhPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PubPdwhPO pubPdwhPO) throws ServiceException {
    try {
      this.pubPdwhDAO.saveOrUpdate(pubPdwhPO);
    } catch (DAOException e) {
      logger.error("保存或更新基准库成果出错！对象属性={}", pubPdwhPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PubPdwhPO pubPdwhPO) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void findByIds(PubQueryDTO pubQueryDTO) {
    pubPdwhDAO.findByIds(pubQueryDTO);
  }
  @Override
  public void findByIdsforSie(PubQueryDTO pubQueryDTO) {
    pubPdwhDAO.findByIdsforSie(pubQueryDTO);
  }
  @Override
  public void findAllByIds(PubQueryDTO pubQueryDTO) {
    pubPdwhDAO.findAllByIds(pubQueryDTO);
  }

  @Override
  public boolean hasCollectedPdwhPub(Long psnId, Long pdwhPubId) {
    return collectedPubDao.isCollectedPub(psnId, pdwhPubId, PubDbEnum.PDWH);
  }

  @Override
  public String buildPdwhPubIndexUrl(Long pdwhPubId) {
    PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlService.get(pdwhPubId);
    if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
      return pdwhPubIndexUrl.getPubIndexUrl();
    }
    return "";
  }

  @Override
  public PubFulltextPsnRcmd getPubFulltextPsnRcmdById(Long pdwhId) {
    return this.pubFulltextPsnRcmdDao.get(pdwhId);
  }

  @Override
  public void updateStatusByPubId(Long pdwhPubId, PubPdwhStatusEnum status) throws ServiceException {
    try {
      pubPdwhDAO.updateStatus(pdwhPubId, status);
    } catch (Exception e) {
      logger.error("更新基准库成果状态失败，pdwhPubId={},status={}", new Object[] {pdwhPubId, status}, e);
    }
  }
}
