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
import com.smate.sie.center.task.dao.PubBookPublisherDao;
import com.smate.sie.center.task.model.PubBookPublisher;

@Service("pubBookPublisherService")
@Transactional(rollbackFor = Exception.class)
public class PubBookPublisherServiceImpl implements PubBookPublisherService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubBookPublisherDao pubBookPublisherDao;

  @Override
  public List<PubBookPublisher> findAllPubBookPublisher(String searchKey) throws SysServiceException, DaoException {
    return pubBookPublisherDao.getPubBookPublisher(searchKey);
  }

  @Override
  public void savePubBookPublisher(String publisherName, Long pdwhId) throws SysServiceException {
    try {
      // 判断是否已经存在该提示信息
      String query = getQuery(publisherName);
      query = StringUtils.substring(query, 0, 50);
      if (pubBookPublisherDao.isExistQuery(query)) {
        return;
      }
      PubBookPublisher publisher = new PubBookPublisher();
      publisher.setName(publisherName);
      publisher.setCreateAt(new Date());
      publisher.setQuery(query);
      pubBookPublisherDao.save(publisher);
    } catch (Exception e) {
      logger.error("pdwhId:" + pdwhId + ",savePubBookPublisher保存出版社出错", e);
      throw new SysServiceException("pdwhId:" + pdwhId + ",savePubBookPublisher保存出版社出错");
    }
  }

  /**
   * 将需要处理的智能提示字符串去掉空格并转换小写。.
   */
  public String getQuery(String name) {

    return name.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
