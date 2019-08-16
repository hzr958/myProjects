package com.smate.center.task.service.rol.quartz;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.bpo.TmpInsInfo;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.person.avatars.PersonAvatarsUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 
 * 单位数据同步任务服务
 * 
 * @author hd
 *
 */
@Service("insSyncService")
@Transactional(rollbackFor = Exception.class)
public class InsSyncServiceImpl {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BpoInsSyncService bpoInsSyncService;
  @Autowired
  private SnsInsSysService snsInsSysService;
  @Autowired
  private RolInsSyncService rolInsSysService;
  @Autowired
  private CasInsSyncService casInsSyncService;
  @Autowired
  private SysDomainConst sysDomainConst;
  /**
   * 文件根路径
   */
  @Value("${file.root}")
  private String rootPath;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 查询需要同步信息的单位
   * 
   * @param batchSize
   * @return
   */
  public List<TmpInsInfo> findSyncInsList(int batchSize) throws Exception {
    List<TmpInsInfo> insList = null;
    try {
      insList = bpoInsSyncService.findSyncInsList(batchSize);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，查询单位信息失败", e);
      throw new ServiceException("tmp_ins_info查询单位信息失败", e);
    }
    return insList;
  }

  /**
   * 同步单位信息
   * 
   * @param tmpInsInfo
   * @throws Exception
   */
  public void syncNsfcIns(TmpInsInfo tmpInsInfo) throws Exception {
    InsInfo info = collectInsInfo(tmpInsInfo);
    try {
      Long insId = bpoInsSyncService.addIns(info);
      if (insId == null) {
        throw new ServiceException("BPO创建institution失败；");
      }
      info.setInsId(insId);
      rolInsSysService.addIns(info);
      snsInsSysService.addIns(info);
      // 更新单位域名
      bpoInsSyncService.addInsPortal(info);
      rolInsSysService.addInsPortal(info);
      snsInsSysService.addInsPortal(info);

      // INS_STATUS
      rolInsSysService.addInsStatus(info.getInsId());
      // 创建和更新人员和角色
      User user = casInsSyncService.findUserByLoginName(info.getContactEmail());
      if (user == null) {// 人员不存在
        info.setPsnId(snsInsSysService.findNewPsnId());// sns查询新人员的psn_id
        Long psnId = casInsSyncService.addSysUser(info);
        info.setPsnId(psnId);
        Map<String, String> pinyin = ServiceUtil.parsePinYin(info.getContactName());
        if (pinyin != null) {
          info.setFirstName(pinyin.get("firstName"));
          info.setLastName(pinyin.get("lastName"));
          info.setPsnEname(info.getFirstName() + " " + info.getLastName());
          info.setSnsAvatar(this.getSnsAvatas(info));
        }
        Map<String, String> word = ServiceUtil.parseZhfirstAndLast((info.getContactName()));
        if (word != null) {
          info.setZhFirstName(word.get("firstNameZh"));
          info.setZhLastName(word.get("lastNameZh"));
        }
        // sns增加人员与单位的关系
        snsInsSysService.addPersonSns(info); // person

      } else {// 人员存在
        info.setPsnId(user.getId());
        Person p = snsInsSysService.getPersonById(user.getId());
        if (p != null) {
          info.setFirstName(p.getFirstName());
          info.setLastName(p.getLastName());
          info.setPsnEname(p.getEname());
          info.setSnsAvatar(p.getAvatars());
          info.setZhFirstName(p.getZhFirstName());
          info.setZhLastName(p.getZhLastName());
          info.setContactName(p.getName());
        }
      }
      Long sid = snsInsSysService.addPsnSid(info.getPsnId());
      info.setSid(sid);
      info.setRolAvatar(this.getRolAvatas(null, null));
      rolInsSysService.addPsnIns(info);// rol增加人员与单位的关系
      snsInsSysService.addPsnIns(info);// sns增加人员与单位的关系

      // sns增加人员与单位的角色表
      // 普通注册人员
      snsInsSysService.addSysUserRole(info.getPsnId(), 1L, 0L);
      // 教授、研究人员
      snsInsSysService.addSysUserRole(info.getPsnId(), 3L, 0L);
      // 单位管理员(RO)
      rolInsSysService.addSysUserRole(info.getPsnId(), 2L, insId);
      // 单位个人用户(M)
      rolInsSysService.addSysUserRole(info.getPsnId(), 3L, insId);
      // 系统管理员(Admin)
      rolInsSysService.addSysUserRole(info.getPsnId(), 11L, insId);
    } catch (Exception e) {
      logger.error("单位同步失败", e);

      if (info.getPsnId() != null) {
        try {
          casInsSyncService.deleteCas(info.getPsnId());// 清除cas端脏数据
          snsInsSysService.deleteSns(info.getPsnId(), info.getInsId());// 清除sns端脏数据
          rolInsSysService.deleteRol(info.getPsnId(), info.getInsId());// 清除rol端脏数据
        } catch (Exception e2) {
          logger.error("单位信息清除失败", e2);
        }
      }
      throw new Exception(e.getMessage(), e);
    }
  }

