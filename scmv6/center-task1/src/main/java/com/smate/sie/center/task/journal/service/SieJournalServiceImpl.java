package com.smate.sie.center.task.journal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.sie.center.task.journal.dao.SieJournalDao;
import com.smate.sie.center.task.journal.model.SieJournal;

/**
 * ROL-5562 成果表单，期刊名称下拉逻辑调整（包括基准库导入，文件导入）
 * 
 * @author sjzhou
 *
 */
@Service("sieJournalService")
@Transactional(rollbackFor = Exception.class)
public class SieJournalServiceImpl implements SieJournalService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieJournalDao sieJournalDao;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Override
  public List<SieJournal> querySieJournalList(String jname) throws Exception {
    try {
      // 调接口基准库查询基准期刊
      List<SieJournal> listFormPDWH = querySieJournalListFromPDWH(jname);
      List<SieJournal> newList = new ArrayList<SieJournal>();
      if (CollectionUtils.isNotEmpty(listFormPDWH) && listFormPDWH.size() == 10) {
        return listFormPDWH;
      } else {
        // 不够10条记录或者为空时，再到sie库查询当前单位添加的期刊
        List<SieJournal> listFromSIE =
            querySieJournalListFromSIE(jname, SecurityUtils.getCurrentInsId(), 10 - listFormPDWH.size());
        if (CollectionUtils.isNotEmpty(listFormPDWH)) {
          for (SieJournal journal : listFormPDWH) {
            newList.add(journal);
          }
        }
        if (CollectionUtils.isNotEmpty(listFromSIE)) {
          for (SieJournal journal : listFromSIE) {
            newList.add(journal);
          }
        }
      }
      return newList;
    } catch (Exception e) {
      logger.error("querySieJournalList 获取智能匹配期刊列表", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public SieJournal getSieJournalById(Long jid) throws SysServiceException {
    return sieJournalDao.get(jid);
  }

  /**
   * 获取pdwh库的基准期刊 base_journal. 调接口
   * 
   * @param startWith
   * @param size
   * @param uid
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("unchecked")
  public List<SieJournal> querySieJournalListFromPDWH(String param) throws SysServiceException {
    Object obj = querySieJournal(param);
    List<SieJournal> sieJournalList = new ArrayList<SieJournal>();
    if (obj != null) {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
      Object result = resultMap.get("result");
      if (result != null) {
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) result;
        for (Map<String, Object> map : listMap) {
          SieJournal journal = new SieJournal();
          Long jnlId = IrisNumberUtils.createLong(ObjectUtils.toString(map.get("jid")));
          String issn = ObjectUtils.toString(map.get("issn"));
          String jname = ObjectUtils.toString(map.get("jname"));
          if (StringUtils.isNotBlank(issn)) {
            journal.setIssn(issn);
            journal.setCode(jnlId + "~" + issn);
            journal.setInputName(jname);
            journal.setName(jname + "（" + (issn != null ? issn : "") + "）");
          } else {
            journal.setCode(jnlId.toString());
            journal.setName(jname);
            journal.setInputName(jname);
          }
          journal.setMatchBaseJnlId(jnlId);
          sieJournalList.add(journal);
        }
      }
    }
    return sieJournalList;

  }

  // 通过期刊名查询期刊
  public Object querySieJournal(String param) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("jname", param);
      mapDate.put("openid", 99999999L);// 系统默认openId
      mapDate.put("token", "11111111hguu65op");// 系统默认token
      mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    } catch (Exception e) {
      logger.error("获取基准库基准期刊错误", e);
      throw new SysServiceException(e);
    }
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 获取sie库的journal.
   * 
   * @param startWith
   * @param size
   * @param uid
   * @return
   * @throws ServiceException
   */
  public List<SieJournal> querySieJournalListFromSIE(String jname, Long currentInsId, int size)
      throws SysServiceException {
    List<SieJournal> list = sieJournalDao.queryJournalByName(jname, currentInsId, size);
    List<SieJournal> newList = new ArrayList<SieJournal>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (SieJournal sieJournal : list) {
        String issn = sieJournal.getIssn();
        String name = "";
        String zhName = sieJournal.getZhName();
        String enName = sieJournal.getEnName();
        name = StringUtils.isNotBlank(zhName) ? zhName : enName;
        Long jnlId = sieJournal.getMatchBaseJnlId();
        SieJournal journal = new SieJournal();
        if (StringUtils.isNotBlank(issn)) {
          journal.setIssn(issn);
          journal.setCode(jnlId + "~" + issn);
          journal.setInputName(name);
          journal.setName(name + "（" + (issn != null ? issn : "") + "）");
        } else {
          journal.setCode(jnlId.toString());
          journal.setName(name);
          journal.setInputName(name);
        }
        journal.setMatchBaseJnlId(jnlId);
        newList.add(journal);
      }
    }
    return newList;
  }

  /**
   * 保存成果调用
   */
  @Override
  public SieJournal addJournalByPubEnter(String jname, String issn, Long currentInsId) throws Exception {
    List<SieJournal> list = null;
    // 查重返回jid,否则在sie.journal新增一条记录。
    list = listByNameIssn(jname, issn, currentInsId);
    if (CollectionUtils.isEmpty(list)) {
      SieJournal journal = saveJournal(jname, issn, currentInsId);
      return journal;
    } else {
      return list.get(0);
    }
  }

  private List<SieJournal> listByNameIssn(String jname, String issn, Long insId) {
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    issn = XmlUtil.buildStandardIssn(issn);
    List<SieJournal> journalList = null;
    // 查重
    journalList = sieJournalDao.queryJournalByNameIssn(issn, nameAlias, insId);
    if (CollectionUtils.isEmpty(journalList)) {
      journalList = sieJournalDao.queryJournalByIns(issn, nameAlias, insId);
    }
    return journalList;
  }

  public SieJournal saveJournal(String jname, String jissn, Long currentInsId) throws Exception {
    jissn = XmlUtil.buildStandardIssn(jissn);
    Long baseJid = getBaseJournalJnlId(jname, jissn);
    SieJournal jsie = new SieJournal();
    jname = StringUtils.substring(jname, 0, 250);
    String nameAlias = JnlFormateUtils.getStrAlias(jname);
    jsie.setZhName(jname.trim());
    jsie.setZhNameAlias(nameAlias);
    jsie.setEnName(jname.trim());
    jsie.setEnameAlias(nameAlias);
    jsie.setIssn(jissn);
    jsie.setAddPsnId(SecurityUtils.getCurrentUserId());
    jsie.setAddInsId(currentInsId);
    jsie.setMatchBaseJnlId(baseJid);
    if (baseJid != null) {
      jsie.setMatchBaseStatus(1);
    } else {
      jsie.setMatchBaseStatus(0);
    }
    try {
      this.sieJournalDao.save(jsie);
    } catch (Exception e) {
      logger.error("saveJournal失败,jname=" + jname + ", jissn=" + jissn + ", isnId=" + currentInsId, e);
      throw new SysServiceException("saveJournal失败,jname=" + jname + ", jissn=" + jissn + ", isnId=" + currentInsId);
    }
    return jsie;
  }

  /**
   * 匹配基准库基准期刊base_journal
   */
  @Override
  public Long getBaseJournalJnlId(String journalName, String issn) throws Exception {
    try {
      if (StringUtils.isBlank(journalName))
        return null;
      // 调接口,返回jnlid
      Object obj = getJnlId(journalName, issn);
      if (obj != null) {
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(obj.toString());
        Object resultOjb = resultMap.get("result");
        if (resultOjb != null & resultOjb != "") {
          List result = (List) resultOjb;
          if (CollectionUtils.isNotEmpty(result)) {
            Map temp = (Map) result.get(0);
            Long baseJnlId = IrisNumberUtils.createLong(ObjectUtils.toString(temp.get("baseJnlId")));
            return baseJnlId;
          }
        }
      }
    } catch (Exception e) {
      logger.error("获取期刊错误", e);
      throw new SysServiceException("匹配基准期刊出错，jname=" + journalName + ", jissn=" + issn + "---日志信息：" + e.getMessage());
    }
    return null;
  }

  public Object getJnlId(String journalName, String issn) throws SysServiceException {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("jname", journalName);
    data.put("issn", issn);
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111fgg34oiu");// 系统默认token
    mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }
}
