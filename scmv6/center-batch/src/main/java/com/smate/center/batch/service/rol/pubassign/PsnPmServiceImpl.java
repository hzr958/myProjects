package com.smate.center.batch.service.rol.pubassign;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.psn.PsnPmJournalRolDao;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.PsnPmCnkiConameDao;
import com.smate.center.batch.dao.rol.pub.PsnPmCnkiNameDao;
import com.smate.center.batch.dao.rol.pub.PsnPmCoemailRolDao;
import com.smate.center.batch.dao.rol.pub.PsnPmConferenceDao;
import com.smate.center.batch.dao.rol.pub.PsnPmEmailRolDao;
import com.smate.center.batch.dao.rol.pub.PsnPmIsiConameDao;
import com.smate.center.batch.dao.rol.pub.PsnPmIsiNameDao;
import com.smate.center.batch.dao.rol.pub.PsnPmKeyWordRolDao;
import com.smate.center.batch.dao.rol.pub.PsnPmSpsConameDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignKwWtDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.PsnPmCnkiConame;
import com.smate.center.batch.model.rol.pub.PsnPmCnkiName;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PsnPmEmailRol;
import com.smate.center.batch.model.rol.pub.PsnPmIsiConame;
import com.smate.center.batch.model.rol.pub.PsnPmIsiName;
import com.smate.center.batch.model.rol.pub.PsnPmKeyWordRol;
import com.smate.center.batch.model.rol.pub.PsnPmSpsCoPreName;
import com.smate.center.batch.model.rol.pub.PsnPmSpsConame;
import com.smate.center.batch.model.rol.pub.ScmPubKeywordsSplit;
import com.smate.center.batch.model.rol.pub.SettingPubAssignKwWt;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScore;
import com.smate.center.batch.util.pub.PsnPmIsiNameUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果匹配用户信息service.
 * 
 * @author liqinghua
 * 
 */
@Service("psnPmService")
@Transactional(rollbackFor = Exception.class)
public class PsnPmServiceImpl implements PsnPmService {

  /**
   * 
   */
  private static final long serialVersionUID = -3513259750393399066L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private List<SettingPubAssignKwWt> kwWtList = null;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private PsnPmIsiNameDao psnPmIsiNameDao;
  @Autowired
  private PsnPmCnkiNameDao psnPmCnkiNameDao;
  @Autowired
  private PsnPmConferenceDao psnPmConferenceDao;
  @Autowired
  private PsnPmEmailRolDao psnPmEmailDao;
  @Autowired
  private PsnPmIsiConameDao psnPmIsiConameDao;
  @Autowired
  private PsnPmSpsConameDao psnPmSpsConameDao;
  @Autowired
  private PsnPmCnkiConameDao psnPmCnkiConameDao;
  @Autowired
  private PsnPmJournalRolDao psnPmJournalDao;
  @Autowired
  private PsnPmKeyWordRolDao psnPmKeyWordDao;
  @Autowired
  private SettingPubAssignKwWtDao settingPubAssignKwWtDao;
  @Autowired
  private SettingPubAssignScoreDao settingPubAssignScoreDao;
  @Autowired
  private PsnPmCoemailRolDao psnPmCoemailDao;

