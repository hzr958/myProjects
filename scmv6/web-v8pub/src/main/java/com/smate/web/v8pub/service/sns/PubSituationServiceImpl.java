package com.smate.web.v8pub.service.sns;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.sns.ConstRefDbDao;
import com.smate.web.v8pub.dao.sns.PubSituationDAO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSituationPO;
import com.smate.web.v8pub.vo.sns.ConstRefDb;

@Service("pubSituationService")
@Transactional(rollbackFor = Exception.class)
public class PubSituationServiceImpl implements PubSituationService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSituationDAO pubSituationDAO;
  @Autowired
  private ConstRefDbDao constRefDbDao;

  @Override
  public PubSituationPO get(Long pubId) throws ServiceException {
    return null;
  }

  @Override
  public void save(PubSituationPO pubSituationPO) throws ServiceException {
    try {
      pubSituationDAO.save(pubSituationPO);
    } catch (Exception e) {
      logger.error("保存个人库成果收录表记录出错！对象属性为={}", pubSituationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubSituationPO pubSituationPO) throws ServiceException {
    // try {
    // pubSituationDAO.update(pubSituationPO);
    // } catch (Exception e) {
    // logger.error("更新个人库成果收录表记录出错！对象属性为={}", pubSituationPO);
    // throw new ServiceException(e);
    // }
  }

  @Override
  public void saveOrUpdate(PubSituationPO ps) throws ServiceException {
    try {
      PubSituationPO p = pubSituationDAO.findByPubIdAndLibraryName(ps.getPubId(), ps.getLibraryName());
      if (p == null) {
        ps.setGmtCreate(new Date());
        ps.setGmtModified(new Date());
        ps.setLibraryName(ps.getLibraryName().toUpperCase());
        pubSituationDAO.save(ps);
      } else {
        p.setGmtModified(new Date());
        p.setSitStatus(ps.getSitStatus());
        p.setSitOriginStatus(ps.getSitOriginStatus());
        pubSituationDAO.saveOrUpdate(p);
      }
    } catch (Exception e) {
      logger.error("保存或更新个人库成果收录表记录出错！对象属性为={}", ps);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      pubSituationDAO.delete(pubId);
    } catch (Exception e) {
      logger.error("根据pubId删除个人库成果收录表记录出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubSituationPO pubSituationPO) throws ServiceException {
    try {
      pubSituationDAO.delete(pubSituationPO);
    } catch (Exception e) {
      logger.error("删除个人库成果收录表记录出错！对象属性为={}", pubSituationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deletePubSituationByPubId(Long pubId) throws ServiceException {
    this.deleteById(pubId);
  }

  @Override
  public HashMap<Long, PubSituationPO> mapSituationByPsnId(Long psnId) throws ServiceException {
    HashMap<Long, PubSituationPO> map = new HashMap<Long, PubSituationPO>();
    try {
      List<PubSituationPO> list = pubSituationDAO.listSituationByPsnId(psnId);
      if (list == null) {
        return null;
      }
      for (PubSituationPO pubSituationPO : list) {
        if (pubSituationPO != null) {
          map.put(pubSituationPO.getPubId(), pubSituationPO);
        }
      }
      return map;
    } catch (Exception e) {
      logger.error("根据人员psnId获取该人员下所有成果的收录情况出错，psnId={}", psnId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteAll(Long pubId) throws ServiceException {
    try {
      pubSituationDAO.deleteAll(pubId);
    } catch (Exception e) {
      logger.error("删除个人库成果收录表记录出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void perfectSrcDbId(List<PubSituationDTO> saveList) throws ServiceException {
    if (CollectionUtils.isEmpty(saveList)) {
      return;
    }
    for (PubSituationDTO pubSituationDTO : saveList) {
      String dbCode = pubSituationDTO.getLibraryName();
      if (StringUtils.isNotBlank(dbCode)) {
        ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(dbCode);
        if (constRefDb != null) {
          pubSituationDTO.setSrcDbId(constRefDb.getId() + "");
        }
      }
    }
  }

}
