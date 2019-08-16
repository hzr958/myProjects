package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.BaseJournalDao;
import com.smate.center.batch.dao.sns.pub.BaseJournalSnsDao;
import com.smate.center.batch.dao.sns.pub.JournalSnsDao;
import com.smate.center.batch.dao.sns.pub.JournalSnsNoSeqDao;
import com.smate.center.batch.enums.pub.JournalStatus;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.AcJournal;
import com.smate.center.batch.model.pdwh.pub.BaseJournal2;
import com.smate.center.batch.model.pdwh.pub.Journal;
import com.smate.center.batch.model.pdwh.pub.JournalNoSeq;
import com.smate.center.batch.model.sns.pub.BaseJournalSns;
import com.smate.center.batch.model.sns.pub.JournalSns;
import com.smate.center.batch.model.sns.pub.JournalSnsNoSeq;
import com.smate.center.batch.service.pdwh.pub.JournalNoSeqService;
import com.smate.center.batch.service.pdwh.pub.JournalToSnsRefreshService;
import com.smate.center.batch.util.pub.JsonUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;


/**
 * sns 冗余期刊Service实现类
 * 
 * @author tsz
 * 
 */
@Service("journalSnsService")
@Transactional(rollbackFor = Exception.class)
public class JournalSnsServiceImpl implements JournalSnsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private JournalSnsDao journalSnsDao;

  @Autowired
  private JournalSnsNoSeqDao journalSnsNoSeqDao;

  @Autowired
  private BaseJournalSnsDao baseJournalSnsDao;


  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private JournalNoSeqService journalNoSeqService;

  @Override
  public JournalSns getById(long jid) throws BatchTaskException {
    return this.journalSnsDao.get(jid);

  }

  @Override
  public JournalSns findJournalByNameIssn(String jname, String issn, Long psnId) throws BatchTaskException {
    JournalSns ret = null;
    try {
      String nameAlias = JnlFormateUtils.getStrAlias(jname);
      issn = XmlUtil.buildStandardIssn(issn);
      List<JournalSns> jList = this.journalSnsDao.queryJournalByNameIssn(issn, nameAlias, psnId);
      if (CollectionUtils.isNotEmpty(jList)) {
        for (JournalSns journal : jList) {
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
      throw new BatchTaskException(e);
    }
    return ret;
  }

  @Override
  public JournalSns addJournal(String jname, String jissn, long currentUserId, String from) throws BatchTaskException {

    jissn = XmlUtil.buildStandardIssn(jissn);
    JournalSns jsns = new JournalSns();
    jsns.setIssn(jissn);
    if (XmlUtil.isChinese(jname)) {
      jsns.setZhName(jname.trim());
    } else {
      jsns.setEnName(jname.trim());
    }
    jsns.setAddPsnId(currentUserId);
    try {
      this.journalSnsDao.addJournal(jsns);
    } catch (DaoException e) {
      logger.error("addJournal失败,jname=" + jname + ", jissn=" + jissn + ", from=" + from, e);
      throw new BatchTaskException(e);
    }
    // 需要发消息同步 pdwh数据 tsz_2014.11.17
    JournalNoSeq j = new JournalNoSeq();
    j.setId(jsns.getId());
    j.setIssn(jissn);
    if (XmlUtil.isChinese(jname)) {
      j.setZhName(jname.trim());
    } else {
      j.setEnName(jname.trim());
    }
    j.setAddPsnId(currentUserId);
    j.setFromFlag(from);
    // lqh add状态
    j.setStatus(JournalStatus.REGISTERED);
    j.setRegDate(new Date());
    try {
      // 新增期刊时匹配基础期刊
      Long baseJnlId = null;
      if (StringUtils.isNotBlank(jname)) {
        baseJnlId = baseJournalDao.snsJnlMatchBaseJnlId(jname, jissn);
      }
      if (baseJnlId != null) {
        j.setMatchBaseJnlId(baseJnlId);
        jsns.setMatchBaseJnlId(baseJnlId);
        BaseJournal2 bjnl = baseJournalDao.getBaseJournal2(baseJnlId);
        if (XmlUtil.isChinese(jname)) {
          j.setZhName(jname);
          jsns.setZhName(jname);
        } else {
          j.setEnName(jname);
          jsns.setEnName(jname);
        }
      }
      j.setMatchBaseStatus(1);
      jsns.setMatchBaseStatus(1);
      // journalSysToPdwhProducer.sendJournalToPdwh(j);
      journalNoSeqService.addJournal(j);

    } catch (Exception e) {
      logger.error("发送同步期刊消息失败");
      throw new BatchTaskException(e);
    }

    return jsns;
  }

  @Override
  public void addJournalSnsFromPdwh(Journal j, JournalToSnsRefreshService journalToSnsRefreshService) throws Exception {
    try {
      if (j == null) {
        return;
      }
      JournalSnsNoSeq jsns = buildJournalSnsNoSeqObject(j);
      // journalSnsDao.addJournal(jsns);
      // 判断对象是否存在
      if (journalSnsDao.get(j.getId()) != null) {
        journalSnsNoSeqDao.getSession().update(jsns);
      } else {
        journalSnsNoSeqDao.getSession().save(jsns);
      }
      // 更新状态
      journalToSnsRefreshService.updateRefresh(j.getId());
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e);
    }
  }

  /**
   * 更具 journal 构造journalSns
   * 
   * @param j
   */
  private JournalSns buildJournalSnsObject(Journal j) {
    JournalSns jsns = new JournalSns();
    jsns.setId(j.getId());
    jsns.setIssn(j.getIssn());
    jsns.setEnameAlias(j.getEnameAlias());
    jsns.setEnName(j.getEnName());
    jsns.setAddPsnId(j.getAddPsnId());
    jsns.setMatchBaseJnlId(j.getMatchBaseJnlId());
    jsns.setZhName(j.getZhName());
    jsns.setZhNameAlias(j.getZhNameAlias());
    return jsns;
  }

  /**
   * 更具 journal 构造journalSns
   * 
   * @param j
   */
  private JournalSnsNoSeq buildJournalSnsNoSeqObject(Journal j) {
    JournalSnsNoSeq jsns = new JournalSnsNoSeq();
    jsns.setId(j.getId());
    jsns.setIssn(j.getIssn());
    jsns.setEnameAlias(j.getEnameAlias());
    jsns.setEnName(j.getEnName());
    jsns.setAddPsnId(j.getAddPsnId());
    jsns.setMatchBaseJnlId(j.getMatchBaseJnlId());
    jsns.setZhName(j.getZhName());
    jsns.setZhNameAlias(j.getZhNameAlias());
    return jsns;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<AcJournal> getAcJournalList(String startWith, int size, Long uid) throws BatchTaskException {
    try {
      // List<AcJournal> newList = new ArrayList<AcJournal>();

      // List1 放缓存 tsz 2014.11.26
      List<AcJournal> list1 = null;
      String startWithNoBlank = startWith.replaceAll(" ", "");
      Serializable cachList1 = cacheService.get("baseJournalSns", startWithNoBlank);
      if (cachList1 != null) {
        list1 = (List<AcJournal>) cachList1;
      } else {
        list1 = baseJournalSnsDao.getAcJournal(startWith, size);
        cacheService.put("baseJournalSns", 60 * 10, startWithNoBlank, (Serializable) list1);
      }

      // 只查基础期刊 期刊查找先去掉 tsz_2014.11.27

      if (CollectionUtils.isNotEmpty(list1)) {
        return list1;
      }
      // if (CollectionUtils.isNotEmpty(list1) && list1.size() == 10) {
      // return list1;
      // } else {
      // // list2 缓存处理
      // List<AcJournal> list2 = null;
      // Serializable cachList2 = cacheService.get(uid.toString(),
      // startWithNoBlank);
      // if (cachList2 != null) {
      // list2 = (List<AcJournal>) cachList2;
      // } else {
      // list2 = journalSnsDao.getAcJournalByPsn(startWith, size -
      // list1.size(), uid);
      // cacheService.put(uid.toString(), 60 * 10, startWithNoBlank,
      // (Serializable) list2);
      // }
      // if (CollectionUtils.isNotEmpty(list1)) {
      // for (AcJournal acJournal : list1) {
      // newList.add(acJournal);
      // }
      // }
      // if (CollectionUtils.isNotEmpty(list2)) {
      // for (AcJournal acJournal : list2) {
      // newList.add(acJournal);
      // }
      // }
      // }
      // return newList;
    } catch (DaoException e) {
      logger.error("getAcJournal 获取智能匹配期刊列表，只读size条记录出错startWith:" + startWith, e);
    }
    return null;
  }

  /**
   * 在成果录入过程中通过ajax添加期刊.
   * 
   * @param name
   * @param issn
   * @return
   */
  @Override
  public String ajaxAddJournalByPubEnter(String name, String issn) throws BatchTaskException {
    issn = XmlUtil.buildStandardIssn(issn);
    return ajaxAddJournalByPubEnter(name, issn, SecurityUtils.getCurrentUserId());
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
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public String ajaxAddJournalByPubEnter(String name, String issn, Long uid) throws BatchTaskException {
    List<JournalSns> list = null;
    issn = XmlUtil.buildStandardIssn(issn);
    list = listByNameIssn(name, issn);
    // 需要排除的属性
    String[] excludes = new String[] {"zhTwName", "status", "enameAlias", "listSCIE", "listSCI", "listAHCI", "listSSCI",
        "listEI", "listISTP", "listISR", "latestJid", "latestImpactFactorYear", "latestImpactFactor", "addPsnId",
        "fromFlag", "latestRImpactFactor"};
    JsonConfig jsonConfig = JsonUtils.configJson(excludes);
    // 如果不重复，则添加
    if (list == null || list.size() == 0) {
      JournalSns journal = addJournal(name, issn, uid, "enter");

      Map map = new HashMap();
      map.put("result", "success");
      map.put("data", journal);
      JSON json = JSONSerializer.toJSON(map, jsonConfig);
      return json.toString();

    } else {// 如果重复，则返回重复结果集
      boolean isContinueAdd = true;
      for (JournalSns jnl : list) {
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

  private List<JournalSns> listByNameIssn(String jname, String issn) {
    issn = XmlUtil.buildStandardIssn(issn);
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    List<JournalSns> jnlList = new ArrayList<JournalSns>();
    List<Long> mbjIdList = this.journalSnsDao.queryJournalByBJId(issn, nameAlias);
    if (CollectionUtils.isNotEmpty(mbjIdList)) {
      List<BaseJournalSns> baseJournalSnss = this.baseJournalSnsDao.findBaseJournalSns(mbjIdList);
      for (BaseJournalSns bj : baseJournalSnss) {
        JournalSns jnl = new JournalSns();
        jnl.setZhName(bj.getTitleXx());
        jnl.setEnName(bj.getTitleEn());
        jnl.setIssn(bj.getPissn());
        jnl.setMatchBaseJnlId(bj.getJouId());
        jnlList.add(jnl);
      }
    }
    List<JournalSns> jList = this.journalSnsDao.queryJournalByPsn(issn, nameAlias, SecurityUtils.getCurrentUserId());
    for (JournalSns journal : jList) {
      jnlList.add(journal);
    }
    return jnlList;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public String ajaxAddJnlContinue(String name) throws BatchTaskException {
    try {
      JournalSns journal = this.addJournal(name, null, SecurityUtils.getCurrentUserId(), "enter");
      Map map = new HashMap();
      map.put("result", "success");
      map.put("data", journal);
      JSON json = JSONSerializer.toJSON(map);
      return json.toString();
    } catch (Exception e) {
      logger.error("成果编辑录入期刊新增，点击继续新增出错", e);
      Map map = new HashMap();
      map.put("result", "error");
      JSON json = JSONSerializer.toJSON(map);
      return json.toString();
    }
  }

}
