package com.smate.core.base.psn.service.psnpub;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.model.PsnPubPO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 个人成果关系实现类
 * 
 * @author yhx
 * @date 2018年6月1日
 */
@Service("psnPubService")
@Transactional(rollbackFor = Exception.class)
public class PsnPubServiceImpl implements PsnPubService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPubDAO psnPubDAO;

  @Override
  public PsnPubPO getPsnPub(Long pubId, Long ownerPsnId) throws ServiceException {
    try {
      PsnPubPO psnPubPO = psnPubDAO.getPsnPub(pubId, ownerPsnId);
      return psnPubPO;
    } catch (Exception e) {
      logger.error("查询个人成果关系失败!拥有者psnId={},pubId={}", new Object[] {ownerPsnId, pubId}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePsnPub(PsnPubPO psnPubPO) throws ServiceException {
    try {
      psnPubDAO.saveOrUpdate(psnPubPO);
    } catch (Exception e) {
      logger.error("保存个人成果失败" + "拥有者id:" + psnPubPO.getOwnerPsnId(), e);
    }

  }

  @Override
  public List<PsnPubPO> getPubsByPsnId(Long psnId) throws ServiceException {
    try {
      List<PsnPubPO> psnPubs = psnPubDAO.getPubsByPsnId(psnId);
      return psnPubs;
    } catch (Exception e) {
      logger.error("获取用户拥有的所有成果关系对象失败，psn={}", psnId, e);
    }
    return null;
  }

  @Override
  public void updateStatusByPsnIdAndPubId(Long psnId, Long pubId) throws ServiceException {
    try {
      psnPubDAO.updateStatus(psnId, pubId);
    } catch (Exception e) {
      logger.error("更新个人与成果关系状态为删除状态失败，psnId={},pubId={}", new Object[] {psnId, pubId}, e);
    }
  }

  @Override
  public Long getPubOwnerId(Long pubId) throws ServiceException {
    Long pubOwnerId = psnPubDAO.getPsnOwner(pubId);
    return pubOwnerId;
  }

  @Override
  public int findPsnPubStatus(Long pubId) {
    int status = psnPubDAO.findPsnPubStatus(pubId);
    return status;
  }

  @Override
  public List<Long> getPubIdsByPsnId(Long psnId) throws ServiceException {
    try {
      return psnPubDAO.getPubIdsByPsnId(psnId);
    } catch (Exception e) {
      logger.error("获取人员所拥有的成果的pubId出错，psnId={}", psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer getStatus(Long pubId) throws ServiceException {
    try {
      List<Integer> staList = psnPubDAO.getPubStatus(pubId);
      if (staList != null && staList.size() > 0) {
        return staList.get(0);
      }
      return null;
    } catch (Exception e) {
      logger.error("获取人员与成果的关系状态出错，pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer updatePubDate(Long pubId, Date date) {
    return psnPubDAO.updatePubDate(pubId, date);
  }

}
