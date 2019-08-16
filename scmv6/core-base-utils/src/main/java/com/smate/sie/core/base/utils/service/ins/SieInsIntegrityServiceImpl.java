package com.smate.sie.core.base.utils.service.ins;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegionDao;
import com.smate.sie.core.base.utils.model.ins.SieInsRegion;

/**
 * 单位信息计算完整度服务实现
 * 
 * @author ztg
 *
 */
@Service("sieInsIntegrityService")
@Transactional(rollbackFor = Exception.class)
public class SieInsIntegrityServiceImpl implements SieInsIntegrityService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private Sie6InsPortalDao sie6InsPortalDao;

  @Autowired
  private SieInsRegionDao sieInsRegionDao;

  @Override
  public void saveIntegrity(Sie6Institution ins) throws SysServiceException {
    try {
      Sie6InsPortal potal = sie6InsPortalDao.get(ins.getId());
      SieInsRegion region = sieInsRegionDao.get(ins.getId());
      int a = 0;
      if (ins != null) {
        if (StringUtils.isNotBlank(ins.getInsName())) {
          a = a + 1;
        }
        if (StringUtils.isNotBlank(ins.getUniformId1())) {
          a = a + 1;
        }
        // if (ins.getRegionId() != null &&
        // StringUtils.isNotBlank(ins.getRegionId().toString())) {
        // a = a + 1;
        // }
        if (StringUtils.isNotBlank(ins.getZhAddress())) {
          a = a + 1;
        }
        if (StringUtils.isNotBlank(ins.getUrl())) {
          a = a + 1;
        }
        if (StringUtils.isNotBlank(ins.getServerTel())) {
          a = a + 1;
        }
        if (StringUtils.isNotBlank(ins.getServerEmail())) {
          a = a + 1;
        }
        if (StringUtils.isNotBlank(ins.getBriefDesc())) {
          a = a + 1;
        }
      }
      if (potal != null) {
        if (StringUtils.isNotBlank(potal.getLogo())) {
          a = a + 1;
        }
      }
      if (region != null) {
        Long regionId = region.getDisId();
        if (regionId == null || regionId == 0) {
          regionId = region.getCyId();
        }
        if (regionId == null || regionId == 0) {
          regionId = region.getPrvId();
        }
        if (regionId == null || regionId == 0) {
          regionId = region.getCountryId();
        }
        if (regionId != null && regionId > 0) {
          a = a + 1;
        }
      }

      a = a * 100;
      DecimalFormat df = new DecimalFormat("0.0");
      int integrity = new BigDecimal(df.format((float) a / 9)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
      ins.setIntegrity(integrity);
      sie6InstitutionDao.save(ins);
    } catch (Exception e) {
      logger.error("更新单位信息完整度出错", e);
      throw new SysServiceException(e);
    }
  }

}
