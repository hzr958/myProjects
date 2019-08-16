package com.smate.sie.center.task.pdwh.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.PubAwardIssueInsDao;
import com.smate.sie.center.task.model.PubAwardIssueIns;

@Service("pubAwardIssueInsService")
@Transactional(rollbackFor = Exception.class)
public class PubAwardIssueInsServiceImpl implements PubAwardIssueInsService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubAwardIssueInsDao pubAwardIssueInsDao;

  @Override
  public List<PubAwardIssueIns> findAllPubAwardIssueIns(String searchKey) throws SysServiceException, DaoException {
    return pubAwardIssueInsDao.getAwardIssueIns(searchKey);
  }

  @Override
  public void savePubAwardIssueIns(String issueInsName, Long pdwhId) throws SysServiceException {
    try {
      // 判断是否已经存在该提示信息
      String query = getQuery(issueInsName);
      query = StringUtils.substring(query, 0, 50);
      if (pubAwardIssueInsDao.isExistQuery(query)) {
        return;
      }
      PubAwardIssueIns issueIns = new PubAwardIssueIns();
      issueIns.setName(issueInsName);
      issueIns.setCreateAt(new Date());
      issueIns.setQuery(query);
      pubAwardIssueInsDao.save(issueIns);
    } catch (Exception e) {
      logger.error("pdwhId:" + pdwhId + ",savePubAwardIssueIns保存授奖机构出错", e);
      throw new SysServiceException("pdwhId:" + pdwhId + ",savePubAwardIssueIns保存授奖机构出错");
    }
  }

  /**
   * 将需要处理的智能提示字符串去掉空格并转换小写。.
   */
  public String getQuery(String name) {

    return name.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
