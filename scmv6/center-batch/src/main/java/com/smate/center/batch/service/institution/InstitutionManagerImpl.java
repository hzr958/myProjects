package com.smate.center.batch.service.institution;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.institution.InstitutionDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Institution;


/**
 * 单位机构服务接实现类.
 * 
 * @author lichangwen
 * 
 */
@Service("institutionManager")
@Transactional(rollbackFor = Exception.class)
public class InstitutionManagerImpl implements InstitutionManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public List<Institution> getInsListByName(String insNameZh, String insNameEn, Long natureType) {
    try {
      return institutionDao.getInsListByName(insNameZh, insNameEn, natureType);
    } catch (Exception e) {
      logger.error("根据单位名查找单位ID出错", e);
    }
    return null;
  }

  @Override
  public String getLocaleInsName(Long insId) throws ServiceException {
    try {
      if (insId == null)
        return null;
      String localeName = null;
      Locale locale = LocaleContextHolder.getLocale();
      Institution ins = institutionDao.get(insId);
      if (ins != null) {
        if (locale.equals(Locale.US)) {
          localeName = StringUtils.isNotBlank(ins.getEnName()) ? ins.getEnName() : ins.getZhName();
        } else {
          localeName = StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName();
        }
      }
      return localeName;
    } catch (Exception e) {
      logger.error("根据isnId:{}获取相应语言的insNam出错", insId, e);
    }
    return null;
  }
}
