package com.smate.sie.center.task.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.sie.core.base.utils.unit.avatars.UnitAvatarsUtils;

/**
 * 批量生成单位默认LOGO 接口实现
 * 
 * @author xr
 *
 */
@Service("sieGenerateInsLogoService")
@Transactional(rollbackFor = Exception.class)
public class SieGenerateInsLogoServiceImpl implements SieGenerateInsLogoService {

  Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 文件根路径
   */
  @Value("${sie.file.root}")
  private String rootPath;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;

  /**
   * 找到单位域名中LOGO字段为空的单位域名
   */
  @Override
  public Page<Sie6InsPortal> findInsProtalByWithoutLogo(Page<Sie6InsPortal> portalPage) {
    Page<Sie6InsPortal> insportalList = null;
    try {
      insportalList = sie6InsPortalDao.findInsProtalByWithoutLogo(portalPage);
    } catch (Exception e) {
      logger.error("批量生成单位默认LOGO任务,查询单位域名中LOGO字段为空的单位域名失败", e);
      throw new ServiceException("INS_PORTAL查询单位域名信息失败", e);
    }
    return insportalList;
  }

  @Override
  public void GenerateInsLogo(Long insId) {
    try {
      // 查询现有非未审核单位
      Sie6Institution ins = sie6InstitutionDao.get(insId);
      if (ins != null) {
        Sie6InsPortal insPortal = sie6InsPortalDao.get(insId);
        if (insPortal != null) {
          // 生成默认图标（单位名称缩略字母图片）
          String LogoPath = insPortal.getLogo();
          if (StringUtils.isBlank(LogoPath)) {
            LogoPath = this.createInsProtalLogo(ins);
          }
          insPortal.setLogo(LogoPath);
          sie6InsPortalDao.saveOrUpdate(insPortal);
        }
      }
    } catch (Exception e) {
      logger.info("生成单位默认LOGO任务,生成单位默认LOGO异常，单位id:" + insId, e);
      throw new ServiceException("INS_PORTAL生成单位默认LOGO", e);
    }

  }

  private String createInsProtalLogo(Sie6Institution ins) {
    String zhName = ins.getZhName();
    String enName = ins.getEnName();

    String avatars = null;
    try {
      // 生成图标使用的字符串
      String iconStr = UnitAvatarsUtils.getIconStr(zhName, enName);
      if (iconStr.length() > 0) {
        String iconPath = UnitAvatarsUtils.unitAvatars(iconStr, ins.getId(), rootPath + "/sielogo");
        // 生成图标路径
        avatars = "/sielogo" + iconPath;
      }
    } catch (Exception e) {
      logger.info("批量生成单位默认LOGO任务,根据单位名称随机产生默认图标失败!单位id:" + ins.getId() + ";单位中文名称：" + zhName + ";单位英文名称：" + enName);
    }
    return avatars;
  }

}
