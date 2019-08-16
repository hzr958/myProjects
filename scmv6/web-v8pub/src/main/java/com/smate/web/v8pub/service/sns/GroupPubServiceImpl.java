package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.dao.sns.group.GroupPubsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群组成果实现类
 */
@Service("groupPubService")
@Transactional(rollbackFor = Exception.class)
public class GroupPubServiceImpl implements GroupPubService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupPubsDAO groupPubsDAO;

  @Override
  public GroupPubPO get(Long id) throws ServiceException {
    GroupPubPO grpPubs = null;
    try {
      grpPubs = groupPubsDAO.get(id);
      return grpPubs;
    } catch (Exception e) {
      logger.error("获取群组与个人库成果关系记录出错！对象属性={}", grpPubs, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(GroupPubPO grpPubs) throws ServiceException {
    try {
      groupPubsDAO.save(grpPubs);
    } catch (Exception e) {
      logger.error("保存群组与个人库成果关系记录出错！对象属性={}", grpPubs, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(GroupPubPO grpPubs) throws ServiceException {
    try {
      groupPubsDAO.update(grpPubs);
    } catch (Exception e) {
      logger.error("更新群组与个人库成果关系记录出错！对象属性={}", grpPubs, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(GroupPubPO grpPubs) throws ServiceException {
    try {
      groupPubsDAO.saveOrUpdate(grpPubs);
    } catch (Exception e) {
      logger.error("更新或保存群组与个人库成果关系记录出错！对象属性={}", grpPubs, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      groupPubsDAO.delete(id);
    } catch (Exception e) {
      logger.error("根据逻辑id删除群组与个人库成果关系记录出错！id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(GroupPubPO grpPubs) throws ServiceException {
    try {
      groupPubsDAO.delete(grpPubs);
    } catch (Exception e) {
      logger.error("根据逻辑id删除群组与个人库成果关系记录出错！对象属性={}", grpPubs, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateGrpPubsGmtModified(Long pubId) throws ServiceException {
    try {
      groupPubsDAO.updateGrpPubsGmtModified(pubId);
    } catch (Exception e) {
      logger.error("更新成果pubId所在群组的更新时间出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateStatusByGrpIdAndPubId(Long grpId, Long pubId, Integer status) throws ServiceException {
    try {
      groupPubsDAO.updateStatusByGrpIdAndPubId(grpId, pubId, status);
    } catch (Exception e) {
      logger.error("更新群组与个人库成果的状态出错！pubId={},grpId={}", new Object[] {pubId, grpId}, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public GroupPubPO getByGrpIdAndPubId(Long grpId, Long pubId) throws ServiceException {
    GroupPubPO groupPubPO = null;
    try {
      groupPubPO = groupPubsDAO.findByGrpIdAndPubId(grpId, pubId);
      return groupPubPO;
    } catch (Exception e) {
      logger.error("根据grpId和pubId获取群组与个人库成果关系对象异常！GroupPubPO={}", groupPubPO, e);
      throw new ServiceException(e);
    }
  }

  @Override public GroupPubPO getGrpPub(Long grpId, Long pubId) throws ServiceException {
    GroupPubPO groupPubPO = null;
    try {
      groupPubPO = groupPubsDAO.findGrpPub(grpId, pubId);
      return groupPubPO;
    } catch (Exception e) {
      logger.error("根据grpId和pubId获取群组与个人库成果关系对象异常！GroupPubPO={}", groupPubPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean existGrpPub(Long grpId, Long pubId) throws ServiceException {
    GroupPubPO groupPubPO = groupPubsDAO.findByGrpIdAndPubId(grpId, pubId);
    return groupPubPO == null ? false : true;
  }

  @Override
  public int findGrpPubStatus(Long grpId, Long pubId) throws ServiceException {
    Integer status = groupPubsDAO.findGrpPubsStatus(pubId, grpId);
    return status;
  }

  @Override
  public Long getPubOwnerPsnId(Long grpId, Long pubId) throws ServiceException {
    Long ownerPsnId = null;
    try {
      GroupPubPO groupPubPO = groupPubsDAO.findByGrpIdAndPubId(grpId, pubId);
      if (groupPubPO != null) {
        ownerPsnId = groupPubPO.getOwnerPsnId();
      }
      return ownerPsnId;
    } catch (Exception e) {
      logger.error("获取群组成果拥有者出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public GroupPubPO getByPubId(Long pubId) throws ServiceException {
    GroupPubPO groupPubPO = null;
    try {
      groupPubPO = groupPubsDAO.findByPubId(pubId);
      return groupPubPO;
    } catch (Exception e) {
      logger.error("根据grpId和pubId获取群组与个人库成果关系对象异常！GroupPubPO={}", groupPubPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer getStatus(Long pubId) {
    try {
      List<Integer> staList = groupPubsDAO.getStatus(pubId);
      if (staList != null && staList.size() > 0) {
        return staList.get(0);
      }
      return null;
    } catch (Exception e) {
      logger.error("获取群组与成果关系状态出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

}