  @Override
  public PsnPmEmailRol savePsnPmEmail(String email, Long psnId) throws ServiceException {

    try {
      if (StringUtils.isBlank(email)) {
        return null;
      }
      email = email.trim().toLowerCase();
      PsnPmEmailRol psnPmEmail = psnPmEmailDao.getPsnPmEmail(email, psnId);
      if (psnPmEmail == null) {
        psnPmEmail = new PsnPmEmailRol(email, psnId);
        psnPmEmailDao.save(psnPmEmail);
      }
      return psnPmEmail;
    } catch (Exception e) {
      logger.error("添加用户确认邮件", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePsnPmEmail(String email, Long psnId) throws ServiceException {

    try {
      if (StringUtils.isBlank(email)) {
        return;
      }
      email = email.trim().toLowerCase();
      psnPmEmailDao.removePsnPmEmail(email, psnId);
    } catch (Exception e) {
      logger.error("删除用户邮件", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PsnPmConference savePsnPmConference(String name, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(name)) {
        return null;
      }
      name = name.trim().toLowerCase();
      Integer namehash = name.hashCode();
      PsnPmConference pc = this.psnPmConferenceDao.getPsnPmConference(namehash, psnId);
      if (pc == null) {
        pc = new PsnPmConference(name, namehash, psnId);
      } else {
        pc.setCcount(pc.getCcount() + 1);
      }
      this.psnPmConferenceDao.save(pc);
      return pc;
    } catch (Exception e) {
      logger.error("保存会议名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePsnPmIsiConame(String initName, String fullName, Long psnId) throws ServiceException {

    try {
      initName = StringUtils.trimToNull(initName);
      fullName = StringUtils.trimToNull(fullName);
      if (initName == null) {
        return;
      }
      boolean exist = this.psnPmIsiConameDao.isExitConame(initName, fullName, psnId);
      if (!exist) {
        PsnPmIsiConame pcn = new PsnPmIsiConame(initName, fullName, psnId);
        this.psnPmIsiConameDao.save(pcn);
      }
    } catch (Exception e) {
      logger.error("保存用户ISI合作者姓名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePsnPmSpsConame(String name, Long psnId) throws ServiceException {

    try {
      name = StringUtils.trimToNull(name);
      if (name == null) {
        return;
      }
      boolean exist = this.psnPmSpsConameDao.isExitConame(name, psnId);
      if (!exist) {
        PsnPmSpsConame pcn = new PsnPmSpsConame(name, psnId);
        this.psnPmSpsConameDao.save(pcn);
      }
    } catch (Exception e) {
      logger.error("保存用户scopus合作者姓名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void savePsnPmSpsCoPreName(String preName, Long psnId) throws ServiceException {
    try {
      preName = StringUtils.trimToNull(preName);
      if (preName == null) {
        return;
      }
      boolean exist = this.psnPmSpsConameDao.isExitCoPreName(preName, psnId);
      if (!exist) {
        PsnPmSpsCoPreName pcn = new PsnPmSpsCoPreName(preName, psnId);
        this.psnPmSpsConameDao.savePsnPmSpsCoPreName(pcn);
      }
    } catch (Exception e) {
      logger.error("保存用户scopus合作者姓名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PsnPmCnkiConame savePsnPmCnkiConame(String name, Long psnId) throws ServiceException {

    try {
      name = StringUtils.trimToNull(name);
      if (name == null) {
        return null;
      }
      PsnPmCnkiConame pcn = this.psnPmCnkiConameDao.getCnkiConame(name, psnId);
      if (pcn == null) {
        pcn = new PsnPmCnkiConame(name, psnId);
        this.psnPmCnkiConameDao.save(pcn);
      }
      return pcn;
    } catch (Exception e) {
      logger.error("保存用户cnki合作者姓名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveAddtPsnPmIsiName(String initName, String fullName, Long psnId) throws ServiceException {

    try {
      // 补充简名
      if (StringUtils.isNotBlank(initName)) {
        initName = initName.trim().toLowerCase();
        if (!this.psnPmIsiNameDao.isAddtNameExists(initName, psnId)) {
          PsnPmIsiName pn = new PsnPmIsiName(initName, psnId, 3);
          this.psnPmIsiNameDao.save(pn);
        }
      }
      // 补充全名
      if (StringUtils.isNotBlank(fullName)) {
        fullName = fullName.trim().toLowerCase();
        if (!this.psnPmIsiNameDao.isAddtNameExists(fullName, psnId)) {
          PsnPmIsiName pn = new PsnPmIsiName(fullName, psnId, 4);
          this.psnPmIsiNameDao.save(pn);
        }
      }
    } catch (Exception e) {
      logger.error("保存用户确认成果ISI名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveAddtPsnPmCnkiName(String name, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(name)) {
        return;
      }
      name = name.trim().toLowerCase();
      if (!this.psnPmCnkiNameDao.isAddtNameExists(name, psnId)) {
        PsnPmCnkiName pn = new PsnPmCnkiName(name, psnId, 2);
        this.psnPmCnkiNameDao.save(pn);
      }
    } catch (Exception e) {
      logger.error("保存用户确认成果CNKI名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PsnPmJournalRol savePsnPmJournal(Long jid, String jname, String issn, Long psnId) throws ServiceException {

    try {
      if (StringUtils.isBlank(jname)) {
        return null;
      }
      jname = jname.trim().toLowerCase();
      issn = StringUtils.isBlank(issn) ? null : issn.trim().toLowerCase();
      PsnPmJournalRol pj = this.psnPmJournalDao.getPsnPmJournal(jid, psnId);
      if (pj == null) {
        pj = new PsnPmJournalRol(jid, jname, issn, psnId);
      } else {
        pj.setJcount(pj.getJcount() + 1);
      }
      this.psnPmJournalDao.save(pj);
      return pj;
    } catch (Exception e) {
      logger.error("保存用户确认期刊信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PsnPmKeyWordRol savePsnPmKeyWord(String keyword, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(keyword)) {
        return null;
      }
      keyword = keyword.trim().toLowerCase();
      Integer kwhash = keyword.hashCode();
      keyword = StringUtils.substring(keyword, 0, 100);
      PsnPmKeyWordRol kw = this.psnPmKeyWordDao.getPsnPmKeyWord(kwhash, psnId);
      if (kw == null) {
        kw = new PsnPmKeyWordRol(keyword, kwhash, psnId);
      } else {
        kw.setKcount(kw.getKcount() + 1);
      }
      this.psnPmKeyWordDao.save(kw);
      return kw;
    } catch (Exception e) {
      logger.error("保存用户确认关键词信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<SettingPubAssignKwWt> getSettingPubAssignKwWt() throws ServiceException {

    try {
      if (kwWtList == null) {
        kwWtList = this.settingPubAssignKwWtDao.getSettingPubAssignKwWt();
      }
      return kwWtList;
    } catch (Exception e) {
      logger.error("获取关键词权重配置信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public SettingPubAssignScore getSettingPubAssignScore(Long dbid) throws ServiceException {
    try {
      return this.settingPubAssignScoreDao.getSettingPubAssignScore(dbid);
    } catch (Exception e) {
      logger.error("获取成果库指派分数配置表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void generalPsnPmIsiName(Long psnId) throws ServiceException {

    try {
      RolPsnIns psnIns = rolPsnInsDao.getRolPsnInsName(psnId);
      if (psnIns == null) {
        return;
      }
      String firstName = StringUtils.trimToNull(psnIns.getFirstName());
      String lastName = StringUtils.trimToNull(psnIns.getLastName());
      String otherName = StringUtils.trimToNull(psnIns.getOtherName());
      if (firstName == null || lastName == null) {
        return;
      }
      firstName = firstName.trim().toLowerCase();
      lastName = lastName.trim().toLowerCase();
      // 很多人的first name是连接在一起的，有必要进行拆分
      firstName = PsnPmIsiNameUtils.splitJoinFirstName(firstName, psnIns.getZhName());
      firstName = firstName.trim().toLowerCase();

      // 用户名前缀
      Set<String> prefixNames = PsnPmIsiNameUtils.buildPrefixName(firstName, lastName, otherName);
      if (CollectionUtils.isEmpty(prefixNames)) {
        return;
      } else {
        psnPmIsiNameDao.savePrefixName(prefixNames, psnId);
      }

      // initNames
      Set<String> initNames = PsnPmIsiNameUtils.buildInitName(firstName, lastName, otherName);
      if (CollectionUtils.isNotEmpty(initNames)) {
        psnPmIsiNameDao.saveInitName(initNames, psnId);
      }

      // fullNames
      Set<String> fullNames = PsnPmIsiNameUtils.buildFullName(firstName, lastName, otherName);
      if (CollectionUtils.isNotEmpty(fullNames)) {
        psnPmIsiNameDao.saveFullName(fullNames, psnId);
      }

    } catch (Exception e) {
      logger.error("生成用户isi名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void generalPsnPmIsiName(RolPsnIns psnIns) throws ServiceException {

    try {
      // RolPsnIns psnIns = rolPsnInsDao.getRolPsnInsName(psnId);
      if (psnIns == null) {
        return;
      }
      Long psnId = psnIns.getPk().getPsnId();
      String firstName = StringUtils.trimToNull(psnIns.getFirstName());
      String lastName = StringUtils.trimToNull(psnIns.getLastName());
      String otherName = StringUtils.trimToNull(psnIns.getOtherName());
      if (firstName == null || lastName == null) {
        return;
      }
      firstName = firstName.trim().toLowerCase();
      lastName = lastName.trim().toLowerCase();
      // 很多人的first name是连接在一起的，有必要进行拆分
      firstName = PsnPmIsiNameUtils.splitJoinFirstName(firstName, psnIns.getZhName());
      firstName = firstName.trim().toLowerCase();

      // 用户名前缀
      Set<String> prefixNames = PsnPmIsiNameUtils.buildPrefixName(firstName, lastName, otherName);
      if (CollectionUtils.isEmpty(prefixNames)) {
        return;
      } else {
        psnPmIsiNameDao.savePrefixName(prefixNames, psnId);
      }

      // initNames
      Set<String> initNames = PsnPmIsiNameUtils.buildInitName(firstName, lastName, otherName);
      if (CollectionUtils.isNotEmpty(initNames)) {
        psnPmIsiNameDao.saveInitName(initNames, psnId);
      }

      // fullNames
      Set<String> fullNames = PsnPmIsiNameUtils.buildFullName(firstName, lastName, otherName);
      if (CollectionUtils.isNotEmpty(fullNames)) {
        psnPmIsiNameDao.saveFullName(fullNames, psnId);
      }

    } catch (Exception e) {
      logger.error("生成用户isi名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, List<String>> getUserFullName(Set<Long> psnIds) throws ServiceException {

    try {
      return this.psnPmIsiNameDao.getUserFullName(psnIds);
    } catch (Exception e) {
      logger.error("获取用户全称列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveUserCnkiZhName(String zhName, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(zhName)) {
        return;
      }
      zhName = zhName.trim().toLowerCase();
      this.psnPmCnkiNameDao.saveUserZhName(zhName, psnId);
    } catch (Exception e) {
      logger.error("保存用户CNKI中文名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long generalHashPubKeywordsSplit(Long startId) throws ServiceException {

    try {
      List<ScmPubKeywordsSplit> list = this.psnPmKeyWordDao.loadScmPubKeywordsSplit(startId);
      if (list.size() == 0) {
        return 0L;
      }
      for (ScmPubKeywordsSplit split : list) {
        String keyWord = split.getKeyWord();
        keyWord = keyWord.trim().toLowerCase();
        keyWord = StringUtils.substring(keyWord, 0, 100);
        split.setKeyWord(keyWord);
        split.setKwHash(keyWord.hashCode());
        this.psnPmKeyWordDao.saveScmPubKeywordsSplit(split);
        startId = split.getId();
      }
      return startId;
    } catch (Exception e) {
      logger.error("保存用户CNKI中文名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePsnPmName(List<String> names, Long psnId) throws ServiceException {

    try {
      for (String name : names) {
        if (ServiceUtil.isChineseStr(name)) {
          this.psnPmCnkiNameDao.removeUserName(name.toLowerCase().trim(), psnId);
        } else {
          String tpname = XmlUtil.getCleanAuthorName(name);
          this.psnPmIsiNameDao.removeUserName(tpname.trim().toLowerCase(), psnId);
        }
      }
    } catch (Exception e) {
      logger.error("删除人员别名", e);
      throw new ServiceException("删除人员别名", e);
    }
  }

  @Override
  public boolean existsPsnPmName(Long psnId) throws ServiceException {

    try {
      return this.psnPmIsiNameDao.existsPsnPmName(psnId);
    } catch (Exception e) {
      logger.error("人员是否存在ISI别名", e);
      throw new ServiceException("人员是否存在ISI别名", e);
    }
  }

}
