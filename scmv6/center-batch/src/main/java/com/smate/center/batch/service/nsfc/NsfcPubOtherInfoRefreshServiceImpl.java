package com.smate.center.batch.service.nsfc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.nsfc.NsfcPubMemberDao;
import com.smate.center.batch.dao.nsfc.NsfcPubOtherInfoRefreshDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.dao.sns.pub.PubSimpleDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.nsfc.NsfcPubMember;
import com.smate.center.batch.model.sns.nsfc.NsfcPubOtherInfoRefresh;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubSimple;

/**
 * 成果其他信息刷新服务实现.
 * 
 * @author xys
 *
 */
@Service(value = "nsfcPubOtherInfoRefreshService")
@Transactional(rollbackFor = Exception.class)
public class NsfcPubOtherInfoRefreshServiceImpl implements NsfcPubOtherInfoRefreshService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NsfcPubOtherInfoRefreshDao nsfcPubOtherInfoRefreshDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  PubDataStoreDao pubDataStoreDao;
  @Autowired
  NsfcPubMemberDao nsfcPubMemberDao;
  @Autowired
  private NsfcPubOtherInfoService nsfcPubOtherInfoService;

  @Override
  public List<NsfcPubOtherInfoRefresh> getNsfcPubOtherInfoNeedRefreshBatch(int maxSize) throws ServiceException {
    return this.nsfcPubOtherInfoRefreshDao.getNsfcPubOtherInfoRefreshBatch(maxSize);
  }

  @Override
  public void updateNsfcPubOtherInfo(NsfcPubOtherInfoRefresh nsfcPubOtherInfoRefresh) throws ServiceException {
    Long pubId = nsfcPubOtherInfoRefresh.getPubId();
    try {
      PubSimple pubSimple = this.pubSimpleDao.get(pubId);
      PubDataStore pubDataStore = this.pubDataStoreDao.get(pubId);
      List<NsfcPubMember> nsfcPubMemberList = nsfcPubMemberDao.getNsfcPubMemberList(pubId);
      if (pubSimple != null && pubDataStore != null) {
        this.nsfcPubOtherInfoService.saveNsfcPubOtherInfo(pubSimple, pubDataStore.getData(), nsfcPubMemberList);
      }
    } catch (Exception e) {
      logger.error("更新成果其他信息出错了喔,pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveNsfcPubOtherInfoRefresh(NsfcPubOtherInfoRefresh nsfcPubOtherInfoRefresh) throws ServiceException {
    try {
      this.nsfcPubOtherInfoRefreshDao.save(nsfcPubOtherInfoRefresh);
    } catch (Exception e) {
      logger.error("保存成果其他信息出错了喔，pubId: " + nsfcPubOtherInfoRefresh.getPubId() + ",status: "
          + nsfcPubOtherInfoRefresh.getStatus(), e);
      throw new ServiceException(e);
    }
  }

}
