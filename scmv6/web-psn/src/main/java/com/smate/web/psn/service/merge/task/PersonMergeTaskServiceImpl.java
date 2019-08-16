package com.smate.web.psn.service.merge.task;



import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.bpo.PersonMergeDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.bpo.PersonMerge;


/**
 * 人员合并服务实现类.
 * 
 * @author liangguokeng
 * 
 */
@Service("personMergeTaskService")
@Transactional(rollbackFor = Exception.class)
public class PersonMergeTaskServiceImpl implements PersonMergeTaskService {

  /**
   * 
   */
  private static final long serialVersionUID = 7196339117913339487L;
  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  PersonMergeDao personMergeTaskDao;


  /**
   * 保存需合并人员记录.
   * 
   * @param savePsnId
   * @param delPsnId
   * @throws ServiceException
   */
  @Override
  public void saveMergePsn(Long savePsnId, Long delPsnId) throws ServiceException {
    try {
      PersonMerge mergePsn = new PersonMerge();
      mergePsn.setSavePsnId(savePsnId);
      mergePsn.setDelPsnId(delPsnId);
      mergePsn.setCreateDate(new Date());
      mergePsn.setStatus(0L);
      mergePsn.setStatusExt(0L);// 是否需要跑动态合并任务.
      personMergeTaskDao.savePersonMerge(mergePsn);
    } catch (DaoException e) {
      logger.error("保存合并人员的psnId的记录出错,savePsnId=" + savePsnId + ",delPsnId" + delPsnId, e);
      throw new ServiceException(e);
    }
  }
}
