package com.smate.web.dyn.service.psn;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.consts.service.ConstPositionService;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 单位信息服务实现类
 * 
 * @author zk
 *
 */
@Service("insInfoService")
@Transactional(rollbackOn = Exception.class)
public class InsInfoServiceImpl implements InsInfoService {

  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private PersonQueryservice personQueryservice;
  @Autowired
  private ConstPositionService constPositionService;
  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public Map<String, String> findPsnInsInfo(Long psnId) throws DynException {
    Map<String, String> insInfoMap = null;
    Person person = personQueryservice.findPersonInsAndPos(psnId);
    if (person == null) {
      return insInfoMap;
    }
    if (person.getInsId() == null && person.getPosId() == null) {
      insInfoMap = this.dealInsInfoByInsIdIsNull(person, insInfoMap);
    } else {
      insInfoMap = this.dealInsInfo(person, insInfoMap);
    }
    return insInfoMap;
  }

  /**
   * 处理单位id和职称id都为空的情况
   * 
   * @param person
   * @param locale
   * @param insInfoMap
   * @return
   */
  private Map<String, String> dealInsInfoByInsIdIsNull(Person person, Map<String, String> insInfoMap) {
    insInfoMap = new HashMap<String, String>();
    String info = "";
    if (StringUtils.isNotBlank(person.getInsName()) && StringUtils.isNotBlank(person.getPosition())) {
      info = person.getInsName() + ", " + person.getPosition();
    } else if (StringUtils.isNotBlank(person.getInsName())) {
      info = person.getInsName();
    } else if (StringUtils.isNotBlank(person.getPosition())) {
      info = person.getPosition();
    }
    insInfoMap.put(INS_INFO_ZH, info);
    insInfoMap.put(INS_INFO_EN, info);
    return insInfoMap;
  }

  /**
   * 处理单位信息
   * 
   * @param person
   * @param insInfoMap
   * @return
   */
  private Map<String, String> dealInsInfo(Person person, Map<String, String> insInfoMap) {

    Institution ins = institutionDao.findInsName(person.getInsId());
    Map<String, String> posMap = this.dealPosName(person);
    if (ins == null || (StringUtils.isBlank(ins.getZhName()) && StringUtils.isBlank(ins.getEnName()))) {
      return this.dealInsInfoByInsIdIsNull(person, insInfoMap);
    } else {
      String zhInfo = "";
      String enInfo = "";
      if (StringUtils.isBlank(ins.getZhName())) {
        zhInfo += ins.getEnName();
        enInfo += ins.getEnName();
      } else if (StringUtils.isBlank(ins.getEnName())) {
        zhInfo += ins.getZhName();
        enInfo += ins.getZhName();
      } else {
        zhInfo += ins.getZhName();
        enInfo += ins.getEnName();
      }
      insInfoMap = new HashMap<String, String>();
      if (StringUtils.isNotBlank(posMap.get("ZHNAME"))) {
        zhInfo += ", " + posMap.get("ZHNAME");
        enInfo += ", " + posMap.get("ENNAME");
      }
      insInfoMap.put(INS_INFO_ZH, zhInfo);
      insInfoMap.put(INS_INFO_EN, enInfo);
    }
    return insInfoMap;
  }

  /**
   * 处理职称 ZHNAME 中文 ENNAME 英文
   * 
   * @param posId
   * @return
   */
  private Map<String, String> dealPosName(Person person) {
    Map<String, String> posMap = constPositionService.getPositionNameById(person.getPosId());
    if (posMap == null) {
      posMap = new HashMap<String, String>();
      posMap.put("ZHNAME", person.getPosition());
      posMap.put("ENNAME", person.getPosition());
    } else {
      if (StringUtils.isBlank(posMap.get("ZHNAME")) && StringUtils.isBlank(posMap.get("ENNAME"))) {
        posMap.put("ZHNAME", person.getPosition());
        posMap.put("ENNAME", person.getPosition());
      } else if (StringUtils.isBlank(posMap.get("ZHNAME"))) {
        posMap.put("ZHNAME", posMap.get("ENNAME"));
      } else if (StringUtils.isBlank(posMap.get("ENNAME"))) {
        posMap.put("ENNAME", posMap.get("ZHNAME"));
      }
    }
    return posMap;
  }
}
