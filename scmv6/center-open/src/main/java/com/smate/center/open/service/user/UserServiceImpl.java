package com.smate.center.open.service.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.sie.publication.InsUnitDao;
import com.smate.center.open.dao.sie.publication.RolPsnInsDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.sie.publication.InsUnit;
import com.smate.center.open.model.sie.publication.RolPsnIns;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.dao.security.AutoLoginOauthInfoDao;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.dao.security.SysRolUserDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.cas.security.AutoLoginOauthInfo;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * open系统 用户账号密码验证服务
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  public static String INDEX_TYPE_PUB = "publication_index";
  public static String INDEX_TYPE_PAT = "patent_index";
  public static String INDEX_TYPE_PSN = "person_index";
  public static String INDEX_TYPE_FUND = "fund_index";
  public static String INDEX_TYPE_KW = "keywords_index";

  @Autowired
  private UserDao userDao;

  @Autowired
  private SysRolUserDao sysRolUserDao;
  @Autowired
  private PsnPubDAO psnPubDAO;

  @Autowired
  private PersonDao personDao;

  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private PsnPrivateDao psnPrivateDao;

  @Autowired
  private InsPortalDao insPortalDao;

  @Autowired
  private RolPsnInsDao rolPsnInsDao;

  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private AutoLoginOauthInfoDao autoLoginOauthInfoDao;

  @Value("${solr.server.url}")
  private String serverUrl;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  private String runEnv = System.getenv("RUN_ENV");

  @Override
  public User getUser(String userName, String password) throws OpenException {
    User user = userDao.getUser(userName, password);
    if (user != null)
      return user;
    return getUserByOpenId(userName, password);
  }

  /**
   * 通过openId 查询
   * 
   * @param userName
   * @param password
   * @return
   */
  public User getUserByOpenId(String userName, String password) {
    if (NumberUtils.isDigits(userName)) {
      OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnionByOpenId(NumberUtils.toLong(userName));
      if (openUserUnion != null) {
        User user = userDao.getUserByPsnId(openUserUnion.getPsnId(), password);
        return user;
      }
    }
    return null;
  }

  public User getUserByUsername(String userName) throws OpenException {
    return userDao.getUserByUsername(userName);
  }

  @Override
  public Long getRolUserByGuid(SysRolUser rolUser) throws Exception {
    if (rolUser != null)
      return sysRolUserDao.getRolUserByGuid(rolUser);
    return null;
  }

  @Override
  public List<Long> getConnectedPsnByGuid(String guid) throws Exception {
    try {
      return this.sysRolUserDao.queryConnectedPsnByGuid(guid);
    } catch (Exception e) {
      throw new Exception("通过guid={1}查询关联的用户ID出现异常：", e);
    }
  }

  @Override
  @Deprecated
  public SysRolUser getSysRolUser(String guid, Long psnId) throws Exception {
    try {
      return this.sysRolUserDao.querySysRolUser(guid, psnId);
    } catch (Exception e) {
      throw new Exception(String.format("通过guid={1}和psnId={2}查找关联用户记录出现异常：", guid, psnId), e);
    }
  }

  @Override
  public void saveSysRolUser(SysRolUser sysRolUser) throws Exception {
    try {
      if (sysRolUser != null) {
        // 设置关联
        SysRolUser sysRolUser2 = this.sysRolUserDao.findSysRolUser(sysRolUser.getGuid(), sysRolUser.getInsId());
        if (sysRolUser2 != null) {
          sysRolUser2.setPsnId(sysRolUser.getPsnId());
          sysRolUser2.setFlag(sysRolUser.getFlag());
          sysRolUser2.setLastDate(new Date());
        } else {
          this.sysRolUserDao.save(sysRolUser);
        }
      }
    } catch (Exception e) {
      throw new Exception("设置关联smate账号出错", e);
    }
  }

  /**
   * 更新人员solr信息统一在center-batch处理
   */
  @Override
  @Deprecated
  public void updateSolrPsnInfo(Long psnId) throws Exception {
    // serverUrl = "http://192.168.15.192:28080/solr/";
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    if (psnId == null || psnId <= 0L) {
      logger.error("更新用户solr索引，psnId为空");
      return;
    }
    Long solrPsnId = this.generateIdForIndex(psnId, INDEX_TYPE_PSN);
    server.deleteById(String.valueOf(solrPsnId));

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
    doc.setField("id", generateIdForIndex(psn.getPersonId(), INDEX_TYPE_PSN));
    doc.setField("psnId", psn.getPersonId());
    doc.setField("psnName", psn.getName());
    doc.setField("enPsnName", psn.getEname());
    doc.setField("title", psn.getPosition());
    // 人员信息完整度
    doc.setField("psnInfoIntegrity", psn.getComplete());

    // 获取个人成果中的关键词 与个人成果数
    // 获取个人成果中的关键词
    Map<String, Object> rsMap = this.getPsnKwsByPubKws(psn.getPersonId());
    if (rsMap != null) {
      String kwsStr = (String) rsMap.get("kwsStr");
      doc.setField("psnKeywords", kwsStr);
      Integer pubCounts = (Integer) rsMap.get("pubCounts");
      doc.setField("psnPubCount", pubCounts);
    }
    // 人员项目数
    Long prjCounts = this.getPsnPrjCounts(psn.getPersonId());
    doc.setField("psnPrjCount", prjCounts);

    // 人员是否在隐私列表中
    if (this.psnPrivateDao.existsPsnPrivate(psn.getPersonId())) {
      doc.setField("isPrivate", 1);
    } else {
      doc.setField("isPrivate", 0);
    }

    if (psn.getInsId() != null) {

      InsPortal ins = insPortalDao.get(psn.getInsId());
      if (ins != null) {
        doc.setField("zhInsName", ins.getZhTitle());
        doc.setField("enInsName", ins.getEnTitle());
      }
      try {
        RolPsnIns psnIns = rolPsnInsDao.findPsnIns(psn.getPersonId(), psn.getInsId());
        if (psnIns != null && psnIns.getUnitId() != null) {
          InsUnit unit = insUnitDao.get(psnIns.getUnitId());
          if (unit != null) {
            doc.setField("zhUnit", unit.getZhName());
            doc.setField("enUnit", unit.getEnName());
          }
        }
      } catch (Exception e) {
        logger.info("获取psnIns相关属性错误, psnId= " + psn.getPersonId(), e);
      }
    }

    server.add(doc);
    server.commit();
  }

  public Long getPsnPrjCounts(Long psnId) {
    Long count = this.projectDao.getPsnPrjCounts(psnId);

    if (count == null) {
      return 0L;
    }
    return count;
  }

  /**
   * 更新人员solr信息统一在center-batch处理
   */
  @Override
  @Deprecated
  public void deleteSolrPsnInfo(Long psnId) throws Exception {
    // serverUrl = "http://192.168.15.192:28080/solr/";
    Assert.hasLength(serverUrl, "Solr中serverUrl为空，请在正确配置！");
    SolrServer server = new HttpSolrServer(serverUrl);
    if (psnId == null || psnId <= 0L) {
      logger.error("更新用户solr索引，psnId为空");
      return;
    }
    Long solrPsnId = this.generateIdForIndex(psnId, INDEX_TYPE_PSN);
    server.deleteById(String.valueOf(solrPsnId));
    server.commit();
    logger.info("Solr人员索引信息成功删除，psnId = " + psnId);
  }

  /*
   * 在index时区分唯一id
   */
  public Long generateIdForIndex(Long id, String type) {
    // pub前缀为100000
    if (INDEX_TYPE_PUB.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("100000" + idString);
    } else if (INDEX_TYPE_PSN.equalsIgnoreCase(type)) {
      // psn前缀为50
      String idString = String.valueOf(id);
      return Long.parseLong("50" + idString);
    } else if (INDEX_TYPE_PAT.equalsIgnoreCase(type)) {
      // pat前缀为700000
      String idString = String.valueOf(id);
      return Long.parseLong("700000" + idString);
    } else if (INDEX_TYPE_FUND.equalsIgnoreCase(type)) {
      // fund前缀为900000
      String idString = String.valueOf(id);
      return Long.parseLong("900000" + idString);
    } else if (INDEX_TYPE_KW.equalsIgnoreCase(type)) {
      String idString = String.valueOf(id);
      return Long.parseLong("210000" + idString);
    } else {
      return id;
    }
  }

  /**
   * 获取个人所属的所有成果关键词
   */
  public Map<String, Object> getPsnKwsByPubKws(Long psnId) {
    Map<String, Object> rsMap = new HashMap<String, Object>();
    String url = scmDomain + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
    List<Long> pubIdList = psnPubDAO.getPubIdsByPsnId(psnId);
    StringBuilder keywords = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        // 调用成果查询接口 /data/pub/query/detail
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("des3PubId", Des3Utils.decodeFromDes3(pubId.toString()));
        map.put("serviceType", "snsPub");
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(map), headers);
        PubDetailVO detail = restTemplate.postForObject(url, entity, PubDetailVO.class);
        if (detail != null) {
          keywords.append(detail.getKeywords());
        }
      }
    }
    rsMap.put("pubCounts", pubIdList.size());
    rsMap.put("kwsStr", keywords.toString());
    return rsMap;
  }


  @Override
  public String getAID(Long openId, String autoLoginType, Date overTime, Long psnId, String token) {
    // 获取 加密id 把加密id当做 cache的key 放入缓存中 注意保证唯一性（统一使用 唯一编码做md5加密
    // (openId+时间挫)
    String oauthid = DigestUtils.md5Hex(UUID.randomUUID().toString() + openId);
    // 缓存 过期时间可以通过 参数的方式 来区分 不同的需求 不一样的过期时间，比如 邮件的自动登录，记住登录的自动登录
    // 第3方系统 跳转的自动登录
    // 不保存缓存 （如果重启缓存服务器的话 所有缓存都会失效） 存数据库
    Date tempDate = new Date();
    AutoLoginOauthInfo autoLogin = new AutoLoginOauthInfo();
    autoLogin.setSecurityId(oauthid);
    autoLogin.setCreateTime(tempDate);
    // 获取过期时间
    autoLogin.setLoginType(autoLoginType);
    autoLogin.setOverdueTime(overTime);
    autoLogin.setPsnId(psnId);
    autoLogin.setUseTimes(0);
    autoLogin.setToken(token);
    autoLoginOauthInfoDao.save(autoLogin);
    return oauthid;
  }

  /**
   * 设置更新首要邮件，更新首要邮件将会将登陆名与首要邮件一致lqh.
   * 
   * @param email
   * @param psnId
   * @return 1成功设置邮件为首要邮件/登录帐号；2成果设置邮件为首要邮件，但是登录名已被其他用户占用;0更新失败
   * @throws ServiceException
   */
  public Integer updateFirstEmail(String email, Long psnId) throws OpenException {
    Assert.hasText(email, "email不允许为空");
    Assert.notNull(psnId, "psnId不允许为空");
    Boolean result = this.userDao.isLoginNameUsed(email, psnId);
    User user = userDao.get(psnId);
    // 已被使用
    if (result && user != null) {
      user.setEmail(email);
      userDao.save(user);
      return 2;
      // 未被使用
    } else if (user != null) {
      user.setLoginName(email);
      user.setEmail(email);
      userDao.save(user);
      return 1;
    }
    return 0;
  }

  @Override
  public User findUserByMobile(String mobile) {

    return userDao.findUserByMobile(mobile);
  }
}
