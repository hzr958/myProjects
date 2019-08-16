package com.smate.center.open.service.data.sie;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.isis.BaseJournalDao;
import com.smate.center.open.dao.pdwh.jnl.BaseJournalSearch;
import com.smate.center.open.dao.pdwh.jnl.BaseJournalSearchDao;
import com.smate.center.open.dao.pdwh.jnl.BaseJournalTitleDao;
import com.smate.center.open.dao.pdwh.jnl.JournalDao;
import com.smate.center.open.model.pdwh.jnl.BaseJournalTitleTo;
import com.smate.center.open.model.pdwh.jnl.Journal;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;

/**
 * 获取通过期刊名字或期刊 issn获取 期刊 ID
 * 
 * @author aijiangbin
 *
 */

@Transactional(rollbackFor = Exception.class)
public class GetPdwhJournalServiceImpl extends ThirdDataTypeBase {

  /**
   * 刚注册的.
   */
  static final int REGISTERED = 0;
  /**
   * 通过审核的.
   */
  static final int APPROVED = 1;
  /**
   * 已删除的.
   */
  static final int DELETED = 2;


  @Autowired
  private JournalDao journalDao;
  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;
  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private BaseJournalSearchDao baseJournalSearchDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();

    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    Object jnameObj = paramet.get(OpenConsts.MAP_JNAME);
    Object psnIdObj = paramet.get(OpenConsts.MAP_PSNID);
    Object issnObj = paramet.get(OpenConsts.MAP_ISSN);
    Object jnamFromObj = paramet.get(OpenConsts.MAP_JNAME_FROM);
    if (jnameObj == null || StringUtils.isBlank(jnameObj.toString())) {
      logger.error("获取通过期刊信息jname不能为空，jname=" + paramet.get(OpenConsts.MAP_JNAME));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2001, paramet, "scm-2001 具体服务类型参数  jname 不能为空");
      return temp;
    }
    if (psnIdObj == null || !NumberUtils.isDigits(psnIdObj.toString())) {
      logger.error("获取通过期刊信息psnId不能为空，必须为数字，psnId=" + paramet.get(OpenConsts.MAP_PSNID));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2002, paramet, "scm-2002 具体服务类型参数  psnId 不能为空，必须为数字");
      return temp;
    }
    if (jnamFromObj == null) {
      logger.error("获取通过期刊信息缺少jnamFrom，jnamFrom=" + paramet.get(OpenConsts.MAP_JNAME_FROM));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2004, paramet, "缺少jnamFrom参数");
      return temp;
    }
    if (issnObj == null) {
      logger.error("获取通过期刊信息缺少issn，issn=" + paramet.get(OpenConsts.MAP_ISSN));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2003, paramet, "缺少issn参数");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    Map<String, Object> data = new HashMap<String, Object>();

    String jname = paramet.get(OpenConsts.MAP_JNAME).toString();
    Long psnId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_PSNID).toString());
    String issn = paramet.get(OpenConsts.MAP_ISSN).toString();
    // source_db_code
    String jnamFrom = paramet.get(OpenConsts.MAP_JNAME_FROM).toString();
    try {
      Journal journal = findJournalByNameIssn(jname, issn, psnId);
      if (journal == null) {
        journal = addJournal(jname, issn, psnId, jnamFrom);
      }
      data.put(OpenConsts.MAP_JID, journal == null ? "" : journal.getId());
    } catch (SysServiceException e) {
      data.put(OpenConsts.MAP_JID, "");
      logger.error("获取通过期刊信息异常，psnId=" + psnId, e);
    }
    dataList.add(data);
    return successMap("获取通过期刊信息成功", dataList);
  }

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

  public Journal addJournal(String jname, String jissn, long currentUserId, String from) throws ServiceException {
    jissn = XmlUtil.buildStandardIssn(jissn);
    Journal j = new Journal();
    j.setIssn(jissn);
    if (XmlUtil.isChinese(jname)) {
      j.setZhName(jname.trim());
    } else {
      j.setEnName(jname.trim());
    }
    j.setAddPsnId(currentUserId);
    j.setFromFlag(from);
    // lqh add状态
    j.setStatus(REGISTERED);
    j.setRegDate(new Date());
    try {
      // 新增期刊时匹配基础期刊
      Long baseJnlId = null;
      if (StringUtils.isNotBlank(jname) && StringUtils.isNotBlank(jissn)) {
        baseJnlId = baseJournalDao.snsJnlMatchBaseJnlId(jname, jissn);
        if (baseJnlId == null) {
          List<BaseJournalTitleTo> bjtList = baseJournalTitleDao.snsJnlMatchBaseJnlId(jname, jissn);
          baseJnlId = CollectionUtils.isNotEmpty(bjtList) ? bjtList.get(0).getJnlId() : null;
        }
      }
      if (baseJnlId != null) {
        j.setMatchBaseJnlId(baseJnlId);
        BaseJournalSearch bjnl = this.getBaseJournalById(baseJnlId);
        if (bjnl != null) {
          j.setZhName(bjnl.getTitleXx());
          j.setEnName(bjnl.getTitleEn());
        }
      }
      j.setMatchBaseStatus(1);
      this.journalDao.addJournal(j);
      // 新增期刊同步更新期刊刷新表，供期刊统计用.
      this.journalDao.syncJournalFlag(j.getId());
    } catch (Exception e) {
      logger.error("addJournal失败,jname=" + jname + ", jissn=" + jissn + ", from=" + from, e);
      throw new ServiceException(e);
    }
    return j;
  }

  public BaseJournalSearch getBaseJournalById(final long jnlId) throws ServiceException {

    return this.baseJournalSearchDao.get(jnlId);
  }
}
