package com.smate.web.group.service.grp.member;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.group.dao.group.psn.PsnDisciplineKeyDao;
import com.smate.web.group.model.group.psn.PsnInfo;

/**
 * 构造人员信息service实现类
 * 
 * @author zzx
 *
 */
@Service("buildPsnInfoService")
@Transactional(rollbackFor = Exception.class)
public class BuildPsnInfoServiceImpl implements BuildPsnInfoService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnStatisticsDao PsnStatisticsDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;

  @Override
  public PsnInfo buildPersonInfo(Long psnId, Integer type) {
    PsnInfo psnInfo = new PsnInfo();
    try {
      switch (type) {
        case 1:
          buildNameAvatar(psnInfo, psnId);// 姓名 头像
          break;
        case 2:// 群组成员列表-调用
          buildNameAvatarInsname(psnInfo, psnId); // 姓名 头像
          buildPubAndPrjNum(psnInfo, psnId);// 项目数 成果数
          buildPsnDiscKey(psnInfo, psnId);// 获取人员关键词
          break;

        default:
          break;
      }
    } catch (Exception e) {
      logger.error("构造人员信息出错psnId=" + psnId, e);
      return null;
    }
    return psnInfo;
  }

  /**
   * 获取人员关键词
   * 
   * @param psnInfo
   * @param psnId
   */
  private void buildPsnDiscKey(PsnInfo psnInfo, Long psnId) {
    // status:当前研究领域是有效,0无效（既删除的研究领域），１有效[兼容认同信息，用户保存的关键词记录不能直接删除，否则该关键词的认同信息就会失联]
    psnInfo.setPsnDisciplineKeyList(psnDisciplineKeyDao.findPsnDisciplineKeyByPsnId(psnId, 1));
  }

  /**
   * 人员项目数 成果统计数
   * 
   * @param psnInfo
   * @param psnId
   */
  private void buildPubAndPrjNum(PsnInfo psnInfo, Long psnId) {
    psnInfo.setPsnStatistics(PsnStatisticsDao.getPubAndPrjNum(psnId));
  }

  /**
   * 获取人员的姓名头像 ins_name
   */
  private void buildNameAvatarInsname(PsnInfo psnInfo, Long psnId) {
    Person person = personDao.get(psnId);
    if (person != null) {
      psnInfo.setPerson(person);
      String local = LocaleContextHolder.getLocale().toString();
      if (local.equals("zh_CN")) {
        if (StringUtils.isNotBlank(person.getName())) {
          psnInfo.setName(person.getName());
        } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
          psnInfo.setName(person.getFirstName() + " " + person.getLastName());
        } else if (StringUtils.isNotBlank(person.getEname())) {
          psnInfo.setName(person.getEname());
        }
      } else {
        if (StringUtils.isNotBlank(person.getEname())) {
          psnInfo.setName(person.getEname());
        } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
          psnInfo.setName(person.getFirstName() + " " + person.getLastName());
        } else if (StringUtils.isNotBlank(person.getName())) {
          psnInfo.setName(person.getName());
        }
      }

      String insName = "";
      if (StringUtils.isNotBlank(person.getInsName())) {
        insName += person.getInsName() + ", ";
      }
      if (StringUtils.isNotBlank(person.getDepartment())) {
        insName += person.getDepartment() + ", ";
      }
      if (StringUtils.isNotBlank(person.getPosition())) {
        insName += person.getPosition() + ", ";
      }
      /*
       * if (StringUtils.isNotBlank(person.getTitolo())) { insName += person.getTitolo() + ", "; }
       */
      if (StringUtils.isNotBlank(insName)) {
        insName = insName.substring(0, insName.length() - 2);
      }
      psnInfo.setInsName(insName);
    }
  }

  /**
   * 人员的姓名和头像
   * 
   * @param psnInfo
   * @param psnId
   */
  private void buildNameAvatar(PsnInfo psnInfo, Long psnId) {
    Person person = personDao.findPersonBase(psnId);
    if (person != null) {
      psnInfo.setPerson(person);
      psnInfo.setAvatarUrl(person.getAvatars());

      String local = LocaleContextHolder.getLocale().toString();
      if (local.equals("zh_CN")) {
        if (StringUtils.isNotBlank(person.getName())) {
          psnInfo.setName(person.getName());
        } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
          psnInfo.setName(person.getFirstName() + " " + person.getLastName());
        } else if (StringUtils.isNotBlank(person.getEname())) {
          psnInfo.setName(person.getEname());
        }
      } else {
        if (StringUtils.isNotBlank(person.getEname())) {
          psnInfo.setName(person.getEname());
        } else if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
          psnInfo.setName(person.getFirstName() + " " + person.getLastName());
        } else if (StringUtils.isNotBlank(person.getName())) {
          psnInfo.setName(person.getName());
        }
      }

    }

  }

}
