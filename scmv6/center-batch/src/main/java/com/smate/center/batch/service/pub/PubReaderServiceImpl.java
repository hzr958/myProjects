package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubReaderDao;
import com.smate.center.batch.dao.sns.pub.TaskPubReadIdsDao;
import com.smate.center.batch.form.pub.PubReaderForm;
import com.smate.center.batch.model.sns.pub.PubReader;
import com.smate.center.batch.model.sns.pub.TaskPubReadIds;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;


/**
 * @author changwenli
 * 
 */
@Service("pubReaderService")
@Transactional(rollbackFor = Exception.class)
public class PubReaderServiceImpl implements PubReaderService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubReaderDao pubReaderDao;
  @Autowired
  private TaskPubReadIdsDao taskPubReadIdsDao;
  @Autowired
  private PersonManager personManager;

  @Override
  public void save(List<PubReader> list) throws BatchTaskException {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    for (PubReader pubReader : list) {
      pubReaderDao.save(pubReader);
    }
  }

  @Override
  public void delete(Long pubId) throws BatchTaskException {
    pubReaderDao.deletePubReader(pubId);
  }

  @Override
  public List<Long> findTaskPubReadIds(int batchSize) throws BatchTaskException {
    return taskPubReadIdsDao.findTaskPubReadIds(batchSize);
  }

  @Override
  public void updateTaskPubReadIds(Long pubId, int status) throws BatchTaskException {
    taskPubReadIdsDao.updateTaskPubReadIds(pubId, status);
  }

  @Override
  public void saveTaskPubReadIds(Long pubId) throws BatchTaskException {
    TaskPubReadIds pubIds = taskPubReadIdsDao.get(pubId);
    if (pubIds == null) {
      pubIds = new TaskPubReadIds();
      pubIds.setPubId(pubId);
      pubIds.setStatus(0);
      taskPubReadIdsDao.save(pubIds);
    } else {
      pubIds.setStatus(0);
    }
  }

  @Override
  public List<Long> getPubReaders(Long pubId, Long psnId) throws BatchTaskException {
    return pubReaderDao.getPubReaders(pubId, psnId);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Page getPubReaders(Page<PubReaderForm> page, Long pubId) throws BatchTaskException {
    page = pubReaderDao.getPubReaders(page, pubId);
    List<PubReaderForm> psnList = page.getResult();
    if (CollectionUtils.isNotEmpty(psnList)) {
      for (PubReaderForm form : psnList) {
        Person person = personManager.getPerson(form.getPsnId());
        if (StringUtils.isNotBlank(person.getAvatars())) {
          form.setAvatars(person.getAvatars());
        }
        // 修改了获取人员头衔，显示名称的逻辑_MJG_SCM-5707.
        if (StringUtils.isNotBlank(person.getTitolo())) {
          form.setTitolo(person.getViewTitolo());
        }
        form.setPsnName(person.getViewName());
      }
    }
    return page;
  }

  @Override
  public void ajaxDelPubReader(Long pubId, Long psnId) throws BatchTaskException {
    pubReaderDao.ajaxDelPubReader(pubId, psnId);
  }
}
