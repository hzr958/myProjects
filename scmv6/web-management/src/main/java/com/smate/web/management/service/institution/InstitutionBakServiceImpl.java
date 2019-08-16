package com.smate.web.management.service.institution;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.institution.bpo.InstitutionBakDao;
import com.smate.web.management.model.institution.bpo.InstitutionBak;
import com.smate.web.management.model.institution.bpo.InstitutionBpo;

@Service("institutionBakService")
@Transactional(rollbackFor = Exception.class)
public class InstitutionBakServiceImpl implements InstitutionBakService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InstitutionBakDao institutionBakDao;

  @Override
  public void backUpInstitution(InstitutionBpo ins, Long optPsnId) throws Exception {
    try {
      InstitutionBak insBak = new InstitutionBak();
      insBak.setInsId(ins.getId());
      insBak.setAbbreviation(ins.getAbbreviation());
      insBak.setBackupDate(new Date());
      insBak.setContactPerson(ins.getContactPerson());
      insBak.setEnabled(ins.getEnabled());
      insBak.setEnAddress(ins.getEnAddress());
      insBak.setEnName(ins.getEnName());
      insBak.setIsisOrgCode(ins.getIsisOrgCode());
      insBak.setNature(ins.getNature());
      insBak.setOptPsnId(optPsnId);
      insBak.setPostcode(ins.getPostcode());
      insBak.setRegionId(ins.getRegionId());
      insBak.setServerEmail(ins.getServerEmail());
      insBak.setServerTel(ins.getServerTel());
      insBak.setStatus(ins.getStatus());
      insBak.setTel(ins.getTel());
      insBak.setUrl(ins.getUrl());
      insBak.setZhAddress(ins.getZhAddress());
      insBak.setZhName(ins.getZhName());
      institutionBakDao.save(insBak);
    } catch (Exception e) {
      logger.error("备份单位修改前信息时出现异常", e);
    }
  }

}
