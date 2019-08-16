package com.smate.center.batch.service.psn;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnScoreDetailDao;
import com.smate.center.batch.dao.sns.psn.PsnScoreInitDao;
import com.smate.center.batch.dao.sns.psn.PsnScoreRefreshDao;
import com.smate.center.batch.dao.sns.pub.ConstPsnScoreDao;
import com.smate.center.batch.dao.sns.pub.PsnDisciplineKeyDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnScoreDetail;
import com.smate.center.batch.model.sns.pub.PsnScoreInit;
import com.smate.center.batch.model.sns.pub.PsnScoreRefresh;
import com.smate.center.batch.service.pub.MyPublicationQueryService;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * 人员信息完整度service实现.
 * 
 * @author chenxiangrong
 * 
 */
@Service(value = "psnScoreService")
@Transactional(rollbackFor = Exception.class)
public class PsnScoreServiceImpl implements PsnScoreService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonManager personManager;
  @Autowired
  private ConstPsnScoreDao constPsnScoreDao;
  @Autowired
  private PsnScoreDetailDao psnScoreDetailDao;
  @Autowired
  private PsnScoreInitDao psnScoreInitDao;
  @Autowired
  private PsnScoreRefreshDao psnScoreRefreshDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private PsnProfileUrlService psnProfileUrlService;
  @Autowired
  private PsnPrivateService psnPrivateService;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private PublicationService publicationService;
  // @Resource(name = "snsSrvServiceLocator")
  // private SnsSrvServiceLocator snsSrvServiceLocator;

  @Override
  public void delScoreDetail(PsnScoreDetail psnScoreDetail) throws ServiceException {
    psnScoreDetailDao.delete(psnScoreDetail);
  }

  @Override
  public void delScoreInit(PsnScoreInit psnScoreInit) throws ServiceException {
    psnScoreInitDao.delete(psnScoreInit);
  }

  @Override
  public List<PsnScoreInit> getpsnScoreInit(Long psnId) throws ServiceException {
    try {
      return psnScoreInitDao.getpsnScoreInit(psnId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void delScoreRefresh(PsnScoreRefresh psnScoreRefresh) throws ServiceException {
    psnScoreRefreshDao.delete(psnScoreRefresh);
  }

  @Override
  public List<PsnScoreRefresh> getPsnScoreRefresh(Long psnId) throws ServiceException {
    try {
      return psnScoreRefreshDao.getPsnScoreRefresh(psnId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * 获取要刷新个人信息计分列表.
   */
  @Override
  public List<PsnScoreRefresh> getPsnScoreRefreshList(int maxSize) throws ServiceException {
    try {
      List<PsnScoreRefresh> refreshList = psnScoreRefreshDao.getRefreshRecords(maxSize);
      return refreshList;
    } catch (DaoException e) {
      logger.error("获取要刷新个人信息计分列表时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取要初始化的列表.
   */
  @Override
  public List<PsnScoreInit> getPsnScoreInitList(int maxSize) throws ServiceException {
    try {
      List<PsnScoreInit> initList = psnScoreInitDao.getRefreshRecords(maxSize);
      return initList;
    } catch (DaoException e) {
      logger.error("获取要初始个人信息计分列表时出错啦！", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 获取姓名信息.
   * 
   * @param person
   * @param langFlag
   * @return
   */
  private String getPsnNameByLang(Person person, int langFlag) {
    if (person == null)
      return null;
    String psnName = "";
    if (langFlag == 1) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  /**
   * 获取人员信息得分详情.
   */
  @Override
  public List<PsnScoreDetail> getPsnScoreDetail(Long psnId) throws ServiceException {
    try {
      return this.psnScoreDetailDao.getPsnScoreDetailByPsn(psnId);
    } catch (DaoException e) {
      logger.error("获取个人信息得分详情时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 删除初始化记录.
   */
  @Override
  public void deletePsnScoreInit(Long psnId) throws ServiceException {
    this.psnScoreInitDao.delete(psnId);
  }

  /**
   * 删除刷新记录.
   */
  @Override
  public void deletePsnScoreRefresh(Long psnId) throws ServiceException {
    this.psnScoreRefreshDao.delete(psnId);
  }

  /**
   * 保存需初始化计分的人员记录.
   * 
   * @param psnId
   */
  @Override
  public void savePsnScoreInit(Long psnId) {
    PsnScoreInit psnScoreInit = new PsnScoreInit();
    psnScoreInit.setPsnId(psnId);
    psnScoreInit.setUpdateDate(new Date());
    this.psnScoreInitDao.save(psnScoreInit);
  }
}
