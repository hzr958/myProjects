package com.smate.web.management.service.institution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.management.dao.institution.sns.InsInfoDao;
import com.smate.web.management.dao.institution.sns.InsSnRegionDao;
import com.smate.web.management.dao.institution.sns.PsnInsDao;
import com.smate.web.management.model.institution.sns.InsInfo;
import com.smate.web.management.model.institution.sns.InsRegion;
import com.smate.web.management.model.institution.sns.PsnInsSns;

@Service("institutionSnsService")
@Transactional(rollbackFor = Exception.class)
public class InstitutionSnsServiceImpl implements InstitutionSnsService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnInsDao psnInsDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private InsSnRegionDao insSnsRegionDao;
  @Autowired
  private InsInfoDao insInfoDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private InsPortalDao insPortalDao;

  @Override
  public void save(InsPortal insPortal, Institution institution, Person person) throws Exception {
    try {
      institutionDao.save(institution);
      PsnInsSns psnInsSns = psnInsDao.findPsnInsSns(person.getPersonId(), institution.getId());
      if (psnInsSns == null) {
        psnInsSns = new PsnInsSns(person.getPersonId(), institution.getId(), 0L, 1);
      } else {
        psnInsSns.setNotInJob(0L);
        psnInsSns.setAllowSubmitPub(1);
      }
      psnInsDao.save(psnInsSns);

      // 审核后，同步单位地区
      if (institution.getProvinceId() != null && institution.getProvinceId() > 0) {
        InsRegion insRegion = insSnsRegionDao.get(institution.getId());
        if (insRegion == null) {
          // 单位地区.
          insRegion = new InsRegion();
          insRegion.setInsId(institution.getId());
        }
        insRegion.setPrvId(institution.getProvinceId());
        insRegion.setCyId(institution.getCityId());
        insSnsRegionDao.save(insRegion);
      }
      // 保存InsInf的信息
      InsInfo insInfo = insInfoDao.get(institution.getId());
      if (insInfo == null) {
        // 单位地区.
        insInfo = new InsInfo();
        insInfo.setInsId(institution.getId());
      }
      insInfo.setProvince(institution.getProvinceId());
      insInfo.setCity(institution.getCityId());
      if (institution.getCityId() != null || institution.getProvinceId() != null) {
        ConstRegion constRegion = constRegionDao
            .get(institution.getProvinceId() == null ? institution.getCityId() : institution.getProvinceId());
        if (constRegion != null && "CN".equals(constRegion.getCountryCode()))
          insInfo.setCountry(156l);
        else
          insInfo.setCountry(-156l);
      }
      insInfo.setIsNsfc(0);
      insInfo.setInsLevel(3);
      insInfo.setIsUnderdevelop(0);
      insInfoDao.save(insInfo);
      if (insPortal != null) {
        insPortalDao.save(insPortal);
      }

    } catch (Exception e) {
      logger.error("同步 信息到sns失败机构id是" + institution.getId() + "人员id是" + person.getPersonId());
      throw new Exception(e);

    }


  }

}
