package com.smate.center.task.service.sns.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.ConstPsnScoreDao;
import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.psn.PsnScoreDetailDao;
import com.smate.center.task.dao.sns.psn.PsnScoreRefreshDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.center.task.psn.model.ConstPsnScore;
import com.smate.center.task.psn.model.PsnScoreDetail;
import com.smate.center.task.psn.model.PsnScoreRefresh;
import com.smate.center.task.service.search.UserSearchService;
import com.smate.center.task.single.service.institution.InstitutionManager;
import com.smate.center.task.single.service.person.PersonManager;
import com.smate.center.task.single.service.pub.MyPublicationQueryService;
import com.smate.center.task.single.service.pub.PublicationService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.common.SpringElCompnent;
import com.smate.core.base.utils.constant.PsnScoreConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;

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
  private InstitutionManager institutionManager;
  @Autowired
  private ConstRegionService constRegionService;
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private PsnProfileUrlService psnProfileUrlService;
  @Autowired
  private PsnPrivateService psnPrivateService;
  @Autowired
  private UserSearchService userSearchService;
  @Autowired
  private PsnScoreRefreshDao psnScoreRefreshDao;
  @Autowired
  private ConstPsnScoreDao constPsnScoreDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnScoreDetailDao psnScoreDetailDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private SysDomainConst sysDomainConst;

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

  @Override
  public void handlerPsnScoreInit(Long psnId) throws ServiceException {
    if (psnId != null) {
      List<Integer> updateScoreType = new ArrayList<Integer>();
      updateScoreType.add(PsnScoreConstants.PSN_AVATAR);// 个人头像
      updateScoreType.add(PsnScoreConstants.PSN_TITLE);// 头衔
      updateScoreType.add(PsnScoreConstants.PSN_BRIEF);// 个人简介
      updateScoreType.add(PsnScoreConstants.PSN_REGION);// 省市地址
      updateScoreType.add(PsnScoreConstants.PSN_WORK);// 首要单位
      updateScoreType.add(PsnScoreConstants.PSN_HINDEX);
      updateScoreType.add(PsnScoreConstants.PSN_PUBNUM);// 成果数
      updateScoreType.add(PsnScoreConstants.PSN_CITE);// 引用数
      updateScoreType.add(PsnScoreConstants.PSN_KEYWORD);// 关键词
      updateScoreType.add(PsnScoreConstants.PSN_HOME);// 个人主页URL
      this.updatePersonScore(psnId, updateScoreType);
      // 删除刷新记录
      this.psnScoreRefreshDao.delete(psnId);
    }
  }

  @Override
  public void updatePersonScore(Long psnId, List<Integer> updateScoreType) throws ServiceException {
    // SCM-13372,改为记录总分
    int psnSumScore = 0;
    try {
      psnScoreDetailDao.getPsnSumScore(psnId);
    } catch (DaoException e) {
      logger.error("获取人员总分出错，psnId:" + psnId, e);
    }
    for (Integer typeId : updateScoreType) {
      psnSumScore += this.updatePsnScoreDetail(psnId, typeId);
    }
    // 保存用户检索信息
    this.saveUserSearch(psnId, psnSumScore);
  }

  public Integer updatePsnScoreDetail(Long psnId, Integer typeId) throws ServiceException {
    // 查找得分.
    int scoreNum = 0;
    String typeValue = "";
    try {
      typeValue = this.getTypeValue(typeId, psnId);
    } catch (Exception e) {
      logger.error("获取对应人员信息类型对应值出错！，psnId:" + psnId + "，typeId：" + typeId, e);
    }
    ConstPsnScore constPsnScore = this.constPsnScoreDao.get(typeId);
    if (constPsnScore != null) {
      Map<String, Object> paramMap = new HashMap<String, Object>();
      if (typeValue.indexOf("'") != -1) {
        typeValue = typeValue.replace("'", "''");
      }
      paramMap.put("typeValue", typeValue);
      boolean result = SpringElCompnent.getExpResult(constPsnScore.getScoreRule(), paramMap);
      if (result) {
        scoreNum = constPsnScore.getScoreNum();
      }
      if (typeValue.indexOf("''") != -1) {
        typeValue = typeValue.replace("''", "'");
      }
    }
    try {
      // 更新得分记录
      PsnScoreDetail psnScoreDetail = this.psnScoreDetailDao.getPsnScoreDetailByType(psnId, typeId);
      if (psnScoreDetail != null) {
        psnScoreDetail.setScoreNum(scoreNum);
        psnScoreDetail.setTypeValue(typeValue);
      } else {
        psnScoreDetail = new PsnScoreDetail();
        psnScoreDetail.setPsnId(psnId);
        psnScoreDetail.setScoreNum(scoreNum);
        psnScoreDetail.setTypeId(typeId);
        psnScoreDetail.setTypeValue(typeValue);
      }
      this.psnScoreDetailDao.save(psnScoreDetail);
      return psnScoreDetail.getScoreNum();
    } catch (DaoException e) {
      logger.error("更新人员" + psnId + "信息完整度分数时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  public void saveUserSearch(Long psnId, int scoreNum) {
    Person person = this.personManager.getPerson(psnId);
    Institution ins = institutionManager.getInstitution(person.getInsName(), person.getInsId());
    // 总得分
    Integer isPrivate = 0;
    isPrivate = psnPrivateService.isPsnPrivate(person.getPersonId()) ? 1 : 0;
    userSearchService.saveUserSearch(person, getPsnNameByLang(person, 1), getPsnNameByLang(person, 2), 1, isPrivate,
        ins, scoreNum);
  }

  /**
   * 获取对应类型的值.
   * 
   * @param typeId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private String getTypeValue(int typeId, Long psnId) throws ServiceException {
    try {
      String typeValue = "";
      Person person = null;
      switch (typeId) {
        case PsnScoreConstants.PSN_AVATAR:
          person = personManager.getPerson(psnId);
          typeValue = person.getAvatars() == null ? "" : person.getAvatars();
          break;
        case PsnScoreConstants.PSN_TITLE:
          person = personManager.getPerson(psnId);
          typeValue = person.getTitolo() == null ? "" : person.getTitolo();
          break;
        case PsnScoreConstants.PSN_REGION:
          person = personManager.getPerson(psnId);
          String region = null;
          if (person.getRegionId() != null) {
            region = constRegionService.getRegionsById(person.getRegionId());
          }
          typeValue = region == null ? "" : region;
          break;
        case PsnScoreConstants.PSN_BRIEF:
          String brief = personManager.getPersonBrief(psnId);
          typeValue = StringUtils.isBlank(brief) ? "" : "1";
          break;
        case PsnScoreConstants.PSN_WORK:
          String insName = workHistoryDao.getPrimaryWorkNameByPsnId(psnId);
          typeValue = insName == null ? "" : insName;
          break;
        case PsnScoreConstants.PSN_KEYWORD:
          List<PsnDisciplineKey> keyList = psnDisciplineKeyDao.findByPsnId(psnId);
          PsnDisciplineKey psnDisciplineKey = null;
          typeValue = "";
          for (int i = 0; i < keyList.size(); i++) {
            psnDisciplineKey = keyList.get(i);
            if (StringUtils.isBlank(typeValue)) {
              typeValue += psnDisciplineKey.getKeyWords();
            } else {
              typeValue += ";" + psnDisciplineKey.getKeyWords();
            }
          }
          if (typeValue.length() > 350) {// 解决过长保存出错
            typeValue = typeValue.substring(0, 350);
          }
          break;
        case PsnScoreConstants.PSN_HINDEX:
          Integer hidex = myPublicationQueryService.getHindex(psnId);
          typeValue = hidex + "";
          break;
        case PsnScoreConstants.PSN_PUBNUM:
          int pubTotalCount = publicationService.getTotalPubsByPsnId(psnId).intValue();
          typeValue = pubTotalCount + "";
          break;
        case PsnScoreConstants.PSN_CITE:
          Integer totalCiteTimes = myPublicationQueryService.getTotalCiteTimes(psnId);
          typeValue = totalCiteTimes + "";
          break;
        case PsnScoreConstants.PSN_HOME:// 公开主页
          typeValue = psnProfileUrlDao.find(psnId).getPsnIndexUrl();
          if (StringUtils.isNotBlank(typeValue)) {
            String snsDomain = sysDomainConst.getSnsDomain();
            typeValue = snsDomain + "/" + ShortUrlConst.P_TYPE + "/" + typeValue;
          }
          break;
        default:
          break;
      }
      return typeValue;
    } catch (Exception e) {
      logger.error("getTypeValue", e);
      throw new ServiceException("getTypeValue", e);
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
   * 删除刷新记录.
   */
  @Override
  public void deletePsnScoreRefresh(Long psnId) throws ServiceException {
    if (this.psnScoreRefreshDao.get(psnId) != null) {
      this.psnScoreRefreshDao.delete(psnId);
    }
  }
}