  /**
   * 更新状态
   * 
   * @param tmpInsInfo
   */
  public void updateTmpInsInfo(TmpInsInfo tmpInsInfo) throws Exception {
    try {

      bpoInsSyncService.updateTmpInsInfo(tmpInsInfo);

    } catch (Exception e) {
      logger.error("更新单位同步状态失败", e);
      throw new Exception(e.getMessage(), e);
    }

  }

  /**
   * 将TmpInsInfo转为InsInfo
   * 
   * @param tmpInsInfo
   * @return
   */
  private InsInfo collectInsInfo(TmpInsInfo tmpInsInfo) throws Exception {
    InsInfo info = new InsInfo(tmpInsInfo);
    try {
      // 单位性质
      Long nature = bpoInsSyncService.getNatureByName(tmpInsInfo.getNature());
      // 省
      Long prvId = snsInsSysService.findRegionId(tmpInsInfo.getProv());
      // 城市
      Long cyId = snsInsSysService.findRegionId(tmpInsInfo.getCity());
      // region_id
      if (cyId != null && cyId > 0) {

        info.setRegionId(cyId);
      } else {
        info.setRegionId(prvId);
      }
      info.setPrvId(prvId);
      info.setCyId(cyId);
      info.setNature(nature);
      // 协会版
      if (info.getNature() == 6L) {
        info.setSwitchLang("0");
        info.setDefaultLang("en_US");
        info.setVersion(1);
      }
      return info;

    } catch (Exception e) {
      logger.error("将TmpInsInfo转为InsInfo对象失败", e);
      throw new Exception(e.getMessage(), e);
    }

  }

  /**
   * 获取sns头像
   * 
   * @param info
   * @return
   */
  private String getSnsAvatas(InsInfo info) {
    String snsAvatar = "";
    if (StringUtils.isNotBlank(info.getFirstName()) || StringUtils.isNotBlank(info.getLastName())) {
      String a = info.getFirstName() != null ? info.getFirstName().substring(0, 1).toUpperCase() : "";
      String b = info.getLastName() != null ? info.getLastName().substring(0, 1).toUpperCase() : "";
      try {
        String avatars = PersonAvatarsUtils.personAvatars(b + a, info.getPsnId(), rootPath + "/avatars");
        snsAvatar = domainscm + "/avatars" + avatars;
      } catch (Exception e) {
        logger.error("根据英文名随机产生默认头像失败!");
      }
    }

    if (StringUtils.isBlank(snsAvatar)) {
      snsAvatar = sysDomainConst.getSnsDomain() + ServiceConstants.DEFAULT_MAN_AVATARS;
    }
    return snsAvatar;
  }

  /**
   * 获取rol头像
   * 
   * @param sex
   * @param avator
   * @return
   */
  private String getRolAvatas(Integer sex, String avator) {
    try {
      // 获取用户头像
      if (avator != null && !avator.endsWith(ServiceConstants.DEFAULT_MAN_AVATARS)
          && !avator.endsWith(ServiceConstants.DEFAULT_WOMAN_AVATARS)) {
        if (avator.startsWith("http://")) {
          return avator;
        }
        return domainscm + avator;
      } else if (sex != null && sex == 0) {// 女性
        return domainscm + ServiceConstants.DEFAULT_WOMAN_AVATARS;
      } else {
        return domainscm + ServiceConstants.DEFAULT_MAN_AVATARS;
      }
    } catch (Exception e) {
      logger.error("rol获取默认头像出现异常", e);
      return domainscm + ServiceConstants.DEFAULT_MAN_AVATARS;
    }
  }

}
