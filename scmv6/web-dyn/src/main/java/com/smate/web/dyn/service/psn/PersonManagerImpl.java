package com.smate.web.dyn.service.psn;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.dyn.dao.psn.PsnWorkHistoryInsInfoDao;
import com.smate.web.dyn.exception.DynException;
import com.smate.web.dyn.model.psn.PsnWorkHistoryInsInfo;

/**
 * 人员信息服务接口实现类
 * 
 * @author Administrator
 *
 */
@Service("personManager")
@Transactional(rollbackFor = Exception.class)
public class PersonManagerImpl implements PersonManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;

  @Override
  public String getPsnViewWorkHisInfo(Long psnId) throws DynException {
    StringBuffer viewInfo = new StringBuffer();
    try {
      PsnWorkHistoryInsInfo psnWorkInfo = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psnId);
      if (psnWorkInfo != null) {
        // 单位
        String insName = getNotBlankString(psnWorkInfo.getInsNameZh(), psnWorkInfo.getInsNameEn());
        // 部门
        String department = getNotBlankString(psnWorkInfo.getDepartmentZh(), psnWorkInfo.getDepartmentEn());
        // 职称
        String position = getNotBlankString(psnWorkInfo.getPositionZh(), psnWorkInfo.getPositionEn());

        viewInfo.append(insName);
        if (StringUtils.isNotBlank(department)) {
          viewInfo.append(StringUtils.isNotBlank(insName) ? ", " + department : department);
        }
        if (StringUtils.isNotBlank(position)) {
          if (StringUtils.isNotBlank(insName) || StringUtils.isNotBlank(department)) {
            viewInfo.append(", " + position);
          } else {
            viewInfo.append(position);
          }
        }
      }
    } catch (Exception e) {
      logger.error("构造人员工作单位信息出错，psnId=" + psnId);
      throw new DynException("构造人员工作单位信息出错，psnId=" + psnId, e);
    }
    return viewInfo.toString();
  }

  /**
   * 优先获取中文名称，中文名为空则获取英文名称-------人员列表中人员姓名下面的工作经历信息显示用
   * 
   * @param zhName
   * @param eName
   * @return
   */
  private String getNotBlankString(String zhName, String eName) {
    String viewEName = StringUtils.isNotBlank(eName) ? eName : "";
    return StringUtils.isNotBlank(zhName) ? zhName : viewEName;

  }
}
