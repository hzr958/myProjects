package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.dao.sns.pub.PubRelatedDao;
import com.smate.center.batch.dao.sns.pub.TaskPubRelatedIdsDao;
import com.smate.center.batch.model.sns.pub.PubRelated;
import com.smate.center.batch.model.sns.pub.TaskPubRelatedIds;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.Page;


@Service("pubPubAllRelatedService")
@Transactional(rollbackFor = Exception.class)
public class PubRelatedServiceImpl implements PubRelatedService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRelatedDao pubRelatedDao;
  @Autowired
  private TaskPubRelatedIdsDao taskPubRelatedIdsDao;

  @Override
  public void save(List<PubRelated> list) throws BatchTaskException {
    if (CollectionUtils.isEmpty(list))
      return;
    for (PubRelated pubRelated : list) {
      pubRelatedDao.save(pubRelated);
    }
  }

  @Override
  public void delete(Long pubId) throws BatchTaskException {
    pubRelatedDao.deletePubRelated(pubId);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public List<Long> findPubAllIdsByPubId(Page page, Long pubId) throws BatchTaskException {
    try {
      return pubRelatedDao.findPubAllIdsByPubId(page, pubId);
    } catch (Exception e) {
      logger.error("根据pubId获取相关文献pubAllIds出现异常，pubId:{}", pubId, e);
    }
    return null;
  }

  @Override
  public List<Long> findTaskPubRelatedIds(int batchSize) throws BatchTaskException {
    return taskPubRelatedIdsDao.findTaskPubRelatedIds(batchSize);
  }

  @Override
  public void updateTaskPubRelatedIds(Long pubId, int status) throws BatchTaskException {
    taskPubRelatedIdsDao.updateTaskPubRelatedIds(pubId, status);
  }

  @Override
  public void saveTaskPubRelatedIds(Long pubId) throws BatchTaskException {
    TaskPubRelatedIds pubIds = taskPubRelatedIdsDao.get(pubId);
    if (pubIds == null) {
      pubIds = new TaskPubRelatedIds();
      pubIds.setPubId(pubId);
      pubIds.setStatus(0);
      taskPubRelatedIdsDao.save(pubIds);
    } else {
      pubIds.setStatus(0);
    }
  }
}
