package com.smate.web.group.service.group.psn;

import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.model.group.psn.PsnInfo;

/**
 * 人员信息服务实现类
 * 
 * @author zk
 *
 */
@Service("psnInfoService")
@Transactional(rollbackOn = Exception.class)
public class PsnInfoServiceImpl implements PsnInfoService {

  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Override
  public String getPsnName(Long psnId, String locale) throws GroupException {
    Assert.notNull(psnId);
    if (StringUtils.isNotBlank(locale)) {
      locale = LocaleContextHolder.getLocale().toString();
    }
    Person person = personDao.getPersonName(psnId);
    if (person != null) {
      return this.getPsnName(person, locale);
    }
    return "";
  }

  @Override
  public String getPsnName(Person person, String locale) throws GroupException {
    String psnName = "";
    if ("zh_CN".equals(locale)) {
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
   * 获取群组成员项目、成果统计数以及工作经历
   */
  @Override
  public void packageGroupMemberInfo(PsnInfo psnInfo) throws GroupException {
    PsnStatistics psnStatistics = psnStatisticsDao.getPubAndPrjNum(psnInfo.getPsnId());
    if (psnStatistics != null) {
      psnInfo.setPrjSum(psnStatistics.getPrjSum());// 项目数
      psnInfo.setPubSum(psnStatistics.getPubSum());// 成果数
    }
    Map<String, String> primaryIns = workHistoryDao.getPrimaryInsInfoByPsnId(psnInfo.getPsnId());
    if (primaryIns != null) {
      psnInfo.setPrimaryIns(primaryIns);// 工作经历
    }
  }
}
