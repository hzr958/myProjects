package com.smate.center.task.service.rol.quartz;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PsnPmCnkiConameDao;
import com.smate.center.task.dao.rol.quartz.PsnPmCnkiNameDao;
import com.smate.center.task.dao.rol.quartz.PsnPmConferenceDao;
import com.smate.center.task.dao.rol.quartz.PsnPmIsiConameDao;
import com.smate.center.task.dao.rol.quartz.PsnPmIsiNameDao;
import com.smate.center.task.dao.rol.quartz.PsnPmJournalDao;
import com.smate.center.task.dao.rol.quartz.PsnPmKeyWordDao;
import com.smate.center.task.dao.rol.quartz.PsnPmSpsCoPreNameDao;
import com.smate.center.task.dao.rol.quartz.PsnPmSpsConameDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PsnPmCnkiConame;
import com.smate.center.task.model.rol.quartz.PsnPmCnkiName;
import com.smate.center.task.model.rol.quartz.PsnPmConference;
import com.smate.center.task.model.rol.quartz.PsnPmIsiConame;
import com.smate.center.task.model.rol.quartz.PsnPmIsiName;
import com.smate.center.task.model.rol.quartz.PsnPmJournal;
import com.smate.center.task.model.rol.quartz.PsnPmKeyWord;
import com.smate.center.task.model.rol.quartz.PsnPmSpsCoPreName;
import com.smate.center.task.model.rol.quartz.PsnPmSpsConame;

@Service("psnPmService")
@Transactional(rollbackFor = Exception.class)
public class PsnPmServiceInpl implements PsnPmService {
  @Autowired
  private PsnPmIsiNameDao psnPmIsiNameDao;
  @Autowired
  private PsnPmIsiConameDao psnPmIsiConameDao;
  @Autowired
  private PsnPmSpsConameDao psnPmSpsConameDao;
  @Autowired
  private PsnPmSpsCoPreNameDao psnPmSpsCoPreNameDao;
  @Autowired
  private PsnPmCnkiNameDao psnPmCnkiNameDao;
  @Autowired
  private PsnPmCnkiConameDao psnPmCnkiConameDao;
  @Autowired
  private PsnPmJournalDao psnPmJournalDao;
  @Autowired
  private PsnPmConferenceDao psnPmConferenceDao;
  @Autowired
  private PsnPmKeyWordDao psnPmKeyWordDao;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void saveAddtPsnPmIsiName(String initName, String fullName, Long psnId) throws ServiceException {
    try {
      // 补充简名
      if (StringUtils.isNotBlank(initName)) {
        initName = initName.trim().toLowerCase();
        if (!psnPmIsiNameDao.isAddtNameExists(initName, psnId)) {
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
  public void savePsnPmIsiConame(String initName, String fullName, Long psnId) throws ServiceException {
    try {
      initName = StringUtils.trimToNull(initName);
      fullName = StringUtils.trimToNull(fullName);
      if (initName == null) {
        return;
      }
      boolean exist = psnPmIsiConameDao.isExitConame(initName, fullName, psnId);
      if (!exist) {
        PsnPmIsiConame pcn = new PsnPmIsiConame(initName, fullName, psnId);
        psnPmIsiConameDao.save(pcn);
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
      boolean exist = psnPmSpsConameDao.isExitConame(name, psnId);
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
      boolean exist = psnPmSpsConameDao.isExitCoPreName(preName, psnId);
      if (!exist) {
        PsnPmSpsCoPreName pcn = new PsnPmSpsCoPreName(preName, psnId);
        psnPmSpsCoPreNameDao.savePsnPmSpsCoPreName(pcn);
      }
    } catch (Exception e) {
      logger.error("保存用户scopus合作者姓名", e);
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
      if (!psnPmCnkiNameDao.isAddtNameExists(name, psnId)) {
        PsnPmCnkiName pn = new PsnPmCnkiName(name, psnId, 2);
        psnPmCnkiNameDao.save(pn);
      }

    } catch (Exception e) {
      logger.error("保存用户确认成果CNKI名称", e);
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
      PsnPmCnkiConame pcn = psnPmCnkiConameDao.getCnkiConame(name, psnId);
      if (pcn == null) {
        pcn = new PsnPmCnkiConame(name, psnId);
        psnPmCnkiConameDao.save(pcn);
      }
      return pcn;
    } catch (Exception e) {
      logger.error("保存用户cnki合作者姓名", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PsnPmJournal savePsnPmJournal(Long jid, String jname, String issn, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(jname)) {
        return null;
      }
      jname = jname.trim().toLowerCase();
      issn = StringUtils.isBlank(issn) ? null : issn.trim().toLowerCase();
      PsnPmJournal pj = psnPmJournalDao.getPsnPmJournal(jid, psnId);
      if (pj == null) {
        pj = new PsnPmJournal(jid, jname, issn, psnId);
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
  public PsnPmConference savePsnPmConference(String name, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(name)) {
        return null;
      }
      name = name.trim().toLowerCase();
      Integer namehash = name.hashCode();
      PsnPmConference pc = psnPmConferenceDao.getPsnPmConference(namehash, psnId);
      if (pc == null) {
        pc = new PsnPmConference(name, namehash, psnId);
      } else {
        pc.setCcount(pc.getCcount() + 1);
      }
      psnPmConferenceDao.save(pc);
      return pc;
    } catch (Exception e) {
      logger.error("保存会议名称", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PsnPmKeyWord savePsnPmKeyWord(String keyword, Long psnId) throws ServiceException {
    try {
      if (StringUtils.isBlank(keyword)) {
        return null;
      }
      keyword = keyword.trim().toLowerCase();
      Integer kwhash = keyword.hashCode();
      keyword = StringUtils.substring(keyword, 0, 100);
      PsnPmKeyWord kw = psnPmKeyWordDao.getPsnPmKeyWord(kwhash, psnId);
      if (kw == null) {
        kw = new PsnPmKeyWord(keyword, kwhash, psnId);
      } else {
        kw.setKcount(kw.getKcount() + 1);
      }
      psnPmKeyWordDao.save(kw);
      return kw;

    } catch (Exception e) {
      logger.error("保存用户确认关键词信息", e);
      throw new ServiceException(e);
    }
  }

}
