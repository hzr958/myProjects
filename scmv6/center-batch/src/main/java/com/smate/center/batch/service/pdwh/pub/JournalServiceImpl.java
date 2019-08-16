package com.smate.center.batch.service.pdwh.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.BaseJournalIfDao;
import com.smate.center.batch.dao.pdwh.pub.BaseJournalSearchDao;
import com.smate.center.batch.dao.pdwh.pub.JournalDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.BaseJournalSearch;
import com.smate.center.batch.model.pdwh.pub.Journal;
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
  @Autowired
  private BaseJournalSearchDao baseJournalSearchDao;
  @Autowired
  private BaseJournalIfDao baseJournalIfDao;


  @Override
  public Long getMatchBaseJnlId(Long jid) throws ServiceException {
    // TODO Auto-generated method stub
    return journalDao.getJnlByMatchBaseJnlId(jid);
  }

  @Override
  public BaseJournalSearch getBaseJournalById(final long jnlId) throws ServiceException {

    return this.baseJournalSearchDao.get(jnlId);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String getBaseJournalImpactors(Long bjId) throws ServiceException {
    String result = "";
    try {
      Map map = baseJournalIfDao.getBjnlLastYearIf(bjId);
      if (map != null) {
        result = ObjectUtils.toString(map.get("JOU_IF")) + "(" + ObjectUtils.toString(map.get("IF_YEAR")) + ")";
      }
    } catch (Exception e) {
      logger.error("获取基础期刊影响因子出错,bjId:{}", bjId, e);
    }
    return result;
  }

  @Override
  public Journal getById(long jid) throws BatchTaskException {
    return this.journalDao.get(jid);
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
    } catch (DaoException e) {
      logger.error("addJournal失败,jname=" + jname + ", jissn=" + jissn + ", from=" + from, e);
      throw new SysServiceException(e);
    }

    return jsns;
  }
}
