package com.smate.web.v8pub.service.journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.v8pub.dao.journal.BaseJournalDAO;
import com.smate.web.v8pub.dao.journal.JournalDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.journal.BaseJournalPO;
import com.smate.web.v8pub.po.journal.JournalPO;
import com.smate.web.v8pub.utils.JsonUtils;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

/**
 * 期刊信息实现类
 * 
 * @author TSZ
 * 
 */
@Service("journalService")
@Transactional(rollbackFor = Exception.class)
public class JournalServiceImpl implements JournalService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 刚注册的.
   */
  public static final int REGISTERED = 0;
  /**
   * 通过审核的.
   */
  public static final int APPROVED = 1;
  /**
   * 已删除的.
   */
  public static final int DELETED = 2;

  @Autowired
  private JournalDAO journalSnsDao;
  @Autowired
  private BaseJournalDAO baseJournalDAO;
  @Autowired
  private BaseJournalService baseJournalService;

  @Override
  public JournalPO getById(Long jid) throws ServiceException {
    try {
      return journalSnsDao.get(jid);
    } catch (Exception e) {
      logger.error("根据jid获取期刊信息对象出错！jid={}", jid);
      throw new ServiceException(e);
    }
  }

  @Override
  public JournalPO findJournalByNameIssn(String jname, String issn, Long psnId) throws ServiceException {
    JournalPO ret = null;
    try {
      String nameAlias = JnlFormateUtils.getStrAlias(jname);
      issn = XmlUtil.buildStandardIssn(issn);
      List<JournalPO> jList = journalSnsDao.queryJournalByNameIssn(issn, nameAlias, psnId);
      if (CollectionUtils.isNotEmpty(jList)) {
        for (JournalPO journal : jList) {
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
      logger.error("期刊信息服务类：查询期刊出错！jname={},issn={},psnId={}", new Object[] {jname, issn, psnId}, e);
      throw new ServiceException(e);
    }
    return ret;
  }

  @Override
  public void save(JournalPO journalPO) throws ServiceException {
    try {
      journalSnsDao.save(journalPO);
    } catch (Exception e) {
      logger.error("保存期刊信息对象出错！journalPO={}", journalPO);
      throw new ServiceException(e);
    }
  }

  /**
   * 在成果录入过程中通过ajax添加期刊.
   * 
   * @param name
   * @param issn
   * @return
   * @throws ServiceException
   */
  @Override
  public String ajaxAddJournalByPubEnter(String name, String issn) throws ServiceException {
    issn = XmlUtil.buildStandardIssn(issn);
    return ajaxAddJournalByPubEnter(name, issn, SecurityUtils.getCurrentUserId());
  }

  @Override
  public String findImpactFactorsByJid(Long jid) {
    return journalSnsDao.findImpactFactors(jid);
  }

  /**
   * 在成果录入过程中通过ajax添加期刊. <br/>
   * 期刊入库原则：2010/12/08 <br/>
   * 所有用户公用一个期刊库，不再按用户区分.<br/>
   * 查找journal的逻辑 按jname和jissn的组合查询
   * 
   * @param name
   * @param issn
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public String ajaxAddJournalByPubEnter(String name, String issn, Long uid) throws ServiceException {
    List<JournalPO> list = null;
    issn = XmlUtil.buildStandardIssn(issn);
    list = listByNameIssn(name, issn);
    // 需要排除的属性
    String[] excludes = new String[] {"zhTwName", "status", "enameAlias", "listSCIE", "listSCI", "listAHCI", "listSSCI",
        "listEI", "listISTP", "listISR", "latestJid", "latestImpactFactorYear", "latestImpactFactor", "addPsnId",
        "fromFlag", "latestRImpactFactor"};
    JsonConfig jsonConfig = JsonUtils.configJson(excludes);
    // 如果不重复，则添加
    if (list == null || list.size() == 0) {
      // 进行标准期刊的匹配
      JournalPO journal = addJournal(name, issn, uid, "enter");

      Map map = new HashMap();
      map.put("result", "success");
      map.put("data", journal);
      JSON json = JSONSerializer.toJSON(map, jsonConfig);
      return json.toString();

    } else {// 如果重复，则返回重复结果集
      boolean isContinueAdd = true;
      for (JournalPO jnl : list) {
        if ((StringUtils.equalsIgnoreCase(jnl.getZhName(), name)
            || StringUtils.equalsIgnoreCase(jnl.getEnameAlias(), name.replace(" ", "")))
            && StringUtils.equalsIgnoreCase(jnl.getIssn(), issn) || StringUtils.isBlank(jnl.getIssn())) {
          isContinueAdd = false;
          break;
        }
      }
      Map map = new HashMap();
      map.put("result", "exist");
      map.put("isContinueAdd", isContinueAdd);
      map.put("data", list);
      map.put("count", list.size());
      JSON json = JSONSerializer.toJSON(map, jsonConfig);
      return json.toString();
    }
  }

  private List<JournalPO> listByNameIssn(String jname, String issn) {
    issn = XmlUtil.buildStandardIssn(issn);
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    List<JournalPO> jnlList = new ArrayList<JournalPO>();
    List<Long> mbjIdList = this.journalSnsDao.queryJournalByBJId(issn, nameAlias);
    if (CollectionUtils.isNotEmpty(mbjIdList)) {
      List<BaseJournalPO> baseJournalSnss = this.baseJournalDAO.findBaseJournalSns(mbjIdList);
      for (BaseJournalPO bj : baseJournalSnss) {
        JournalPO jnl = new JournalPO();
        jnl.setZhName(bj.getTitleXx());
        jnl.setEnName(bj.getTitleEn());
        jnl.setIssn(bj.getPissn());
        jnl.setMatchBaseJnlId(bj.getJouId());
        jnlList.add(jnl);
      }
    }
    List<JournalPO> jList = this.journalSnsDao.queryJournalByPsn(issn, nameAlias, SecurityUtils.getCurrentUserId());
    for (JournalPO journal : jList) {
      jnlList.add(journal);
    }
    return jnlList;
  }

  public JournalPO addJournal(String jname, String jissn, long currentUserId, String from) throws ServiceException {

    jissn = XmlUtil.buildStandardIssn(jissn);
    Long baseJid = baseJournalService.searchJnlMatchBaseJnlId(jname, jissn);
    JournalPO jsns = new JournalPO();
    jsns.setIssn(jissn);
    jname = StringUtils.substring(jname, 0, 250);
    if (XmlUtil.isChinese(jname)) {
      jsns.setZhName(jname.trim());
    } else {
      jsns.setEnName(jname.trim());
    }
    jsns.setAddPsnId(currentUserId);
    jsns.setMatchBaseJnlId(baseJid);
    jsns.setMatchBaseStatus(1);
    try {
      this.journalSnsDao.save(jsns);
    } catch (DAOException e) {
      logger.error("addJournal失败,jname=" + jname + ", jissn=" + jissn + ", from=" + from, e);
      throw new ServiceException(e);
    }
    return jsns;
  }

}
