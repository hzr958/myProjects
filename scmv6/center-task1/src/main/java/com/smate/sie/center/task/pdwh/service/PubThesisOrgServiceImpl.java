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
import com.smate.sie.center.task.dao.PubThesisOrgDao;
import com.smate.sie.center.task.model.PubThesisOrg;

@Service("pubThesisOrgService")
@Transactional(rollbackFor = Exception.class)
public class PubThesisOrgServiceImpl implements PubThesisOrgService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubThesisOrgDao pubThesisOrgDao;

  @Override
  public List<PubThesisOrg> findAllPubThesisOrg(String searchKey) throws SysServiceException, DaoException {
    return pubThesisOrgDao.getPubThesisOrg(searchKey);
  }

  @Override
  public void savePubThesisOrg(String thesisOrg, Long pdwhId) throws SysServiceException {
    try {
      // 判断是否已经存在该提示信息
      // 判断是否已经存在该提示信息
      String query = getQuery(thesisOrg);
      query = StringUtils.substring(query, 0, 50);
      if (pubThesisOrgDao.isExistQuery(query)) {
        return;
      }
      PubThesisOrg org = new PubThesisOrg();
      org.setName(thesisOrg);
      org.setCreateAt(new Date());
      org.setQuery(query);
      pubThesisOrgDao.save(org);
    } catch (Exception e) {
      logger.error("pdwhId:" + pdwhId + ",savePubThesisOrg保存颁发单位出错", e);
      throw new SysServiceException("pdwhId:" + pdwhId + ",savePubThesisOrg保存颁发单位出错");
    }
  }

  /**
   * 将需要处理的智能提示字符串去掉空格并转换小写。.
   */
  public String getQuery(String name) {

    return name.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
