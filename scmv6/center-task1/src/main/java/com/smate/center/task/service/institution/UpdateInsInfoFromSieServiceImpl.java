package com.smate.center.task.service.institution;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.InsStatisticsRolDao;
import com.smate.center.task.dao.rol.quartz.InstitutionRolDao;
import com.smate.center.task.dao.sns.ins.InsStatisticsDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.rol.quartz.InsStatisticsRol;
import com.smate.center.task.model.rol.quartz.InstitutionRol;
import com.smate.center.task.model.sns.ins.InsStatistics;
import com.smate.center.task.model.sns.quartz.Institution;

/**
 * 从SIE更新机构信息到SNS服务
 * 
 * @author wsn
 *
 */
@Service("updateInsInfoFromSieService")
@Transactional(rollbackFor = Exception.class)
public class UpdateInsInfoFromSieServiceImpl implements UpdateInsInfoFromSieService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private InsStatisticsDao insStatisticsDao;
  @Autowired
  private InstitutionRolDao institutionRolDao;
  @Autowired
  private InsStatisticsRolDao insStatisticsRolDao;

  @Override
  public List<InsStatisticsRol> findInsStatisticsRolList(Integer size, Long startInsId, Long endInsId)
      throws TaskException {
    return insStatisticsRolDao.getInsStatisticsRolBySize(size, startInsId, endInsId);
  }

  @Override
  public List<InstitutionRol> findInstitutionRolList(Integer size, Long startInsId, Long endInsId)
      throws TaskException {
    return institutionRolDao.getInstitutionRolBySize(size, startInsId, endInsId);
  }

  @Override
  public void updateInsInfoFromROL(InstitutionRol insRol) throws TaskException {
    try {
      Long insId = insRol.getId();
      // 更新机构信息
      Institution ins = institutionDao.get(insId);
      if (ins == null) {
        ins = new Institution();
        ins.setId(insId);
      }
      ins.setZhName(insRol.getZhName());
      ins.setEnName(insRol.getEnName());
      ins.setAbbreviation(insRol.getAbbreviation());
      ins.setContactPerson(insRol.getContactPerson());
      ins.setTel(insRol.getTel());
      ins.setServerEmail(insRol.getServerEmail());
      ins.setUrl(insRol.getUrl());
      ins.setRegionId(insRol.getRegionId() == 0 ? null : insRol.getRegionId());
      ins.setStatus(insRol.getStatus() == null ? 0 : insRol.getStatus());
      ins.setNature(insRol.getNature() == null ? 99 : insRol.getNature());
      ins.setZhAddress(insRol.getZhAddress());
      ins.setEnAddress(insRol.getEnAddress());
      ins.setServerTel(insRol.getServerTel());
      ins.setPostcode(insRol.getPostcode());
      ins.setEnabled(insRol.getEnabled());
      institutionDao.save(ins);
      InsStatisticsRol insStaRol = insStatisticsRolDao.get(insId);
      if (insStaRol != null) {
        InsStatistics insSta = insStatisticsDao.get(insId);
        if (insSta == null) {
          insSta = new InsStatistics();
          insSta.setInsId(insId);
        }
        insSta.setZhName(insStaRol.getZhName());
        insSta.setEnName(insStaRol.getEnName());
        insSta.setPrjSum(insStaRol.getPrjSum());
        insSta.setPubSum(insStaRol.getPubSum());
        insSta.setPsnSum(insStaRol.getPsnSum());
        insStatisticsDao.save(insSta);
      }

    } catch (Exception e) {
      logger.error("更新机构信息出错， insId = " + insRol.getId(), e);
      throw new TaskException(e);
    }

  }

}
