package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.pub.GroupPubDao;
import com.smate.center.batch.dao.sns.pub.GroupPubNodeDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPubs;

/**
 * @author LY
 * 
 */
@Service("groupPubsService")
@Transactional(rollbackFor = Exception.class)
public class GroupPubsServiceImpl implements GroupPubsService {

  /**
   * 
   */
  private static final long serialVersionUID = -3827608982136481420L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPubDao groupPubDao;
  @Autowired
  private GroupPubNodeDao groupPubNodeDao;

  @Override
  public GroupPubs findGroupPub(Long groupId, Long pubId, Integer nodeId) throws ServiceException {
    try {
      return this.groupPubDao.findGroupPubs(groupId, pubId, nodeId);
    } catch (DaoException e) {
      logger.error("查找群组成果失败", e);
      throw new ServiceException("查找群组成果失败");
    }
  }

  @Override
  public void saveGroupPub(GroupPubs groupPub) throws ServiceException {
    this.groupPubDao.save(groupPub);
  }

  @Override
  public void deleteGroupPub(Long groupId, Long pubId) throws ServiceException {
    this.groupPubDao.deletePub(groupId, pubId);
  }

  // 查找是否有全文在群组中
  @Override
  public boolean isFullTextInGroup(Long pubId, Long psnId) throws ServiceException {
    Assert.notNull(pubId, "查找是否有全文在群组中pubId不能为空");
    Assert.notNull(psnId, "查找是否有全文在群组中psnId不能为空");

    // 查找成果在我加入的群组中的数量
    if (psnId > 0) {
      int inMyGroup = this.findPubCountInMyGroup(pubId, psnId);
      if (inMyGroup > 0) {// 如果找到数量大于0，说明全文可以下载
        return true;
      }
    }
    // 查找成果在开放群组中的数量
    int inOpenGroup = this.findPubCountInOpenGroup(pubId);
    if (inOpenGroup > 0) {// 如果找到数量大于0，说明全文可以下载
      return true;
    }
    return false;
  }

  // 查找成果在开放群组中的数量

  @Override
  public Integer findPubCountInOpenGroup(Long pubId) throws ServiceException {
    try {
      return this.groupPubDao.findPubCountInOpenGroup(pubId);
    } catch (DaoException e) {
      logger.error("查找成果在开放群组中的数量失败", e);
      throw new ServiceException("查找成果在开放群组中的数量失败");
    }
  }

  // 查找成果在我加入的群组中的数量
  @Override
  public Integer findPubCountInMyGroup(Long pubId, Long psnId) throws ServiceException {

    try {
      return this.groupPubNodeDao.findPubCountInMyGroup(pubId, psnId);
    } catch (DaoException e) {
      logger.error("查找成果在我加入的群组中的数量失败", e);
      throw new ServiceException("查找成果在我加入的群组中的数量失败");
    }

  }
}
