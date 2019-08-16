package com.smate.center.batch.service.user;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.institution.InstitutionDao;
import com.smate.center.batch.dao.sns.psn.PsnScienceAreaDao;
import com.smate.center.batch.dao.sns.pub.PsnDisciplineKeyDao;
import com.smate.center.batch.dao.sns.pub.PubSnsDetailDAO;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.ScmPubXmlDao;
import com.smate.center.batch.dao.sns.wechat.OpenUserUnionDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Institution;
import com.smate.center.batch.service.psn.UserCacheService;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

/**
 * center-batch 系统用户 服务实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
  public static String INDEX_TYPE_PSN = "person_index";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserDao userDao;
  @Autowired
  private SysUserLoginDao sysUserLoginDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private UserCacheService userCacheService;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Value("${solr.server.url}")
  private String serverUrl;
  private String runEnv = System.getenv("RUN_ENV");

  @Override
  public Boolean isLoginNameExist(String loginName) {
    Assert.hasText(loginName, "loginName不允许为空");
    Long id = this.userDao.findIdByLoginName(loginName);
    if (id != null) {
      return true;
    }
    return false;
  }

  /**
   * 设置更新首要邮件，更新首要邮件将会将登陆名与首要邮件一致lqh.
   * 
   * @param email
   * @param psnId
   * @return 1成功设置邮件为首要邮件/登录帐号；2成果设置邮件为首要邮件，但是登录名已被其他用户占用;0更新失败
   * @throws ServiceException
   */
  public Integer updateFirstEmail(String email, Long psnId) throws Exception {
    Assert.hasText(email, "email不允许为空");
    Assert.notNull(psnId, "psnId不允许为空");
    Boolean result = this.userDao.isLoginNameUsed(email, psnId);
    User user = this.findUserById(psnId);

    // 已被使用
    if (result && user != null) {
      user.setEmail(email);
      this.saveUser(user);
      userCacheService.put(psnId, user);
      return 2;
      // 未被使用
    } else if (user != null) {
      user.setLoginName(email);
      user.setEmail(email);
      this.saveUser(user);
      userCacheService.put(psnId, user);
      return 1;
    }
    return 0;
  }

  @Override
  public User findUserById(Long psnId) throws Exception {
    Assert.notNull(psnId, "psnId不允许为空");
    User user = userCacheService.getCacheUser(psnId);
    if (user == null) {
      user = userDao.get(psnId);
      if (user != null) {
        userCacheService.put(psnId, user);
      }
    }
    return user;
  }

  @Override
  public List<User> findUserByLoginNameOrEmail(String email) {
    Assert.hasText(email, "email不允许为空");
    return this.userDao.findUserByLoginNameOrEmail(email);
  }

  /**
   * 保存用户信息.
   * 
   * @param user
   * @throws ServiceException
   */
  private void saveUser(User user) throws ServiceException {
    try {
      this.userDao.save(user);
      this.userCacheService.remove(user.getId());
    } catch (Exception e) {
      logger.error("保存用户信出错,psnId={}", user.getId());
      throw new ServiceException(e);
    }
  }

  @Override
  public List<User> findUsersNodeId(String psnIds) throws Exception {
    Assert.notNull(psnIds, "psnIds不允许为空");
    Assert.hasLength(psnIds, "收件人Id列表不允许为空！");
    try {
      return this.userDao.getUserNodeIds(psnIds);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public User findSystemUser() throws ServiceException {

    return this.findUserByLoginName("system");

  }

  @Override
  public User findUserByLoginName(String email) {
    Assert.hasText(email, "email不允许为空");
    return userDao.findByLoginName(email);
  }

  @Override
  public Long findIdByLoginName(String loginName) {
    Assert.hasText(loginName, "loginName不允许为空");
    return this.userDao.findIdByLoginName(loginName);
  }

  @Override
  public void saveUserLoginLog(Long userId, Long selfLogin, String loginIP) throws ServiceException {
    Assert.notNull(userId, "personId不允许为空");
    try {
      // 查找，不存在，更新
      SysUserLogin login = sysUserLoginDao.get(userId);
      if (login == null) {
        login = new SysUserLogin(userId);
      } else {
        login.setLastLoginTime(new Date());
      }
      if (selfLogin != null) {
        login.setSelfLogin(selfLogin);
      }
      if (loginIP != null) {
        login.setLoginIP(loginIP);
      }
      sysUserLoginDao.save(login);
    } catch (Exception e) {
      throw new ServiceException();
    }
  }

  @Override
  public void updateSolrPsnInfo(Long psnId) throws ServiceException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    try {
      SolrServer server = new HttpSolrServer(serverUrl);
      if (psnId == null || psnId <= 0L) {
        logger.error("更新用户solr索引，psnId为空");
        return;
      }
      server.deleteById("50" + psnId.toString());
      Person psn = this.personDao.get(psnId);
      if (psn == null) {
        server.commit();
        logger.error("更新用户solr索引，person表中数据为空, psnId = " + psnId + ", solr中对应索引已经删除");
        return;
      }
      SolrInputDocument doc = new SolrInputDocument();
      // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
      doc.setField("businessType", INDEX_TYPE_PSN);
      Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
      doc.setField("env", runEnv);
      doc.setField("id", Long.parseLong("50" + psn.getPersonId()));
      doc.setField("psnId", psn.getPersonId());
      doc.setField("psnName", psn.getName());
      doc.setField("enPsnName", psn.getEname());
      // 人员信息完整度
      doc.setField("psnInfoIntegrity", psn.getComplete());
      // 判断是否是isis注册过的用户
      boolean isIsisRegistered = openUserUnionDao.ifRegisteredInTheIns(psn.getPersonId(), "2bcca485");
      if (isIsisRegistered) {
        doc.setField("psnIsisReg", 1);
      } else {
        doc.setField("psnIsisReg", 0);
      }
      if (StringUtils.isBlank(psn.getPosition()) || StringUtils.isBlank(psn.getTitolo())) {
        doc.setField("title", (StringUtils.isBlank(psn.getPosition()) ? "" : psn.getPosition())
            + (StringUtils.isBlank(psn.getTitolo()) ? "" : psn.getTitolo()));
      } else {
        doc.setField("title", psn.getPosition() + ", " + psn.getTitolo());
      }
      // 个人成果中的关键词
      this.getPsnKwsByPubKws(psn.getPersonId(), doc);
      // 人员自填关键词
      List<String> keywords = psnDisciplineKeyDao.getPsnKeywords(psn.getPersonId());
      String psnDisciplineKeyStr = "";
      if (CollectionUtils.isNotEmpty(keywords)) {
        psnDisciplineKeyStr = StringUtils.lowerCase(keywords.toString());
      }
      doc.setField("psnDisciplineKey", psnDisciplineKeyStr);
      // 添加人员科技领域
      List<Long> psnScienceAreaList = this.psnScienceAreaDao.findPsnScienceAreaIds(psn.getPersonId());
      if (psnScienceAreaList != null && psnScienceAreaList.size() > 0) {
        doc.setField("psnScienceArea", psnScienceAreaList.toArray());
      }
      // 获取人员的专利数/项目数
      PsnStatistics psnStatistics = psnStatisticsDao.getPsnStatistics(psn.getPersonId());
      doc.setField("psnPatentCount", psnStatistics.getPatentSum());
      doc.setField("psnPrjCount", psnStatistics.getPrjSum());
      doc.setField("psnPubCount", psnStatistics.getPubSum());
      // 人员是否在隐私列表中
      if (this.psnPrivateDao.existsPsnPrivate(psn.getPersonId())) {
        doc.setField("isPrivate", 1);
      } else {
        doc.setField("isPrivate", 0);
      }
      if (psn.getInsId() != null) {
        Institution ins = institutionDao.get(psn.getInsId());
        if (ins != null) {
          doc.setField("zhInsName", StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName());
          doc.setField("enInsName", StringUtils.isNotBlank(ins.getEnName()) ? ins.getEnName() : ins.getZhName());
        }
      } else if (StringUtils.isNotEmpty(psn.getInsName())) {
        doc.setField("zhInsName", psn.getInsName());
      }
      doc.setField("psnRegionId", psn.getRegionId());
      doc.setField("zhUnit", psn.getDepartment());
      // openid
      Long openId = openUserUnionDao.getOpenIdByPsnId(psn.getPersonId());
      doc.setField("openId", openId);
      server.add(doc);
      server.commit();
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  private void getPsnKwsByPubKws(Long personId, SolrInputDocument doc) {
    List<Long> pubIdList = psnPubDAO.getPubIdsByPsnId(personId);
    StringBuilder keywords = new StringBuilder();
    StringBuilder patKeywords = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        PubSnsDetailDOM detail = pubSnsDetailDAO.findByPubId(pubId);
        if (5 == detail.getPubType()) {
          patKeywords.append(detail.getSummary());
          patKeywords.append(detail.getTitle());
        }
        keywords.append(detail.getKeywords());
      }
    }
    doc.setField("psnKeywords", keywords.toString());
    doc.setField("psnPatKeywords", patKeywords.toString());
  }

  @Override
  public void deleteSolrPsnInfo(Long psnId) throws ServiceException {
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    try {
      SolrServer server = new HttpSolrServer(serverUrl);
      if (psnId == null || psnId <= 0L) {
        logger.error("更新用户solr索引，psnId为空");
        return;
      }
      server.deleteById("50" + psnId.toString());
      server.commit();
      logger.info("Solr人员索引信息成功删除，psnId = " + psnId);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

}
