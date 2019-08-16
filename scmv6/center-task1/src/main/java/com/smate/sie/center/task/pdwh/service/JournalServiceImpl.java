package com.smate.sie.center.task.pdwh.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.JournalDao;
import com.smate.center.task.model.pdwh.quartz.Journal;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.string.JnlFormateUtils;

/**
 * 期刊入库原则：2010/12/08 <br/>
 * 所有用户公用一个期刊库，不再按用户区分.<br/>
 * 查找journal的逻辑 按jname和jissn的组合查询
 * 
 * @author yamingd
 * 
 */
@Service("journalService")
@Transactional(rollbackFor = Exception.class)
public class JournalServiceImpl implements JournalService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JournalDao journalDao;

  @Override
  public Long getMatchBaseJnlId(Long jid) throws SysServiceException {
    return journalDao.getJnlByMatchBaseJnlId(jid);
  }

  @Override
  public Journal getById(long jid) throws BatchTaskException {
    return this.journalDao.get(jid);
  }

  @Override
  public Journal addJournal(String jname, String jissn, long currentUserId, String from) throws SysServiceException {

    jissn = XmlUtil.buildStandardIssn(jissn);
    Journal jsns = new Journal();
    jsns.setIssn(jissn);
    if (XmlUtil.isChinese(jname)) {
      jsns.setZhName(jname.trim());
    } else {
      jsns.setEnName(jname.trim());
    }
    jsns.setAddPsnId(currentUserId);
    try {
      jsns.setStatus(0);
      jsns.setRegDate(new Date());
      journalDao.addJournal(jsns);
    } catch (Exception e) {
      logger.error("addJournal失败,jname=" + jname + ", jissn=" + jissn + ", from=" + from, e);
      throw new SysServiceException(e);
    }

    return jsns;
  }

  @Override
  public Long findJournalByJnlId(Long jnlId) throws SysServiceException {
    try {

      List<Long> jids = journalDao.queryJidByJnlId(jnlId);
      if (jids != null && jids.size() > 0) {
        return jids.get(0);
      }
    } catch (Exception e) {
      logger.error("findJournalByJnlId失败,jnlId=" + jnlId, e);
      throw new SysServiceException(e);
    }
    return null;
  }

  @Override
  public Journal findJournalByNameIssn(String jname, String issn, Long psnId) throws SysServiceException {
    Journal ret = null;
    try {
      String nameAlias = JnlFormateUtils.getStrAlias(jname);
      issn = XmlUtil.buildStandardIssn(issn);
      List<Journal> jList = this.journalDao.queryJournalByNameIssn(issn, nameAlias, psnId);
      if (CollectionUtils.isNotEmpty(jList)) {
        for (Journal journal : jList) {
          if (journal.getMatchBaseJnlId() != null) {
            ret = journal;
            break;
          }
        }
        if (ret == null) {
          ret = jList.get(0);
        }
      }
    } catch (Exception e) {
      logger.error("===查询期刊出错jname:{},issn:{},psnId:{}", new Object[] {jname, issn, psnId}, e);
    }
    return ret;
  }
}
