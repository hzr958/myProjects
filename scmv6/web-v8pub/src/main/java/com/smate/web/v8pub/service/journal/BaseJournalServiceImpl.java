package com.smate.web.v8pub.service.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.journal.BaseJournalDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.journal.BaseJournal2;
import com.smate.web.v8pub.po.journal.BaseJournalPO;

/**
 * 基础期刊服务类
 * 
 * @author YJ
 *
 *         2018年8月6日
 */

@Service("baseJournalService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalServiceImpl implements BaseJournalService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BaseJournalDAO baseJournalDAO;

  @Override
  public BaseJournalPO getById(Long jnlId) throws ServiceException {
    try {
      return baseJournalDAO.get(jnlId);
    } catch (Exception e) {
      logger.error("通过jidId获取基础期刊对象出错！");
      throw new ServiceException(e);
    }
  }

  @Override
  public Long searchJnlMatchBaseJnlId(String jname, String issn) throws ServiceException {
    try {
      return baseJournalDAO.searchJnlMatchBaseJnlId(jname, issn);
    } catch (Exception e) {
      logger.error("根据期刊名和期刊issn匹配基础期刊的jid出错！ jname={},issn={}", new Object[] {jname, issn});
      throw new ServiceException(e);
    }
  }

  @Override
  public BaseJournal2 getBaseJournal2Title(Long baseJnlId) throws ServiceException {
    try {
      return baseJournalDAO.getBaseJournal2Title(baseJnlId);
    } catch (Exception e) {
      logger.error("基准库获取期刊信息出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String findPdwhPubImpactFactors(Long jnlId,Integer publishYear) {
    return baseJournalDAO.findImpactFactors(jnlId,publishYear);
  }
}
