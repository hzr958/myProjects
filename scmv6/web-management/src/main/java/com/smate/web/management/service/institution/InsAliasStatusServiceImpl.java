package com.smate.web.management.service.institution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.institution.bpo.InsAliasStatusDao;
import com.smate.web.management.model.institution.bpo.InsAliasStatus;

/**
 * 单位更改标记，方便检索式维护查询.
 * 
 * @author zjh
 *
 */
@Service("insAliasStatusService")
@Transactional(rollbackFor = Exception.class)
public class InsAliasStatusServiceImpl implements InsAliasStatusService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsAliasStatusDao insAliasStatusDao;

  @Override
  public void markNameCg(Long id) throws Exception {
    try {
      InsAliasStatus status = loadCreateStatus(id);
      status.setNameCg(1);
      status.setConfirmStatus(0);
      insAliasStatusDao.save(status);
    } catch (Exception e) {
      logger.error("单位改名，标记一下.", e);
      throw new Exception("单位改名，标记一下.", e);
    }

  }

  /**
   * 加载标记，如果不存在，创建.
   * 
   * @param insId
   * @return
   */
  private InsAliasStatus loadCreateStatus(Long insId) {

    return insAliasStatusDao.getInsAliasStatus(insId);
  }

}
