package com.smate.center.oauth.service.version;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.version.VersionInfoDao;
import com.smate.center.oauth.model.mobile.version.AppVersionVo;
import com.smate.center.oauth.model.mobile.version.VersionInfoBean;
import com.smate.core.base.utils.common.BeanUtils;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 移动端app版本服务
 * 
 * @author wsn
 * @date Jan 3, 2019
 */
@Service("mobileAppVersionService")
@Transactional(rollbackFor = Exception.class)
public class MobileAppVersionServiceImpl implements MobileAppVersionService {


  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private VersionInfoDao versionInfoDao;

  @Override
  public VersionInfoBean findLastAppVersionInfo(String appType) {
    return versionInfoDao.findLastAppVersionInfoByType(appType);
  }


  @Override
  public AppVersionVo findAllAppVersionInfo(AppVersionVo avo) {
    List<AppVersionVo> versionVoList = new ArrayList<AppVersionVo>();
    List<VersionInfoBean> versionList = versionInfoDao.findAllAppVersionInfo(avo);
    if (CollectionUtils.isNotEmpty(versionList)) {
      versionList.forEach(v -> {
        AppVersionVo vo = new AppVersionVo();
        try {
          BeanUtils.copyProperties(vo, v);
        } catch (IllegalAccessException e) {
          logger.error("拷贝app版本Bean属性异常");
        } catch (InvocationTargetException e) {
          logger.error("拷贝app版本Bean属性异常");
        }
        versionVoList.add(vo);
      });
    }
    avo.setAppVersionList(versionVoList);
    return avo;
  }

  @Override
  public String updateAppVersionInfo(AppVersionVo vo) {
    String result = "error";
    try {
      if (checkUpdateOptParams(vo)) {
        switch (vo.getOptType()) {
          case 0:// 删除
            result = this.deleteAppVersionInfo(vo);
            break;
          case 1:// 新增
            result = this.addAppVersionInfo(vo);
            break;
          case 2:// 编辑
            result = this.modifyAppVersionInfo(vo);
            break;
          default:
            break;
        }
      }
    } catch (Exception e) {
      logger.error("更新app版本信息异常", e);
    }
    return result;
  }

  /**
   * 校验必要参数
   * 
   * @param vo
   * @return
   */
  protected boolean checkUpdateOptParams(AppVersionVo vo) {
    boolean flag = true;
    Integer optType = vo.getOptType();
    if (optType == null) {
      flag = false;
    }
    // 新增或编辑
    if (CommonUtils.compareIntegerValue(optType, 1) || CommonUtils.compareIntegerValue(optType, 2)) {
      if (StringUtils.isBlank(vo.getAppType())) {
        flag = false;
      }
      if (StringUtils.isBlank(vo.getDownloadUrl())) {
        flag = false;
      }
      if (StringUtils.isBlank(vo.getVersionDesc())) {
        flag = false;
      }
      if (StringUtils.isBlank(vo.getVersionName())) {
        flag = false;
      }
      if (vo.getVersionCode() == null) {
        flag = false;
      }
      if (vo.getMustUpdate() == null) {
        flag = false;
      }
    }
    // 编辑或删除
    if (CommonUtils.compareIntegerValue(optType, 0) || CommonUtils.compareIntegerValue(optType, 2)) {
      if (NumberUtils.isNullOrZero(vo.getId())) {
        flag = false;
      }
    }
    return flag;
  }

  /**
   * 删除版本信息
   * 
   * @param vo
   * @return
   */
  protected String deleteAppVersionInfo(AppVersionVo vo) {
    String result = "success";
    int count = versionInfoDao.deleteAppVersionInfo(vo.getAppType(), vo.getId());
    if (count == 0) {
      result = "error";
    }
    return result;
  }

  /**
   * 添加版本信息
   * 
   * @param vo
   * @return
   */
  protected String addAppVersionInfo(AppVersionVo vo) {
    VersionInfoBean vBean =
        new VersionInfoBean(vo.getAppType(), vo.getMustUpdate(), vo.getVersionName(), vo.getVersionCode(),
            vo.getVersionDesc(), vo.getDownloadUrl(), vo.getVersionSize(), vo.getNewMd5(), new Date(), 1);
    versionInfoDao.save(vBean);
    return "success";
  }


  /**
   * 更新版本信息
   * 
   * @param vo
   * @return
   */
  protected String modifyAppVersionInfo(AppVersionVo vo) {
    String result = "error";
    VersionInfoBean vBean = versionInfoDao.get(vo.getId());
    if (vBean != null) {
      vBean.setAppType(vo.getAppType());
      vBean.setDownloadUrl(vo.getDownloadUrl());
      vBean.setMustUpdate(vo.getMustUpdate());
      vBean.setNewMd5(vo.getNewMd5());
      vBean.setStatus(1);
      vBean.setUpdateDate(new Date());
      vBean.setVersionCode(vo.getVersionCode());
      vBean.setVersionDesc(vo.getVersionDesc());
      vBean.setVersionName(vo.getVersionName());
      vBean.setVersionSize(vo.getVersionSize());
      versionInfoDao.save(vBean);
      result = "success";
    }
    return result;
  }

  @Override
  public VersionInfoBean findAppVersionInfoById(String appType, Long id) {
    return versionInfoDao.findVersionInfoById(appType, id);
  }

}
