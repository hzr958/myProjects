package com.smate.web.psn.service.profile.psnListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.open.PersonOpenDao;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.model.third.user.PersonOpen;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;

/**
 * 通用的人员列表信息构建服务
 *
 * @author wsn
 * @createTime 2017年6月19日 下午6:57:05
 *
 */

@Transactional(rollbackFor = Exception.class)
public class CommonPsnListViewService extends PsnListViewBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PersonOpenDao personOpenDao;

  @Override
  public String doVerifyData(PsnListViewForm form) {
    String verifyResult = null;
    if (CollectionUtils.isEmpty(form.getPsnIds())) {
      verifyResult = "psnIds is empty";
    }
    return verifyResult;
  }

  @Override
  public void doGetPsnListViewInfo(PsnListViewForm form) throws ServiceException, PsnBuildException {
    List<Person> pList = personProfileDao.getPsnListInfoByPsnIds(form.getPsnIds());
    List<Long> psnIds = form.getPsnIds();
    List<Person> psnList = new ArrayList<Person>();
    if (CollectionUtils.isNotEmpty(pList) && CollectionUtils.isNotEmpty(psnIds)) {// 按接收到的psnIds排序
      for (Long psnId : psnIds) {
        for (Person p : pList) {
          if (psnId.equals(p.getPersonId())) {
            psnList.add(p);
          }
        }
      }
    }
    Map<Long, String> urlMap = this.buildPsnShortUrlMap(psnProfileUrlDao.findPsnShortUrls(psnIds));
    List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
    if (CollectionUtils.isNotEmpty(psnList)) {
      for (Person psn : psnList) {
        PsnInfo psnInfo = new PsnInfo();
        psnInfo.setPsnId(psn.getPersonId());
        psnInfo.setDes3PsnId(ServiceUtil.encodeToDes3(psn.getPersonId().toString()));
        psnInfo.setAvatarUrl(psn.getAvatars());
        psnInfo = this.buildPsnName(psnInfo, psn);
        psnInfo = this.buildPsnTitoloInfo(psnInfo, psn);
        psnInfo = this.buildPsnKeyWords(psnInfo, psn.getPersonId());
        psnInfo = this.buildPsnStatisticsInfo(psnInfo, psn.getPersonId());
        // SCM-24288 科研之友 页面功能调整，需要显示科研号
        psnInfo = this.buildPsnOpenId(psnInfo, psn.getPersonId());
        psnInfo.setPerson(psn);
        this.buildPsnShortUrl(urlMap, psnInfo);
        psnInfoList.add(psnInfo);
      }
    }
    form.setPsnInfoList(psnInfoList);
  }

  /**
   * 构建人员名称_统一取name或ename,为空取另一个
   * 
   * @param psnInfo
   * @param psn
   * @return
   */
  private PsnInfo buildPsnName(PsnInfo psnInfo, Person psn) {
    Locale locale = LocaleContextHolder.getLocale();
    String name = psn.getName();
    String eName = psn.getEname();
    if (Locale.US.equals(locale)) {
      if (StringUtils.isBlank(eName)) {
        psnInfo.setName(psn.getName());
      } else {
        psnInfo.setName(psn.getEname());
      }
    } else {
      if (StringUtils.isBlank(name)) {
        psnInfo.setName(psn.getEname());
      } else {
        psnInfo.setName(psn.getName());
      }
    }
    if (StringUtils.isBlank(psnInfo.getName())) {
      psnInfo.setName("");
    }
    return psnInfo;
  }

  /**
   * 构建人员信息:机构+部门+职称
   * 
   * @param psnInfo
   * @param psn
   * @return
   */
  private PsnInfo buildPsnTitoloInfo(PsnInfo psnInfo, Person psn) {
    String insName = psn.getInsName();
    String department = psn.getDepartment();
    String position = psn.getPosition();
    String titolo = psn.getTitolo();
    // 机构+部门
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      psnInfo.setInsAndDep(
          (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department));
    } else {
      psnInfo.setInsAndDep(insName + ", " + department);
    }
    // 职称+头衔
    if (StringUtils.isBlank(position) || StringUtils.isBlank(titolo)) {
      psnInfo.setPosAndTitolo(
          (StringUtils.isBlank(position) ? "" : position) + (StringUtils.isBlank(titolo) ? "" : titolo));
    } else {
      psnInfo.setPosAndTitolo(position + ", " + titolo);
    }
    return psnInfo;
  }

  /**
   * 构建人员关键词信息
   * 
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PsnInfo buildPsnKeyWords(PsnInfo psnInfo, Long psnId) {
    List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(psnId);
    psnInfo.setDiscList(keyList);
    return psnInfo;
  }

  /**
   * 构建人员统计信息
   * 
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PsnInfo buildPsnStatisticsInfo(PsnInfo psnInfo, Long psnId) {
    // TODO 取公开的成果和项目数
    PsnStatistics psnSta = psnStatisticsDao.getPubAndPrjNum(psnId);
    // psnSta，getPubSum，getPrjSum都要做非空判断，防止出现空指针问题
    if (psnSta == null) {
      psnInfo.setPubSum(0);
      psnInfo.setPrjSum(0);
    } else {
      psnInfo.setPubSum(psnSta.getPubSum() == null ? 0 : psnSta.getPubSum());
      psnInfo.setPrjSum(psnSta.getPrjSum() == null ? 0 : psnSta.getPrjSum());
    }
    return psnInfo;
  }

  /**
   * 设置人员短地址
   * 
   * @param shortUrlMap
   * @param psnInfo
   * @return
   */
  private PsnInfo buildPsnShortUrl(Map<Long, String> shortUrlMap, PsnInfo psnInfo) {
    Long psnId = psnInfo.getPsnId();
    if (shortUrlMap.containsKey(psnId) && StringUtils.isNotBlank(shortUrlMap.get(psnId))) {
      psnInfo.setPsnShortUrl("/P/" + shortUrlMap.get(psnId));
    } else {
      psnInfo.setPsnShortUrl("/psnweb/outside/homepage?des3PsnId=" + Des3Utils.encodeToDes3(psnId.toString()));
    }
    return psnInfo;
  }

  /**
   * 设置人员科研号
   * 
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PsnInfo buildPsnOpenId(PsnInfo psnInfo, Long psnId) {
    PersonOpen personOpen = personOpenDao.getPersonOpenByPsnId(psnId);
    if (personOpen != null) {
      psnInfo.setPsnOpenId(personOpen.getOpenId());
    }
    return psnInfo;
  }

  /**
   * 构建人员短地址Map
   * 
   * @param urlList
   * @return
   */
  private Map<Long, String> buildPsnShortUrlMap(List<PsnProfileUrl> urlList) {
    Map<Long, String> urlMap = new HashMap<Long, String>();
    if (CollectionUtils.isNotEmpty(urlList)) {
      for (PsnProfileUrl psnProfile : urlList) {
        urlMap.put(psnProfile.getPsnId(), psnProfile.getPsnIndexUrl());
      }
    }
    return urlMap;
  }

}
